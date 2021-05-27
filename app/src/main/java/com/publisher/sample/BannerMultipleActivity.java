package com.publisher.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vungle.warren.AdConfig;
import com.vungle.warren.BannerAdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.error.VungleException;

import java.util.ArrayList;
import java.util.List;

public class BannerMultipleActivity extends AppCompatActivity {
    protected static String PACKAGE_NAME;

    private VungleBanner vungleBannerAd;

    private List<VungleBannerAd> vungleBannerAds = new ArrayList<>();

    final private String bannerTop = "banner_top";
    final private String bannerMiddle = "banner_middle";
    final private String bannerBottom = "banner_bottom";

    private static final String LOG_TAG = "VungleSamplApp";

    public static Intent getIntent(Context ctx) {
        Intent intent = new Intent(ctx, BannerMultipleActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_multiple);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        Log.d(LOG_TAG, PACKAGE_NAME);

        vungleBannerAds.add(new VungleBannerAd(bannerTop));
        vungleBannerAds.add(new VungleBannerAd(bannerMiddle));
        vungleBannerAds.add(new VungleBannerAd(bannerBottom));

        initUIElements();
    }

    private void initUIElements() {
        Log.d(LOG_TAG, "Initialize Multiple Banner Ad");

        for (VungleBannerAd vungleBannerAd : vungleBannerAds) {
            setVungleBannerAdUi(vungleBannerAd);

            if (Vungle.canPlayAd(vungleBannerAd.placementReferenceId)) {
                enableButton(vungleBannerAd.playButton);
            } else {
                enableButton(vungleBannerAd.loadButton);
            }
        }
    }


    private final PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
        @Override
        public void onAdStart(final String placementReferenceID) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdStart" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);
            disableButton(getVungleAd(placementReferenceID).playButton);
        }

        @Override
        public void onAdViewed(String placementReferenceID) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdViewed" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);
        }

        // Deprecated
        @Override
        public void onAdEnd(final String placementReferenceID, final boolean completed, final boolean isCTAClicked) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdEnd" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tView Completed = " + completed + "" +
                    "\n\tDownload Clicked = " + isCTAClicked);
        }

        @Override
        public void onAdEnd(String placementReferenceID) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdEnd" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);
        }

        @Override
        public void onAdClick(String placementReferenceID) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdClick" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);
        }

        @Override
        public void onAdRewarded(String placementReferenceID) {
            // Not applicable for banner ads
        }

        @Override
        public void onAdLeftApplication(String placementReferenceID) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdLeftApplication" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);
        }

        @Override
        public void creativeId(String creativeId) {
            Log.d(LOG_TAG, "PlayAdCallback - creativeId" +
                    "\n\tCreative ID = " + creativeId);
        }

        @Override
        public void onError(final String placementReferenceID, VungleException vungleException) {
            Log.d(LOG_TAG, "PlayAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + vungleException.getLocalizedMessage());

            makeToast(vungleException.getLocalizedMessage());
        }
    };

    private final LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(final String placementReferenceID) {
            Log.d(LOG_TAG, "LoadAdCallback - onAdLoad" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

            enableButton(getVungleAd(placementReferenceID).playButton);
        }

        @Override
        public void onError(final String placementReferenceID, VungleException vungleException) {
            Log.d(LOG_TAG, "LoadAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + vungleException.getLocalizedMessage());

            makeToast(vungleException.getLocalizedMessage());
            enableButton(getVungleAd(placementReferenceID).loadButton);
        }
    };

    private void setVungleBannerAdUi(final VungleBannerAd ad) {
        switch (ad.name) {
            case bannerTop:
            case bannerBottom:
                setBannerAd(ad, AdConfig.AdSize.BANNER);
                break;
            case bannerMiddle:
                setBannerAd(ad, AdConfig.AdSize.VUNGLE_MREC);
                break;
            default:
                Log.d(LOG_TAG, "Vungle ad type not recognized");
                break;
        }
    }

    private void setBannerAd(final VungleBannerAd ad, final AdConfig.AdSize adSize) {
        final BannerAdConfig bannerAdConfig = new BannerAdConfig();
        bannerAdConfig.setAdSize(adSize);
        bannerAdConfig.setMuted(true);

        ad.loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vungle.isInitialized()) {
                    Banners.loadBanner(ad.placementReferenceId, bannerAdConfig, vungleLoadAdCallback);

                    disableButton(ad.loadButton);
                } else {
                    makeToast("Vungle SDK not initialized");
                }
            }
        });

        ad.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vungle.isInitialized()) {
                    if (Banners.canPlayAd(ad.placementReferenceId, adSize)) {
                        if (vungleBannerAd != null) {
                            vungleBannerAd = null;
                            ad.container.removeAllViews();
                        }

                        vungleBannerAd = Banners.getBanner(ad.placementReferenceId, bannerAdConfig, vunglePlayAdCallback);

                        if (vungleBannerAd != null) {
                            ad.container.addView(vungleBannerAd);
                            ad.container.setVisibility(View.VISIBLE);
                        }
                        // Button UI
                        enableButton(ad.loadButton);
                        disableButton(ad.playButton);
                        enableButton(ad.pauseResumeButton);
                        enableButton(ad.closeButton);

                        ad.bannerAdPlaying = true;
                        ad.pauseResumeButton.setText("PAUSE");
                    } else {
                        makeToast("Vungle ad not playable for " + ad.placementReferenceId);
                    }
                } else {
                    makeToast("Vungle SDK not initialized");
                }
            }
        });

        ad.pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.bannerAdPlaying = !ad.bannerAdPlaying;

                if (vungleBannerAd != null) {
                    vungleBannerAd.setAdVisibility(ad.bannerAdPlaying);
                }

                ad.pauseResumeButton.setText(ad.bannerAdPlaying ? "PAUSE" : "RESUME");
            }
        });

        ad.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vungleBannerAd != null) {
                    ad.container.removeView(vungleBannerAd);
                    vungleBannerAd.destroyAd();
                    vungleBannerAd = null;
                }

                disableButton(ad.pauseResumeButton);
                disableButton(ad.closeButton);
            }
        });
    }

    private VungleBannerAd getVungleAd(String placementReferenceId) {
        for (VungleBannerAd vungleAd : vungleBannerAds) {
            if (vungleAd.placementReferenceId.equals(placementReferenceId)) {
                return vungleAd;
            }
        }
        return null;
    }

    private void enableButton(final Button button) {
        if (button == null) { return; }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
                button.setAlpha(1.0f);
            }
        });
    }

    private void disableButton(final Button button) {
        if (button == null) { return; }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(false);
                button.setAlpha(0.5f);
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private class VungleBannerAd {
        @NonNull private final String name;
        @NonNull private final String placementReferenceId;
        @NonNull private final Button loadButton;
        @NonNull private final Button playButton;
        @NonNull private final Button pauseResumeButton;
        @NonNull private final Button closeButton;
        @NonNull private boolean bannerAdPlaying;
        @Nullable private final FrameLayout container;

        private VungleBannerAd(String name) {
            this.name = name;
            this.placementReferenceId = getPlacementReferenceId();
            this.loadButton = getLoadButton();
            this.playButton = getPlayButton();
            this.pauseResumeButton = getPauseResumeButton();
            this.closeButton = getCloseButton();
            this.container = getContainer();
            this.bannerAdPlaying = false;
        }

        private String getPlacementReferenceId() {
            int stringId = getResources().getIdentifier("placement_id_" + name, "string", PACKAGE_NAME);
            return getString(stringId);
        }

        private Button getLoadButton() {
            int buttonId = getResources().getIdentifier("btn_load_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            disableButton(button);
            return button;
        }

        private Button getPlayButton() {
            int buttonId = getResources().getIdentifier("btn_play_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            disableButton(button);
            return button;
        }

        private Button getPauseResumeButton() {
            int buttonId = getResources().getIdentifier("btn_pause_resume_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            disableButton(button);
            return button;
        }

        private Button getCloseButton() {
            int buttonId = getResources().getIdentifier("btn_close_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            disableButton(button);
            return button;
        }

        private FrameLayout getContainer() {
            int containerId = getResources().getIdentifier("container_" + name, "id", PACKAGE_NAME);
            FrameLayout container = (FrameLayout) findViewById(containerId);
            if (container != null) {
                return container;
            }
            return null;
        }
    }
}
