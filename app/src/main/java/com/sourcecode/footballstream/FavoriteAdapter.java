package com.sourcecode.footballstream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private  boolean bool = true;
    private final Context context;
    private final List<Favorite> favoriteList;
    private InterstitialAd mInterstitialAd;
    public FavoriteAdapter( Context context, List<Favorite> list) {
        this.context = context;
        this.favoriteList = list;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        if(this.bool) {
            MobileAds.initialize(this.context);
            AdRequest adRequest = new AdRequest.Builder().build();
            Context context = this.context;
            InterstitialAd.load(context, context.getString(R.string.ADMOB_INTERSTITIAL_AD_UNIT), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            mInterstitialAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            FavoriteAdapter.this.mInterstitialAd = interstitialAd;
                        }
                    });
            this.bool = false;
        }
        return new FavoriteAdapter.ViewHolder(view);
    }
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Favorite favorite = this.favoriteList.get(position);
        holder.textTeam.setText(favorite.getName());
        Picasso.get().load(favorite.getImage()).placeholder(R.drawable.baseline_apps_24).error(R.drawable.baseline_apps_24).into(holder.imageTeam);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(FavoriteAdapter.this.context, ServersActivity.class);
            if (FavoriteAdapter.this.mInterstitialAd != null) {
                FavoriteAdapter.this.mInterstitialAd.show((Activity) FavoriteAdapter.this.context);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        FavoriteAdapter.this.mInterstitialAd = null;
                        FavoriteAdapter.this.context.startActivity(intent);
                    }
                });
            } else {
                FavoriteAdapter.this.context.startActivity(intent);
            }
        });
    }
    public int getItemCount() {
        return this.favoriteList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textTeam;
        private final ImageView imageTeam;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textTeam = itemView.findViewById(R.id.textTeam);
            this.imageTeam = itemView.findViewById(R.id.imageTeam);
        }
    }
}
