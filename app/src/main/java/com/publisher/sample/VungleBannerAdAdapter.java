package com.publisher.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vungle.warren.AdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.error.VungleException;

public class VungleBannerAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BANNED_AD_TYPE = R.layout.ad_item;
    private final String placementId;
    private final int adPosition;
    private final RecyclerView.Adapter originalAdapter;
    private final PlayAdCallback playAdCallback;
    private VungleBanner ad;
    private boolean destroyed;

    public VungleBannerAdAdapter(@NonNull String placementId,
                                 int adPosition,
                                 @NonNull RecyclerView.Adapter originalAdapter,
                                 @Nullable PlayAdCallback playAdCallback) {
        this.placementId = placementId;
        this.adPosition = adPosition;
        this.originalAdapter = originalAdapter;
        this.playAdCallback = playAdCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == BANNED_AD_TYPE) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(type, viewGroup, false);
            return new OneAdHolder(view);
        }
        return originalAdapter.onCreateViewHolder(viewGroup, type);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == BANNED_AD_TYPE) {
            ((OneAdHolder) holder).bind(placementId);
        } else {
            //noinspection unchecked
            originalAdapter.onBindViewHolder(holder, position < adPosition ? position : position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + originalAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == adPosition) {
            return BANNED_AD_TYPE;
        } else {
            return position < adPosition ?
                    originalAdapter.getItemViewType(position) :
                    originalAdapter.getItemViewType(position + 1);
        }
    }

    //must be called
    public void destroy() {
        destroyed = true;
        if (ad != null) {
            ad.destroyAd();
        }
    }

    private boolean canStart() {
        return ad == null && !destroyed;
    }

    private class OneAdHolder extends RecyclerView.ViewHolder {
        private final ViewGroup viewGroup;

        OneAdHolder(@NonNull View itemView) {
            super(itemView);
            viewGroup = itemView.findViewById(R.id.ad_container);
        }

        void bind(final String placement) {
            if (canStart()) {
                final AdConfig.AdSize size = AdConfig.AdSize.BANNER;
                Banners.loadBanner(placement, size, new LoadAdCallback() {
                    @Override
                    public void onAdLoad(String s) {
                        if (canStart()) {
                            ad = Banners.getBanner(placement, size, playAdCallback);
                            if (ad != null) {
                                ad.disableLifeCycleManagement(true);
                                viewGroup.addView(ad);
                                ad.renderAd();
                            }
                        }
                    }

                    @Override
                    public void onError(String s, VungleException e) {
                    }
                });
            }
        }
    }
}
