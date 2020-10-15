package com.publisher.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vungle.warren.AdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.VungleNativeAd;
import com.vungle.warren.error.VungleException;

import java.util.ArrayList;
import java.util.List;

public class BannerMultipleActivity extends AppCompatActivity {

    private class VungleBannerAd {
        @NonNull private final String name;
        @NonNull private final String placementReferenceId;
        @NonNull private final Button loadButton;
        @NonNull private final Button playButton;
        @NonNull private final Button pauseResumeButton;
        @NonNull private final Button closeButton;
        @NonNull private boolean nativeAdPlaying;
        @Nullable private final RelativeLayout container;
        @Nullable private VungleBanner vungleBanner;

        private VungleBannerAd(String name) {
            this.name = name;
            this.placementReferenceId = getPlacementReferenceId();
            this.loadButton = getLoadButton();
            this.playButton = getPlayButton();
            this.pauseResumeButton = getPauseResumeButton();
            this.closeButton = getCloseButton();
            this.container = getContainer();
            this.nativeAdPlaying = false;
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
            if (button != null) {
                return button;
            }
            return null;
        }

        private Button getCloseButton() {
            int buttonId = getResources().getIdentifier("btn_close_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            if (button != null) {
                return button;
            }
            return null;
        }

        private RelativeLayout getContainer() {
            int containerId = getResources().getIdentifier("container_" + name, "id", PACKAGE_NAME);
            RelativeLayout container = (RelativeLayout) findViewById(containerId);
            if (container != null) {
                return container;
            }
            return null;
        }
    }

    protected static String PACKAGE_NAME;

    private View nativeAdView;
    private VungleBanner   vungleBannerAdTop;
    private VungleNativeAd vungleNativeAd;
    private VungleBanner   vungleBannerAdBottom;

    private VungleBanner vungleBanner;

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

            VungleBannerAd ad = getVungleAd(placementReferenceID);
            if (ad != null) {
                disableButton(ad.playButton);
            }
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
        public void onError(final String placementReferenceID, VungleException throwable) {
            Log.d(LOG_TAG, "PlayAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + throwable.getLocalizedMessage());

            makeToast(throwable.getLocalizedMessage());
        }
    };

    private final LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(final String placementReferenceID) {
            Log.d(LOG_TAG, "LoadAdCallback - onAdLoad" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

            VungleBannerAd ad = getVungleAd(placementReferenceID);
            if (ad != null) {
                enableButton(ad.playButton);
            }
        }

        @Override
        public void onError(final String placementReferenceID, VungleException throwable) {
            Log.d(LOG_TAG, "LoadAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + throwable.getLocalizedMessage());

            makeToast(throwable.getLocalizedMessage());
            VungleBannerAd ad = getVungleAd(placementReferenceID);
            enableButton(ad.loadButton);
        }
    };

    private void setVungleBannerAdUi(final VungleBannerAd ad) {
        if (Vungle.canPlayAd(ad.placementReferenceId)) {
            enableButton(ad.playButton);
        } else {
            disableButton(ad.playButton);
        }

        switch (ad.name) {
            case bannerMiddle:
                setNativeAd(ad);
                break;
            case bannerTop:
                ad.vungleBanner = vungleBannerAdTop;
                setBannerAd(ad);
            case bannerBottom:
                ad.vungleBanner = vungleBannerAdBottom;
                setBannerAd(ad);
                break;
            default:
                Log.d(LOG_TAG, "Vungle ad type not recognized");
                break;
        }
    }

    private void setNativeAd(final VungleBannerAd ad) {
        disableButton(ad.pauseResumeButton);
        disableButton(ad.closeButton);

        ad.loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vungle.isInitialized()) {
                    // Play Vungle ad
                    Vungle.loadAd(ad.placementReferenceId, vungleLoadAdCallback);
                    // Button UI
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
                    AdConfig adConfig = new AdConfig();

                    if (Vungle.canPlayAd(ad.placementReferenceId)) {
                        if (vungleNativeAd != null) {
                            vungleNativeAd.finishDisplayingAd();
                            vungleNativeAd = null;
                            ad.container.removeView(nativeAdView);
                        }

                        if (ad.name == bannerMiddle) {
                            adConfig.setAdSize(AdConfig.AdSize.VUNGLE_MREC);
                            adConfig.setMuted(true);
                        }

                        vungleNativeAd = Vungle.getNativeAd(ad.placementReferenceId, adConfig, vunglePlayAdCallback);

                        if (vungleNativeAd != null) {
                            nativeAdView = vungleNativeAd.renderNativeView();
                            ad.container.addView(nativeAdView);
                            ad.container.setVisibility(RelativeLayout.VISIBLE);
                        }

                        ad.nativeAdPlaying = true;

                        // Button UI
                        enableButton(ad.loadButton);
                        disableButton(ad.playButton);
                        enableButton(ad.pauseResumeButton);
                        enableButton(ad.closeButton);

                        ad.nativeAdPlaying = true;
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
                ad.nativeAdPlaying = !ad.nativeAdPlaying;

                if (vungleNativeAd != null) {
                    vungleNativeAd.setAdVisibility(ad.nativeAdPlaying);
                }

                if (ad.nativeAdPlaying) {
                    ad.pauseResumeButton.setText("PAUSE");
                } else {
                    ad.pauseResumeButton.setText("RESUME");
                }
            }
        });

        ad.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vungleNativeAd != null) {
                    vungleNativeAd.finishDisplayingAd();
                    vungleNativeAd = null;
                    ad.container.removeView(nativeAdView);
                    ad.container.setVisibility(RelativeLayout.GONE);
                }

                disableButton(ad.pauseResumeButton);
                disableButton(ad.closeButton);
            }
        });
    }

    private void setBannerAd(final VungleBannerAd ad) {
        disableButton(ad.pauseResumeButton);
        disableButton(ad.closeButton);

        final AdConfig adConfig = new AdConfig();
        adConfig.setAdSize(AdConfig.AdSize.BANNER);

        // Loading Banner ad works similar to fullscreen ad and only requires the placement and AdSize to be configured properly
        ad.loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vungle.isInitialized()) {
                    // Load Vungle ad
                    Banners.loadBanner(ad.placementReferenceId, adConfig.getAdSize(), vungleLoadAdCallback);
                    // Button UI
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

                    if (Banners.canPlayAd(ad.placementReferenceId, adConfig.getAdSize())) {
                        if (ad.vungleBanner != null) {
                            ad.vungleBanner.destroyAd();
                            ad.vungleBanner = null;
                            ad.container.removeAllViews();
                        }

                        ad.vungleBanner = Banners.getBanner(ad.placementReferenceId, adConfig.getAdSize(), vunglePlayAdCallback);

                        if (ad.vungleBanner != null) {
                            ad.container.addView(ad.vungleBanner);
                            ad.container.setVisibility(RelativeLayout.VISIBLE);
                        }

                        ad.nativeAdPlaying = true;

                        // Button UI
                        enableButton(ad.loadButton);
                        disableButton(ad.playButton);
                        enableButton(ad.pauseResumeButton);
                        enableButton(ad.closeButton);

                        ad.nativeAdPlaying = true;
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
                ad.nativeAdPlaying = !ad.nativeAdPlaying;

                if (ad.vungleBanner != null) {
                    ad.vungleBanner.setAdVisibility(ad.nativeAdPlaying);
                }

                if (ad.nativeAdPlaying) {
                    ad.pauseResumeButton.setText("PAUSE");
                } else {
                    ad.pauseResumeButton.setText("RESUME");
                }
            }
        });

        ad.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ad.vungleBanner != null) {
                    ad.vungleBanner.destroyAd();
                    ad.vungleBanner = null;
                    ad.container.removeView(ad.vungleBanner);
                    ad.container.setVisibility(RelativeLayout.GONE);
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
        if (button == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
                button.setAlpha(1.0f);
            }
        });
    }

    private void disableButton(final Button button) {
        if (button == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(false);
                button.setAlpha(0.5f);
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
