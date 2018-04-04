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
    final String app_id = "5a53f997eb59d1410c01048d";
    final String LOG_TAG = "VungleSampleApp";
    private final List<String> placement_list =
            Arrays.asList("DEFAULT14886", "INTDT3O03441", "FLEXFEED-3233399");

    private PlayAdCallback vunglePlayAdCallback;
    private RelativeLayout flexfeed_container;
    private VungleNativeAd vungleNativeAd;
    private View nativeAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flexfeed_container = findViewById(R.id.flexfeedcontainer);
        close_ff_button = findViewById(R.id.flexfeed_close);
        pause_ff_button = findViewById(R.id.flexfeed_invisible);
        resume_ff_button = findViewById(R.id.flexfeed_visible);
        setButtonState(close_ff_button, false);
        setButtonState(pause_ff_button, false);
        setButtonState(resume_ff_button, false);
        initUIElements();

        //Optional settings for all ads
        AdConfig adConfig = new AdConfig();
        adConfig.setAutoRotate(true);
        //Optional settings for incentivized ads
        Vungle.setIncentivizedFields("user1","title1","body1","keepwatching1","close1");
        //setIncentivizedFields(@Nullable String userID, @Nullable String title, @Nullable String body, @Nullable String keepWatching, @Nullable String close)

        close_ff_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtonState(play_buttons[2], false);
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

        vunglePlayAdCallback = new PlayAdCallback() {
            @Override
            public void onAdStart(String s) {
                Log.d(LOG_TAG, "PlayAdCallback onAdStart");
            }

            //b is for completed view, b1 is for clicked
            @Override
            public void onAdEnd(String s, boolean b, boolean b1) {
                Log.d(LOG_TAG, "PlayAdCallback onAdEnd b=" + b + " b1=" + b1);

                final String placementReferenceID = s;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (placementReferenceID == placement_list.get(1)) {
                            setButtonState(play_buttons[1], false);
                            setButtonState(load_buttons[1], true);
                        } else if (placementReferenceID == placement_list.get(2)) {
                            setButtonState(play_buttons[2], false);
                            setButtonState(load_buttons[2], true);
                        }
                    }
                });
            }

            @Override
            public void onError(String s, Throwable throwable) {
                Log.d(LOG_TAG, "PlayAdCallback onError");
            }
        };
    }

    private void initSDK() {

        Vungle.init(placement_list, app_id, getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG, "Init onSuccess");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtonState(initButton, false);
                        for (int i = 0; i < 3; i++) {
                            setButtonState(play_buttons[i], Vungle.canPlayAd(placement_list.get(i)));
                            setButtonState(load_buttons[i], !Vungle.canPlayAd(placement_list.get(i)));
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOG_TAG, "Init onError: " + e.getLocalizedMessage());
            }

            @Override
            public void onAutoCacheAdAvailable(String placementId) {
                Log.d(LOG_TAG, "onAutoCacheAdAvailable");
                // SDK will request auto cache placement ad immediately upon initialization
                // This callback is triggered every time the auto-cached placement is available
                // This is the best place to add your own listeners and propagate them to any UI logic bearing class
                setButtonState(play_buttons[0], true);
            }
        });
    }

    private void initUIElements() {
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
        placement_id_texts[0].setText("Placement ID: " + placement_list.get(0));

        placement_id_texts[1] = (TextView)findViewById(R.id.placement_id2);
        placement_id_texts[1].setText("Placement ID: " + placement_list.get(1));

        placement_id_texts[2] = (TextView)findViewById(R.id.placement_id3);
        placement_id_texts[2].setText("Placement ID: " + placement_list.get(2));

        load_buttons[0] = (Button)findViewById(R.id.placement_load1);
        setButtonState(load_buttons[0], false);
        load_buttons[1] = (Button)findViewById(R.id.placement_load2);
        setButtonState(load_buttons[1], false);
        load_buttons[2] = (Button)findViewById(R.id.placement_load3);
        setButtonState(load_buttons[2], false);

        play_buttons[0] = (Button)findViewById(R.id.placement_play1);
        setButtonState(play_buttons[0], false);
        play_buttons[1] = (Button)findViewById(R.id.placement_play2);
        setButtonState(play_buttons[1], false);
        play_buttons[2] = (Button)findViewById(R.id.placement_play3);
        setButtonState(play_buttons[2], false);

        for (int i = 0; i < 3; i++) {
            final int index = i;

            load_buttons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Vungle.isInitialized()) {
                        Vungle.loadAd(placement_list.get(index), vungleLoadAdCallback);
                    }
                }
            });

            play_buttons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set flex feed invisible and visible options
                    if(index==2){
                        if(Vungle.isInitialized()){
                            vungleNativeAd = Vungle.getNativeAd(placement_list.get(2), vunglePlayAdCallback);
                            nativeAdView = vungleNativeAd.renderNativeView();
                            flexfeed_container.addView(nativeAdView);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setButtonState(play_buttons[index], false);
                                    setButtonState(close_ff_button, true);
                                    setButtonState(pause_ff_button, true);
                                    setButtonState(resume_ff_button, true);
                                }
                            });
                        }
                    }
                    else{
                        if (Vungle.isInitialized()) {
                            if (Vungle.canPlayAd(placement_list.get(index))) {
                                Vungle.playAd(placement_list.get(index), null, vunglePlayAdCallback);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setButtonState(play_buttons[index], false);
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

    private void setButtonState(Button button, boolean enabled) {
        button.setEnabled(enabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            button.setAlpha(enabled ? 1.0f : 0.5f);
        }
    }

    //load ad callbacks for non-auto-cache
    private final LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
        @Override
        public void onAdLoad(String s) {

            Log.d(LOG_TAG,"LoadAdCallback onAdLoad");

            final String placementReferenceID = s;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (placementReferenceID == placement_list.get(0)) {
                        setButtonState(play_buttons[0], true);
                    } else if (placementReferenceID == placement_list.get(1)) {
                        setButtonState(play_buttons[1], true);
                        setButtonState(load_buttons[1], false);
                    } else if (placementReferenceID == placement_list.get(2)) {
                        setButtonState(play_buttons[2], true);
                        setButtonState(load_buttons[2], false);
                    }
                }

            });
        }

        @Override
        public void onError(String s, Throwable e) {
            Log.d(LOG_TAG, "LoadAdCallback onError:" + e.getLocalizedMessage());
        }
    };
}