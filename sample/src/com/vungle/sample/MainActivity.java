package com.vungle.sample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.vungle.publisher.AdConfig;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.Orientation;
import com.vungle.publisher.VunglePub;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {
	// get the VunglePub instance
	final VunglePub vunglePub = VunglePub.getInstance();

    private static final String TAG = "VunglePubSample";

    @InjectView(R.id.etAppId)
    EditText etAppId;
    @InjectView(R.id.btInit)
    Button btInit;
    @InjectView(R.id.etUserTag)
    EditText etUserTag;
    @InjectView(R.id.cbIncentivized)
    CheckBox cbIncentivized;
    @InjectView(R.id.cbStartMuted)
    CheckBox cbStartMuted;
    @InjectView(R.id.cbDisableTransitionAnimation)
    CheckBox cbDisableTransitionAnimation;
    @InjectView(R.id.cbEnableBack)
    CheckBox cbEnableBack;
    @InjectView(R.id.etPlacement)
    EditText etPlacement;
    @InjectView(R.id.btPlayAdWithOptions)
    ImageButton btPlayAdOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize UI elements
        ButterKnife.inject(this);

        setButtonState(btPlayAdOptions, false);

		// attach listener to buttons
        btInit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize the Publisher SDK
                String appId = etAppId.getText().toString();
                Log.d(TAG, "Initializing with app ID = " + appId);
                vunglePub.init(getApplicationContext(), appId);
                vunglePub.setEventListeners(vungleDefaultListener, vungleSecondListener);
                btInit.setEnabled(false);
                etAppId.setEnabled(false);
            }
        });
		btPlayAdOptions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if Vungle Ad is available and Play Ad with selected options
                if(vunglePub.isAdPlayable()) {
                    Log.d(TAG, "Playing Ad!!!");
                    PlayAdOptions();
                } else {
                    Log.e(TAG, "Ad is not playable!!!");
                }
            }
        });
	}

	private void setButtonState(ImageButton button, boolean enabled) {
		button.setEnabled(enabled);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			button.setAlpha(enabled ? 1.0f : 0.2f);
		}
	}

	private final EventListener vungleDefaultListener = new EventListener() {
		@Deprecated
		@Override
		public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
			// This method is deprecated and will be removed. Please use onAdEnd() instead.
		}

		@Override
		public void onAdStart() {
			// Called before playing an ad.
		}

		@Override
		public void onAdUnavailable(String reason) {
			// Called when VunglePub.playAd() was called but no ad is available to show to the user.
            Log.e(TAG, "onAdUnavailable() - " + reason);
		}

		@Override
		public void onAdEnd(boolean wasSuccessfulView, boolean wasCallToActionClicked) {
			// Called when the user leaves the ad and control is returned to your application.
		}

		@Override
		public void onAdPlayableChanged(boolean isAdPlayable) {
			// Called when ad playability changes.
            Log.d(TAG, "onAdPlayableChanged to " + isAdPlayable);
			final boolean enabled = isAdPlayable;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// Called when ad playability changes.
					setButtonState(btPlayAdOptions, enabled);
				}
			});
		}
	};

	private final EventListener vungleSecondListener = new EventListener() {
		// Vungle SDK allows for multiple listeners to be attached. This secondary event listener is only
		// going to print some logs for now, but it could be used to Pause music, update a badge icon, etc.
		@Deprecated
		@Override
		public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {}

		@Override
		public void onAdStart() {}

		@Override
		public void onAdUnavailable(String reason) {}

		@Override
		public void onAdEnd(boolean wasSuccessfulView, boolean wasCallToActionClicked) {}

		@Override
		public void onAdPlayableChanged(boolean isAdPlayable) {}
	};

    private void PlayAdOptions() {
        // create a new AdConfig object
        final AdConfig overrideConfig = new AdConfig();

        // set configuration options for ad play.
        overrideConfig.setOrientation(Orientation.matchVideo);
        overrideConfig.setSoundEnabled(!cbStartMuted.isChecked());
        overrideConfig.setBackButtonImmediatelyEnabled(cbEnableBack.isChecked());
        overrideConfig.setPlacement(etPlacement.getText().toString());
        overrideConfig.setTransitionAnimationEnabled(!cbDisableTransitionAnimation.isChecked());

        if (cbIncentivized.isChecked()) {
            // set incentivized option on
            overrideConfig.setIncentivized(true);
            overrideConfig.setIncentivizedUserId(etUserTag.getText().toString());
            overrideConfig.setIncentivizedCancelDialogTitle("Careful!");
            overrideConfig.setIncentivizedCancelDialogBodyText("If the video isn't completed you won't get your reward! Are you sure you want to close early?");
            overrideConfig.setIncentivizedCancelDialogCloseButtonText("Close");
            overrideConfig.setIncentivizedCancelDialogKeepWatchingButtonText("Keep Watching");
        }

        // the overrideConfig object will only affect this ad play.
        vunglePub.playAd(overrideConfig);
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
		// onDestroy(), remove eventlisteners.
		vunglePub.removeEventListeners(vungleDefaultListener, vungleSecondListener);
		super.onDestroy();
	}
}
