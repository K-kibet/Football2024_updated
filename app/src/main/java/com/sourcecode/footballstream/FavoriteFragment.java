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

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Favorite> favoriteList;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    private FrameLayout adViewContainer;
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
        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(getContext(), favoriteList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(this::readFirebase, 1000);

        adViewContainer = view.findViewById(R.id.adViewContainer);

        adViewContainer.post(this::LoadBanner);

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
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adWidth);
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