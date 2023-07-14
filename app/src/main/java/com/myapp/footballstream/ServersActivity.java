package com.myapp.footballstream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.material.button.MaterialButton;

public class ServersActivity extends AppCompatActivity {
    private MaxInterstitialAd mediationInterstitialAd;
    private MaterialButton serverOne, serverTwo, serverThree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);

        MaxAdView adView = findViewById(R.id.adView);
        serverOne = findViewById(R.id.serverOne);
        serverTwo = findViewById(R.id.serverTwo);
        serverThree = findViewById(R.id.serverThree);

        mediationInterstitialAd = new MaxInterstitialAd(getResources().getString(R.string.Interstitial_Ad_Unit), this);
        mediationInterstitialAd.loadAd();

        adView.loadAd();
        adView.startAutoRefresh();

        serverOne.setOnClickListener(v -> openServer());
        serverTwo.setOnClickListener(v -> openServer());
        serverThree.setOnClickListener(v -> openServer());
    }

    private void openServer() {
        Intent intent = new Intent(this, LivestreamActivity.class);
        if(mediationInterstitialAd.isReady()) mediationInterstitialAd.showAd();
        startActivity(intent);
    }
}