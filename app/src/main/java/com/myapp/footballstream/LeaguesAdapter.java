package com.myapp.footballstream;

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

import com.applovin.mediation.ads.MaxInterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.ViewHolder> {
    private  boolean bool = true;
    private final Context context;
    private final Activity activity;
    private final List<League> leagueList;
    private MaxInterstitialAd mInterstitialAd;
    public LeaguesAdapter(Context context, Activity activity, List<League> list) {
        this.context = context;
        this.leagueList = list;
        this.activity = activity;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);
        if(this.bool) {
            mInterstitialAd = new MaxInterstitialAd(activity.getString(R.string.Interstitial_Ad_Unit), activity );
            // Load the first ad
            mInterstitialAd.loadAd();
            this.bool = false;
        }
        return new ViewHolder(view);
    }
    public void onBindViewHolder(ViewHolder holder, int position) {
        final League league = this.leagueList.get(position);
        holder.leagueName.setText(league.getLeagueLocation() + "-" + league.getLeagueName());
        holder.leagueSeason.setText(league.getLeagueSeason());
        Picasso.get().load(league.getLeagueImage()).placeholder(R.drawable.baseline_apps_24).error(R.drawable.baseline_apps_24).into(holder.leagueImage);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(LeaguesAdapter.this.context, LeagueActivity.class);
            intent.putExtra("id", league.getLeagueId());
            LeaguesAdapter.this.context.startActivity(intent);
            if(mInterstitialAd.isReady()) mInterstitialAd.showAd();
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