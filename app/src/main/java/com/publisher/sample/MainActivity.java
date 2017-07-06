package com.publisher.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vungle.publisher.VungleAdEventListener;
import com.vungle.publisher.VungleInitListener;
import com.vungle.publisher.VunglePub;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "Vungle Sample App";
    final VunglePub vunglePub = VunglePub.getInstance();

    // UI elements
    private Button initButton;
    private Button[] load_buttons = new Button[3];
    private Button[] play_buttons = new Button[3];

    // Get your Vungle App ID and Placement ID information from Vungle Dashboard
    final String app_id = "5916309cb46f6b5a3e00009c";
    final String DEFAULT_PLACEMENT_ID = "DEFAULT32590";
    private final String[] placement_list = { DEFAULT_PLACEMENT_ID, "TESTREW28799", "TESTINT07107" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIElements();
    }

    private void initSDK() {

        // Initialize Vungle SDK with Vungle App ID, Placement ID list and VungleInitListener
        vunglePub.init(this, app_id, placement_list, new VungleInitListener() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG, "init success");
                vunglePub.clearAndSetEventListeners(vungleDefaultListener);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtonState(initButton, false);
                        for (int i = 0; i < 3; i++) {
                            setButtonState(play_buttons[i], vunglePub.isAdPlayable(placement_list[i]));
                            setButtonState(load_buttons[i], !vunglePub.isAdPlayable(placement_list[i]));
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d(LOG_TAG, "init failure: " );
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
        placement_id_texts[0].setText("Placement ID: " + placement_list[0]);

        placement_id_texts[1] = (TextView)findViewById(R.id.placement_id2);
        placement_id_texts[1].setText("Placement ID: " + placement_list[1]);

        placement_id_texts[2] = (TextView)findViewById(R.id.placement_id3);
        placement_id_texts[2].setText("Placement ID: " + placement_list[2]);

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
                    if (vunglePub != null && vunglePub.isInitialized()) {

                        // Load an ad using a Placement ID
                        vunglePub.loadAd(placement_list[index]);
                    }
                }
            });

            play_buttons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (vunglePub != null && vunglePub.isInitialized()) {

                        // Check a Placement if it is ready to play the Ad
                        if (vunglePub.isAdPlayable(placement_list[index])) {
                            setButtonState(play_buttons[index], false);
                            // Play a Placement ad with Placement ID, you can pass AdConfig to customize your ad
                            vunglePub.playAd(placement_list[index], null);
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

    private final VungleAdEventListener vungleDefaultListener = new VungleAdEventListener() {

        @Override
        public void onAdEnd(@NonNull String placementReferenceId, boolean wasSuccessFulView, boolean wasCallToActionClicked) {
            // Called when user exits the ad and control is returned to your application
            // if wasSuccessfulView is true, the user watched the ad and could be rewarded
            // if wasCallToActionClicked is true, the user clicked the call to action button in the ad.
            Log.d(LOG_TAG, "onAdEnd: " + placementReferenceId + " ,wasSuccessfulView: " + wasSuccessFulView + " ,wasCallToActionClicked: " + wasCallToActionClicked);

        }

        @Override
        public void onAdStart(@NonNull String placementReferenceId) {
            // Called before playing an ad
            Log.d(LOG_TAG, "onAdStart: " + placementReferenceId);
        }

        @Override
        public void onUnableToPlayAd(@NonNull String placementReferenceId, String reason) {
            // Called after playAd(placementId, adConfig) is unable to play the ad
            Log.d(LOG_TAG, "onUnableToPlayAd: " + placementReferenceId + " ,reason: " + reason);
        }

        @Override
        public void onAdAvailabilityUpdate(@NonNull String placementReferenceId, boolean isAdAvailable) {

            // Notifies ad availability for the indicated placement
            // There can be duplicate notifications
            Log.d(LOG_TAG, "onAdAvailabilityUpdate: " + placementReferenceId + " isAdAvailable: " + isAdAvailable);

            final boolean enabled = isAdAvailable;
            final String placementIdUpdated = placementReferenceId;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (placementIdUpdated.equals(placement_list[0])) {
                        setButtonState(play_buttons[0], enabled);
                    } else if (placementIdUpdated.equals(placement_list[1])) {
                        setButtonState(play_buttons[1], enabled);
                        setButtonState(load_buttons[1], !enabled);
                    } else if (placementIdUpdated.equals(placement_list[2])) {
                        setButtonState(play_buttons[2], enabled);
                        setButtonState(load_buttons[2], !enabled);
                    }
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onDestroy() {
        // remove VungleAdEventListeners
        vunglePub.removeEventListeners(vungleDefaultListener);
        super.onDestroy();
    }
}