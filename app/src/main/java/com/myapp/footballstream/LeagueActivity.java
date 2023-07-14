package com.myapp.footballstream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import com.applovin.mediation.ads.MaxAdView;
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

        MaxAdView adView = findViewById(R.id.adView);
        adView.loadAd();
        adView.startAutoRefresh();


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