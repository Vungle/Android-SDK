package com.publisher.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vungle.warren.AdConfig;
import com.vungle.warren.InitCallback;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleNativeAd;
import com.vungle.warren.Vungle.Consent;
import com.vungle.warren.error.VungleException;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // UI elements
    private Button initButton;
    private Button[] load_buttons = new Button[3];
    private Button[] play_buttons = new Button[3];
    private Button close_ff_button;
    private Button pause_ff_button;
    private Button resume_ff_button;

    // Get your Vungle App ID and Placement ID information from Vungle Dashboard
    final String LOG_TAG = "VungleSampleApp";

    final String app_id = "5ae0db55e2d43668c97bd65e";
    private final String autocachePlacementReferenceID = "DEFAULT-6595425";
    private final List<String> placementsList =
            Arrays.asList(autocachePlacementReferenceID, "DYNAMIC_TEMPLATE_INTERSTITIAL-6969365", "FLEX_FEED-2416159");

    private RelativeLayout flexfeed_container;
    private VungleNativeAd vungleNativeAd;
    private View nativeAdView;

    private AdConfig adConfig = new AdConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUIElements();
    }

    private void initSDK() {
        Vungle.init(app_id, getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG, "InitCallback - onSuccess");

//                // Usage example of GDPR API
//                // To set the user's consent status to opted in:
//                Vungle.updateConsentStatus(Vungle.Consent.OPTED_IN, “1.0.0”);
//
//                // To set the user's consent status to opted out:
//                Vungle.updateConsentStatus(Vungle.Consent.OPTED_OUT, “1.0.0”);
//
//                // To find out what the user's current consent status is:
//                // This will return null if the GDPR Consent status has not been set
//                // Otherwise, it will return Vungle.Consent.OPTED_IN or Vungle.Consent.OPTED_OUT
//                Vungle.Consent currentStatus = Vungle.getConsentStatus();
//                String consentMessageVersion = Vungle.getConsentMessageVersion();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtonState(initButton, false);
                        for (int i = 0; i < 3; i++) {
                            if (i != 0) {
                                setButtonState(load_buttons[i], !Vungle.canPlayAd(placementsList.get(i)));
                            }
                            setButtonState(play_buttons[i], Vungle.canPlayAd(placementsList.get(i)));
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(LOG_TAG, "InitCallback - onError: " + throwable.getLocalizedMessage());
            }

            @Override
            public void onAutoCacheAdAvailable(final String placementReferenceID) {
                Log.d(LOG_TAG, "InitCallback - onAutoCacheAdAvailable" +
                        "\n\tPlacement Reference ID = " + placementReferenceID);
                // SDK will request auto cache placement ad immediately upon initialization
                // This callback is triggered every time the auto-cached placement is available
                // This is the best place to add your own listeners and propagate them to any UI logic bearing class
                setButtonState(play_buttons[0], true);
            }
        });
    }

    private final PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
        @Override
        public void onAdStart(final String placementReferenceID) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdStart" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final int index = placementsList.indexOf(placementReferenceID);

                    if (placementReferenceID != autocachePlacementReferenceID) {
                        setButtonState(load_buttons[index], true);
                    }

                    setButtonState(play_buttons[index], false);
                }
            });
        }

        @Override
        public void onAdEnd(final String placementReferenceID, final boolean completed, final boolean isCTAClicked) {
            Log.d(LOG_TAG, "PlayAdCallback - onAdEnd" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tView Completed = " + completed + "" +
                    "\n\tDownload Clicked = " + isCTAClicked);
        }

        @Override
        public void onError(final String placementReferenceID, Throwable throwable) {
            Log.d(LOG_TAG, "PlayAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + throwable.getLocalizedMessage());

            checkInitStatus(throwable);
        }
    };

    private final LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(final String placementReferenceID) {

            Log.d(LOG_TAG,"LoadAdCallback - onAdLoad" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setButtonState(play_buttons[placementsList.indexOf(placementReferenceID)], true);
                }
            });

            setButtonState(load_buttons[placementsList.indexOf(placementReferenceID)], false);
        }

        @Override
        public void onError(final String placementReferenceID, Throwable throwable) {
            Log.d(LOG_TAG, "LoadAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + throwable.getLocalizedMessage());

            checkInitStatus(throwable);
        }
    };


    private void checkInitStatus(Throwable throwable) {
        try {
            VungleException ex = (VungleException) throwable;
            Log.d(LOG_TAG, ex.getExceptionCode() + "");

            if (ex.getExceptionCode() == VungleException.VUNGLE_NOT_INTIALIZED) {
                initSDK();
            }
        } catch (ClassCastException cex) {
            Log.d(LOG_TAG, cex.getMessage());
        }
    }

    private void initUIElements() {
        setContentView(R.layout.activity_main);
        TextView vungle_app_id = (TextView)findViewById(R.id.vungle_app_id);
        vungle_app_id.setText("App ID: " + app_id);

        initButton = (Button)findViewById(R.id.init_button);
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSDK();
                setButtonState(initButton, false);
            }
        });

        TextView[] placement_id_texts = new TextView[3];

        placement_id_texts[0] = (TextView)findViewById(R.id.placement_id1);
        placement_id_texts[1] = (TextView)findViewById(R.id.placement_id2);
        placement_id_texts[2] = (TextView)findViewById(R.id.placement_id3);

        load_buttons[0] = (Button)findViewById(R.id.placement_load1);
        load_buttons[1] = (Button)findViewById(R.id.placement_load2);
        load_buttons[2] = (Button)findViewById(R.id.placement_load3);

        play_buttons[0] = (Button)findViewById(R.id.placement_play1);
        play_buttons[1] = (Button)findViewById(R.id.placement_play2);
        play_buttons[2] = (Button)findViewById(R.id.placement_play3);

        flexfeed_container = findViewById(R.id.flexfeedcontainer);
        close_ff_button = findViewById(R.id.flexfeed_close);
        pause_ff_button = findViewById(R.id.flexfeed_invisible);
        resume_ff_button = findViewById(R.id.flexfeed_visible);

        setButtonState(close_ff_button, false);
        setButtonState(pause_ff_button, false);
        setButtonState(resume_ff_button, false);

        for (int i = 0; i < 3; i++) {
            placement_id_texts[i].setText("Placement ID: " + placementsList.get(i));
            setButtonState(load_buttons[i], false);
            setButtonState(play_buttons[i], false);
        }

        for (int i = 0; i < 3; i++) {
            final int index = i;

            load_buttons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Vungle.isInitialized() && index != 0) {
                        setButtonState(load_buttons[index], false);
                        Vungle.loadAd(placementsList.get(index), vungleLoadAdCallback);
                    }
                }
            });

            play_buttons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Vungle.isInitialized() && Vungle.canPlayAd(placementsList.get(index))) {
                        if (index == 2) {
                            // Play Flex-Feed ad
                            vungleNativeAd = Vungle.getNativeAd(placementsList.get(2), vunglePlayAdCallback);
                            nativeAdView = vungleNativeAd.renderNativeView();
                            flexfeed_container.addView(nativeAdView);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setButtonState(close_ff_button, true);
                                    setButtonState(pause_ff_button, true);
                                    setButtonState(resume_ff_button, true);
                                }
                            });
                        } else if (index == 1) {
                            // Play Dynamic Template ad
                            Vungle.playAd(placementsList.get(index), null, vunglePlayAdCallback);
                        } else {
                            // Play default placement with ad customization
                            adConfig.setBackButtonImmediatelyEnabled(true);
                            adConfig.setAutoRotate(true);
                            adConfig.setMuted(false);
                            // Optional settings for rewarded ads
                            Vungle.setIncentivizedFields("TestUser","RewardedTitle","RewardedBody","RewardedKeepWatching","RewardedClose");

                            Vungle.playAd(placementsList.get(index), adConfig, vunglePlayAdCallback);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setButtonState(play_buttons[index], false);
                            }
                        });
                    }
                }
            });
        }

        close_ff_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtonState(close_ff_button, false);
                        setButtonState(pause_ff_button, false);
                        setButtonState(resume_ff_button, false);
                    }
                });
                vungleNativeAd.finishDisplayingAd();
                flexfeed_container.removeView(nativeAdView);
                vungleNativeAd = null;
            }
        });

        resume_ff_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vungleNativeAd.setAdVisibility(true);
            }
        });

        pause_ff_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vungleNativeAd.setAdVisibility(false);
            }
        });
    }

    private void setButtonState(Button button, boolean enabled) {
        button.setEnabled(enabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            button.setAlpha(enabled ? 1.0f : 0.5f);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (vungleNativeAd != null) {
            vungleNativeAd.setAdVisibility(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (vungleNativeAd != null) {
            vungleNativeAd.setAdVisibility(false);
        }
    }
}