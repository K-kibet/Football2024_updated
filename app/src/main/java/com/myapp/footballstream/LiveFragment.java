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
public class LiveFragment extends Fragment {
    private RecyclerView recyclerView;
    private AllAdapter allAdapter;
    private List<AllMatch> allMatchList;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);

        allMatchList = new ArrayList<>();
        allAdapter = new AllAdapter(getContext(), getActivity(),allMatchList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(this::readFirebase, 1000);

        MaxAdView adView = view.findViewById(R.id.adView);
        adView.loadAd();
        adView.startAutoRefresh();
    }

    private void readFirebase () {
        db.collection("all").whereEqualTo("live", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AllMatch allMatch = new AllMatch(
                                    document.getString("status"),
                                    document.getString("home"),
                                    document.getString("away"),
                                    document.getString("homeImage"),
                                    document.getString("awayImage"),
                                    document.getString("homeResult"),
                                    document.getString("awayResult"));
                            allMatchList.add(allMatch);
                        }
                        recyclerView.setAdapter(allAdapter);
                    }
                });
    }
}