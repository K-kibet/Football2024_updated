package com.sourcecode.footballstream;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
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
    private FrameLayout adViewContainer;
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
        leagueList = new ArrayList<>();
        leaguesAdapter = new LeaguesAdapter(getContext(), leagueList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(this::readFirebase, 1000);

        adViewContainer = view.findViewById(R.id.adViewContainer);

        adViewContainer.post(this::LoadBanner);
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


    private void LoadBanner() {
        AdView adView = new AdView(getContext());
        adView.setAdUnitId(getString(R.string.ADMOB_BANNER_AD_UNIT));
        adViewContainer.removeAllViews();
        adViewContainer.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adWidth);
    }
}