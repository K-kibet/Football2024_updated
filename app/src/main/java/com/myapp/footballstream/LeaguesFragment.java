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

public class LeaguesFragment extends Fragment {
    private RecyclerView recyclerView;
    private LeaguesAdapter leaguesAdapter;
    private List<League> leagueList;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leagues, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        MaxAdView adView = view.findViewById(R.id.adView);
        leagueList = new ArrayList<>();
        leaguesAdapter = new LeaguesAdapter(getContext(), getActivity(), leagueList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(this::readFirebase, 1000);

        adView.loadAd();
        adView.startAutoRefresh();
    }

    private void readFirebase () {
        db.collection("leagues")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            League league = new League(
                                    document.getString("image"),
                                    document.getString("location"),
                                    document.getString("name"),
                                    document.getString("season"),
                                    document.getId());
                            leagueList.add(league);
                        }
                        recyclerView.setAdapter(leaguesAdapter);
                    }
                });
    }

}