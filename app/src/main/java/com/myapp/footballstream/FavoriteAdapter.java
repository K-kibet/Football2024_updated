package com.myapp.footballstream;

import android.app.Activity;
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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private  boolean bool = true;
    private final Activity activity;
    private final List<Favorite> favoriteList;
    private MaxInterstitialAd mInterstitialAd;
    public FavoriteAdapter( Activity activity, List<Favorite> list) {
        this.favoriteList = list;
        this.activity = activity;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        if(this.bool) {
            mInterstitialAd = new MaxInterstitialAd(activity.getString(R.string.Interstitial_Ad_Unit), activity );
            // Load the first ad
            mInterstitialAd.loadAd();
            this.bool = false;
        }
        return new ViewHolder(view);
    }
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Favorite favorite = this.favoriteList.get(position);
        holder.textTeam.setText(favorite.getName());
        Picasso.get().load(favorite.getImage()).placeholder(R.drawable.baseline_apps_24).error(R.drawable.baseline_apps_24).into(holder.imageTeam);
        holder.itemView.setOnClickListener(view -> {
            if(mInterstitialAd.isReady()) mInterstitialAd.showAd();
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
