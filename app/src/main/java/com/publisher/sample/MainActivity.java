package com.publisher.sample;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vungle.warren.BannerAdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.Vungle;
import com.vungle.warren.AdConfig;              // Custom ad configurations
import com.vungle.warren.InitCallback;          // Initialization callback
import com.vungle.warren.LoadAdCallback;        // Load ad callback
import com.vungle.warren.PlayAdCallback;        // Play ad callback
import com.vungle.warren.VungleBanner;          // Banner
import com.vungle.warren.Vungle.Consent;        // GDPR consent
import com.vungle.warren.VungleSettings;
import com.vungle.warren.error.VungleException; // onError message


import java.util.List;
import java.util.ArrayList;

import static com.vungle.warren.Vungle.getValidPlacements;

public class MainActivity extends AppCompatActivity {
    protected static String PACKAGE_NAME;

    private VungleBanner vungleBannerAd;

    final private List<VungleAd> vungleAds = new ArrayList<>();

    final private String interstitial = "interstitial";
    final private String rewardedVideo = "rewarded_video";
    final private String mrec = "mrec";
    final private String banner = "banner";

    final String LOG_TAG = "VungleSampleApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        vungleAds.add(new VungleAd(interstitial));
        vungleAds.add(new VungleAd(rewardedVideo));
        vungleAds.add(new VungleAd(mrec));
        vungleAds.add(new VungleAd(banner));

        initUiElements();
        initSDK();
    }

    private void initSDK() {
        final String appId = getString(R.string.app_id);

        final long MEGABYTE = 1024L * 1024L;
        final VungleSettings vungleSettings =
                new VungleSettings.Builder()
                        .setMinimumSpaceForAd(20 * MEGABYTE)
                        .setMinimumSpaceForInit(21 * MEGABYTE)
                        .setAndroidIdOptOut(false)
                        .build();

        // CCPA example
//        Vungle.updateCCPAStatus(Consent.OPTED_OUT);
//        Vungle.getCCPAStatus();

        // GDPR example
//        Vungle.updateConsentStatus(Consent.OPTED_OUT, "1.0.0");
//        Vungle.getConsentStatus();
//        Vungle.getConsentMessageVersion();

        Vungle.init(appId, getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {
                makeToast("Vungle SDK initialized");
                Log.d(LOG_TAG, "InitCallback - onSuccess");
                Log.d(LOG_TAG, "Vungle SDK Version - " + com.vungle.warren.BuildConfig.VERSION_NAME);
                Log.d(LOG_TAG, "Valid placement list:");

                for (String validPlacementReferenceIdId : getValidPlacements()) {
                    Log.d(LOG_TAG, validPlacementReferenceIdId);
                }

                // Set button state according to ad playability
                for (VungleAd vungleAd : vungleAds) {
                    if (Vungle.canPlayAd(vungleAd.placementReferenceId)) {
                        enableButton(vungleAd.playButton);
                    } else {
                        enableButton(vungleAd.loadButton);
                    }
                }

                // Set custom configuration for rewarded placements
                setCustomRewardedFields();
            }

            @Override
            public void onError(VungleException vungleException) {
                if (vungleException != null) {
                    Log.d(LOG_TAG, "InitCallback - onError: " + vungleException.getLocalizedMessage());
                } else {
                    Log.d(LOG_TAG, "VungleException is null");
                }
            }

            @Override
            public void onAutoCacheAdAvailable(final String placementReferenceID) {
                Log.d(LOG_TAG, "InitCallback - onAutoCacheAdAvailable" +
                        "\n\tPlacement Reference ID = " + placementReferenceID);

                VungleAd ad = getVungleAd(placementReferenceID);
                if (ad != null) {
                    enableButton(ad.playButton);
                    disableButton(ad.loadButton);
                }
            }
        }, vungleSettings);
    }

    private final PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
        @Override
        public void onAdStart(final String placementReferenceID) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdStart" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

            VungleAd ad = getVungleAd(placementReferenceID);
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
            Log.d(LOG_TAG, "PlayAdCallback - onAdRewarded" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);
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
            checkInitStatus(vungleException);
        }
    };

    private final LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(final String placementReferenceID) {
            Log.d(LOG_TAG, "LoadAdCallback - onAdLoad" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

            VungleAd ad = getVungleAd(placementReferenceID);
            if (ad != null) {
                enableButton(ad.playButton);
                disableButton(ad.loadButton);
                enableButton(ad.bannerListButton);
            }
        }

        @Override
        public void onError(final String placementReferenceID, VungleException vungleException) {
            Log.d(LOG_TAG, "LoadAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + vungleException.getLocalizedMessage());

            makeToast(vungleException.getLocalizedMessage());
            checkInitStatus(vungleException);
            VungleAd ad = getVungleAd(placementReferenceID);
            enableButton(ad.loadButton);
        }
    };

    private void checkInitStatus(VungleException vungleException) {
        try {
            VungleException ex = (VungleException) vungleException;
            Log.d(LOG_TAG, ex.getExceptionCode() + "");

            if (ex.getExceptionCode() == VungleException.VUNGLE_NOT_INTIALIZED) {
                initSDK();
            }
        } catch (ClassCastException cex) {
            Log.d(LOG_TAG, cex.getMessage());
        }
    }

    private void setVungleAdUi(final VungleAd ad) {
        if (Vungle.canPlayAd(ad.placementReferenceId)) {
            enableButton(ad.playButton);
        } else {
            disableButton(ad.playButton);
        }

        switch (ad.name) {
            case interstitial:
            case rewardedVideo:
                setFullscreenAd(ad);
                break;
            case mrec:
                setBannerAd(ad, AdConfig.AdSize.VUNGLE_MREC);
                break;
            case banner:
                setBannerAd(ad, AdConfig.AdSize.BANNER);
                break;
            default:
                Log.d(LOG_TAG, "Vungle ad type not recognized");
                break;
        }
    }

    private void setFullscreenAd(final VungleAd ad) {
        ad.loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vungle.isInitialized()) {
                    Vungle.loadAd(ad.placementReferenceId, vungleLoadAdCallback);

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
                    if (Vungle.canPlayAd(ad.placementReferenceId)) {
                        final AdConfig adConfig = getAdConfig();

                        Vungle.playAd(ad.placementReferenceId, adConfig, vunglePlayAdCallback);

                        enableButton(ad.loadButton);
                        disableButton(ad.playButton);
                    } else {
                        makeToast("Vungle ad not playable for " + ad.placementReferenceId);
                    }
                } else {
                    makeToast("Vungle SDK not initialized");
                }
            }
        });
    }

    private void setBannerAd(final VungleAd ad, final AdConfig.AdSize adSize) {
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
                    if (Banners.canPlayAd(ad.placementReferenceId, bannerAdConfig.getAdSize())) {
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
                    ad.container.setVisibility(View.GONE);
                    vungleBannerAd.destroyAd();
                    vungleBannerAd = null;
                }

                disableButton(ad.pauseResumeButton);
                disableButton(ad.closeButton);
            }
        });

        if (ad.bannerListButton != null) {
            ad.bannerListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(BannerListActivity.getIntent(MainActivity.this, ad.placementReferenceId));
                }
            });
        }

        if (ad.bannerMultipleButton != null) {
            ad.bannerMultipleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(BannerMultipleActivity.getIntent(MainActivity.this));
                }
            });
        }
    }

    private AdConfig getAdConfig() {
        AdConfig adConfig = new AdConfig();

        adConfig.setAdOrientation(AdConfig.MATCH_VIDEO);
        adConfig.setMuted(true);

        return adConfig;
    }

    private void setCustomRewardedFields() {
        Vungle.setIncentivizedFields("TestUser", "RewardTitle", "RewardedBody", "RewardedKeepWatching", "RewardedClose");
    }


    private void initUiElements() {
        Log.d(LOG_TAG, "initUiElements");

        TextView text_app_id = (TextView) findViewById(R.id.text_app_id);
        text_app_id.setText("App ID - " + getString(R.string.app_id));

        Log.d(LOG_TAG, "!!!VungleAd Test Begins!!!");

        for (VungleAd vungleAd : vungleAds) {
            setVungleAdUi(vungleAd);
        }
    }

    private VungleAd getVungleAd(String placementReferenceId) {
        for (VungleAd vungleAd : vungleAds) {
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

    private class VungleAd {
        @NonNull private final String name;
        @NonNull private final String placementReferenceId;
        @NonNull private final TextView titleTextView;
        @NonNull private final Button loadButton;
        @NonNull private final Button playButton;
        @Nullable private final Button pauseResumeButton;
        @Nullable private final Button closeButton;
        @Nullable private final Button bannerListButton;
        @Nullable private final Button bannerMultipleButton;
        @Nullable private final FrameLayout container;
        @NonNull private boolean bannerAdPlaying;

        private VungleAd(String name) {
            this.name = name;
            this.placementReferenceId = getPlacementReferenceId();
            this.titleTextView = getTextView();
            this.loadButton = getLoadButton();
            this.playButton = getPlayButton();
            this.pauseResumeButton = getPauseResumeButton();
            this.closeButton = getCloseButton();
            this.bannerListButton = getBannerListButton();
            this.bannerMultipleButton = getBannerMultipleButton();
            this.container = getContainer();
            this.bannerAdPlaying = false;
        }

        private String getPlacementReferenceId() {
            int stringId = getResources().getIdentifier("placement_id_" + name, "string", PACKAGE_NAME);
            return getString(stringId);
        }

        private TextView getTextView() {
            int textViewId = getResources().getIdentifier("tv_" + name, "id", PACKAGE_NAME);
            String textViewString = getString(getResources().getIdentifier("title_" + name, "string", PACKAGE_NAME));
            TextView tv = (TextView) findViewById(textViewId);
            tv.setText(textViewString + " - " + placementReferenceId);
            return tv;
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
                disableButton(button);
                return button;
            }
            return null;
        }

        private Button getCloseButton() {
            int buttonId = getResources().getIdentifier("btn_close_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            if (button != null) {
                disableButton(button);
                return button;
            }
            return null;
        }

        private FrameLayout getContainer() {
            int containerId = getResources().getIdentifier("container_" + name, "id", PACKAGE_NAME);
            FrameLayout container = (FrameLayout) findViewById(containerId);
            if (container != null) {
                return container;
            }
            return null;
        }

        private Button getBannerListButton() {
            int buttonId = getResources().getIdentifier("btn_list_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            if (button != null) {
                return button;
            }
            return null;
        }

        private Button getBannerMultipleButton() {
            int buttonId = getResources().getIdentifier("btn_multiple_" + name, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(buttonId);
            if (button != null) {
                return button;
            }
            return null;
        }
    }
}