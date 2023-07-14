package com.myapp.footballstream;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applovin.mediation.ads.MaxAdView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Favorite> favoriteList;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        MaxAdView adView = view.findViewById(R.id.adView);
        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(getActivity(),favoriteList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(this::readFirebase, 1000);

        adView.loadAd();
        adView.startAutoRefresh();

    }

    private void readFirebase () {
        db.collection("leagues").document("C4Rttqq6gB4PFJgvIIy2").collection("teams")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Favorite favorite = new Favorite(
                                    document.getString("name"),
                                    document.getString("image"),
                                    document.getId());
                            favoriteList.add(favorite);
                        }
                        recyclerView.setAdapter(favoriteAdapter);
                    }
                });
    }
}