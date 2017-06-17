package com.publisher.sample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.vungle.publisher.AdConfig;
import com.vungle.publisher.Orientation;
import com.vungle.publisher.VungleAdEventListener;
import com.vungle.publisher.VungleInitListener;
import com.vungle.publisher.VunglePub;

public class MainActivity extends Activity implements OnClickListener {

    // get the VunglePub instance
    final VunglePub vunglePub = VunglePub.getInstance();
    final String TAG = "VungleSample";
    final String DEFAULT_PLACEMENT_ID = "DEFAULT35839";

    // buttons
    private ImageButton buttonPlayAd;
    private ImageButton buttonPlayAdOptions;
    private ImageButton buttonPlayAdIncentivized;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get your App ID from the app's main page on the Vungle Dashboard after setting up your app
        final String app_id = "58f8f64fcf684f7f4b00002e";

        // initialize the Publisher SDK
        vunglePub.init(this, app_id, new String[]{DEFAULT_PLACEMENT_ID}, new VungleInitListener() {
            @Override
            public void onSuccess() {
                Log.d("MainActivity", "Initialization is successful");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", "Initialization failed");
            }
        });

        vunglePub.clearAndSetEventListeners(vungleDefaultListener, vungleSecondListener);

        // initialize buttons
        buttonPlayAd = (ImageButton) findViewById(R.id.button_play_ad);
        buttonPlayAdOptions = (ImageButton) findViewById(R.id.button_play_ad_options);
        buttonPlayAdIncentivized = (ImageButton) findViewById(R.id.button_play_ad_incentivized);

        boolean buttonsEnabled = vunglePub.isAdPlayable(DEFAULT_PLACEMENT_ID);
        setButtonState(buttonPlayAd, buttonsEnabled);
        setButtonState(buttonPlayAdIncentivized, buttonsEnabled);
        setButtonState(buttonPlayAdOptions, buttonsEnabled);

        // attach listener to buttons
        buttonPlayAd.setOnClickListener(this);
        buttonPlayAdOptions.setOnClickListener(this);
        buttonPlayAdIncentivized.setOnClickListener(this);
    }

    private void setButtonState(ImageButton button, boolean enabled) {
        button.setEnabled(enabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            button.setAlpha(enabled ? 1.0f : 0.5f);
        }
    }

    private final VungleAdEventListener vungleDefaultListener = new VungleAdEventListener() {
        @Override
        public void onAdEnd(String placementId, boolean wasSuccessfulView, boolean wasCallToActionClicked) {
            // Called when the user leaves the ad and control is returned to your application.
        }

        @Override
        public void onAdStart(String placementId) {
            // Called before playing an ad.
        }

        @Override
        public void onUnableToPlayAd(String placementId, String reason) {
            // Called when VunglePub.playAd() was called but no ad is available to show to the user.
        }

        @Override
        public void onAdAvailabilityUpdate(String placementId, boolean isAdAvailable) {
            // Called when ad playability changes.
            Log.d("DefaultListener", "This is a default eventlistener.");
            final boolean enabled = isAdAvailable;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Called when ad playability changes.
                    setButtonState(buttonPlayAd, enabled);
                    setButtonState(buttonPlayAdIncentivized, enabled);
                    setButtonState(buttonPlayAdOptions, enabled);
                }
            });
        }
    };

    private final VungleAdEventListener vungleSecondListener = new VungleAdEventListener() {
        // Vungle SDK allows for multiple listeners to be attached. This secondary event listener is only
        // going to print some logs for now, but it could be used to Pause music, update a badge icon, etc.
        @Override
        public void onAdEnd(String placementId, boolean wasSuccessfulView, boolean wasCallToActionClicked) {
            // Called when the user leaves the ad and control is returned to your application.
            Log.d(TAG, String.format("This is a second event listener. Ad finished playing, wasSuccessfulView - %s, wasCallToActionClicked - %s", wasSuccessfulView, wasCallToActionClicked));
        }

        @Override
        public void onAdStart(String placementId) {
            // Called before playing an ad.
            Log.d(TAG, "This is a second event listener. Ad is about to play now!");
        }

        @Override
        public void onUnableToPlayAd(String placementId, String reason) {
            // Called when VunglePub.playAd() was called but no ad is available to show to the user.
            Log.d(TAG, String.format("This is a second event listener. Unable to play Ad - %s", reason));
        }

        @Override
        public void onAdAvailabilityUpdate(String placementId, boolean isAdAvailable) {
            // Called when ad playability changes.
            Log.d(TAG, String.format("This is a second event listener! Ad playability has changed, and is now: %s", isAdAvailable));
        }
    };

    public void onClick(View view) {
        // Check if Vungle Ad is available
        if (!vunglePub.isAdPlayable(DEFAULT_PLACEMENT_ID)) {
            return;
        }

        switch ((view.getId())) {
            // PLAY AD WITH DEFAULT OPTIONS
            case R.id.button_play_ad:
                PlayAd();
                break;

            // PLAY AD WITH CUSTOM OPTIONS
            case R.id.button_play_ad_options:
                PlayAdOptions();
                break;

            // PLAY INCENTIVIZED AD
            case R.id.button_play_ad_incentivized:
                PlayAdIncentivized();
                break;
        }
    }

    private void PlayAd() {
        vunglePub.playAd(DEFAULT_PLACEMENT_ID, null);
    }

    private void PlayAdOptions() {
        // create a new AdConfig object
        final AdConfig overrideConfig = new AdConfig();

        // set any configuration options you like.
        overrideConfig.setOrientation(Orientation.autoRotate);
        overrideConfig.setSoundEnabled(false);
        overrideConfig.setBackButtonImmediatelyEnabled(false);
        //overrideConfig.setExtra1("LaunchedFromNotification");

        // the overrideConfig object will only affect this ad play.
        vunglePub.playAd(DEFAULT_PLACEMENT_ID, overrideConfig);
    }

    private void PlayAdIncentivized() {
        // create a new AdConfig object
        final AdConfig overrideConfig = new AdConfig();

        // set incentivized option on
        overrideConfig.setIncentivizedCancelDialogTitle("Careful!");
        overrideConfig.setIncentivizedCancelDialogBodyText("If the video isn't completed you won't get your reward! Are you sure you want to close early?");
        overrideConfig.setIncentivizedCancelDialogCloseButtonText("Close");
        overrideConfig.setIncentivizedCancelDialogKeepWatchingButtonText("Keep Watching");

        // the overrideConfig object will only affect this ad play.
        vunglePub.playAd(DEFAULT_PLACEMENT_ID, overrideConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    @Override
    protected void onDestroy() {
        // onDestroy(), remove event listeners.
        vunglePub.removeEventListeners(vungleDefaultListener, vungleSecondListener);
        super.onDestroy();
    }
}
