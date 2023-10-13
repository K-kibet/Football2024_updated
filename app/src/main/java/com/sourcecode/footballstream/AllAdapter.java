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

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.ViewHolder> {
    private final List<AllMatch> allList;
    private  boolean bool = true;
    private final Context context;
    private InterstitialAd mInterstitialAd;

    public AllAdapter(Context context, List<AllMatch> list) {
        this.context = context;
        this.allList = list;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all, parent, false);
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
                            AllAdapter.this.mInterstitialAd = interstitialAd;
                        }
                    });
            this.bool = false;
        }
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final AllMatch allMatch = this.allList.get(position);
        holder.textStatus.setText(allMatch.getTextStatus());
        holder.textHome.setText(allMatch.getHomeTeam());
        holder.textAway.setText(allMatch.getAwayTeam());
        holder.textHomeResult.setText(allMatch.getHomeResult());
        holder.textAwayResult.setText(allMatch.getAwayResult());

        Picasso.get().load(allMatch.getHomeImage()).placeholder(R.drawable.baseline_apps_24).error(R.drawable.baseline_apps_24).into(holder.homeImage);
        Picasso.get().load(allMatch.getAwayImage()).placeholder(R.drawable.baseline_apps_24).error(R.drawable.baseline_apps_24).into(holder.awayImage);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(AllAdapter.this.context, ServersActivity.class);
            if (AllAdapter.this.mInterstitialAd != null) {
                AllAdapter.this.mInterstitialAd.show((Activity) AllAdapter.this.context);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        AllAdapter.this.mInterstitialAd = null;
                        AllAdapter.this.context.startActivity(intent);
                    }
                });
            } else {
                AllAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.allList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textStatus;
        private final TextView textHome;
        private final TextView textAway;
        private final TextView textHomeResult;
        private final TextView textAwayResult;
        private final ImageView homeImage, awayImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textStatus= itemView.findViewById(R.id.textStatus);
            this.textHome = itemView.findViewById(R.id.textHome);
            this.textAway = itemView.findViewById(R.id.textAway);
            this.textHomeResult = itemView.findViewById(R.id.textHomeResult);
            this.textAwayResult = itemView.findViewById(R.id.textAwayResult);
            this.homeImage = itemView.findViewById(R.id.homeImage);
            this.awayImage = itemView.findViewById(R.id.awayImage);
        }
    }
}
