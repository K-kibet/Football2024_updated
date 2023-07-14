package com.myapp.footballstream;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.applovin.mediation.ads.MaxAdView;

public class LivestreamActivity extends AppCompatActivity {
    WebView browser;
    SwipeRefreshLayout refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestream);
        browser = findViewById(R.id.webview);
        refresh = findViewById(R.id.refresh);

        browser.setWebViewClient(new Browser());
        loadUrl("https://afr.livesports088.com/");

        refresh.setOnRefreshListener(this::refreshContent);

        MaxAdView adView = findViewById(R.id.adView);
        adView.loadAd();
        adView.startAutoRefresh();
    }

    public void loadUrl(String url) {
        browser.loadUrl(url);
    }

    private class Browser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void refreshContent() {
        new Handler().postDelayed(() -> {
            String url = browser.getUrl();
            loadUrl(url);
            refresh.setRefreshing(false);
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else super.onBackPressed();
    }
}