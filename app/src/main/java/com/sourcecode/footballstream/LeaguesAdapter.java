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

public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.ViewHolder> {
    private  boolean bool = true;
    private final Context context;
    private final List<League> leagueList;
    private InterstitialAd mInterstitialAd;
    public LeaguesAdapter(Context context, List<League> list) {
        this.context = context;
        this.leagueList = list;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);
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
                            LeaguesAdapter.this.mInterstitialAd = interstitialAd;
                        }
                    });
            this.bool = false;
        }
        return new LeaguesAdapter.ViewHolder(view);
    }
    public void onBindViewHolder(ViewHolder holder, int position) {
        final League league = this.leagueList.get(position);
        holder.leagueName.setText(league.getLeagueLocation() + "-" + league.getLeagueName());
        holder.leagueSeason.setText(league.getLeagueSeason());
        Picasso.get().load(league.getLeagueImage()).placeholder(R.drawable.baseline_apps_24).error(R.drawable.baseline_apps_24).into(holder.leagueImage);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(LeaguesAdapter.this.context, LeagueActivity.class);
            intent.putExtra("id", league.getLeagueId());
            if (LeaguesAdapter.this.mInterstitialAd != null) {
                LeaguesAdapter.this.mInterstitialAd.show((Activity) LeaguesAdapter.this.context);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        LeaguesAdapter.this.mInterstitialAd = null;
                        LeaguesAdapter.this.context.startActivity(intent);
                    }
                });
            } else {
                LeaguesAdapter.this.context.startActivity(intent);
            }
        });
    }
    public int getItemCount() {
        return this.leagueList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView leagueName;
        private final TextView leagueSeason;
        private final ImageView leagueImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.leagueName = itemView.findViewById(R.id.leagueName);
            this.leagueSeason = itemView.findViewById(R.id.leagueSeason);
            this.leagueImage = itemView.findViewById(R.id.leagueImage);
        }
    }
}