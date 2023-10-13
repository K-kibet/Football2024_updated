package com.sourcecode.footballstream;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeagueActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Favorite> favoriteList;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    private FrameLayout adViewContainer;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);

        id = getIntent().getStringExtra("id");

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter( LeagueActivity.this,favoriteList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(this::readFirebase, 1000);

        adViewContainer = findViewById(R.id.adViewContainer);
        adViewContainer.post(this::LoadBanner);
    }


    private void LoadBanner() {
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ADMOB_BANNER_AD_UNIT));
        adViewContainer.removeAllViews();
        adViewContainer.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
    private void readFirebase () {
        db.collection("leagues").document(id).collection("teams")
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