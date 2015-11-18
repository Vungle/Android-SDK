package com.vungle.publisherSampleAndroid;

import android.os.Bundle;
import android.app.Activity;
import android.os.Debug;
import android.util.Log;
import android.view.View.OnClickListener;

import com.vungle.publisher.Orientation;
import com.vungle.publisher.VunglePub;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;

import com.vungle.publisher.EventListener;
import com.vungle.publisher.AdConfig;

public class MainActivity extends Activity implements OnClickListener {

	// get the VunglePub instance
	final VunglePub vunglePub = VunglePub.getInstance();

	// buttons
	private ImageButton buttonPlayAd;
	private ImageButton buttonPlayAdOptions;
	private ImageButton buttonPlayAdIncentivized;

	// constant string for app id
	private final static String APP_ID = "56423fc5ae9355e074000016";
	// private final static String APP_ID = "Test App";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get your App ID from the app's main page on the Vungle Dashboard after setting up your app
		final String app_id = APP_ID;

		// initialize the Publisher SDK
		vunglePub.init(this, app_id);

		// registering multiple eventlistners.
		vunglePub.setEventListeners(vungleDefaultListener, vungleSecondListener);

		// initialize buttons
		buttonPlayAd             = (ImageButton) findViewById(R.id.button_play_ad);
		buttonPlayAdOptions      = (ImageButton) findViewById(R.id.button_play_ad_options);
		buttonPlayAdIncentivized = (ImageButton) findViewById(R.id.button_play_ad_incentivized);

		// attach listener to buttons
		buttonPlayAd.setOnClickListener(this);
		buttonPlayAdOptions.setOnClickListener(this);
		buttonPlayAdIncentivized.setOnClickListener(this);
	}

	private final EventListener vungleDefaultListener = new EventListener() {
		@Override
		public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
			// Called each time a video completes.  isCompletedView is true if >= 80% of the video was watched.
		}

		@Override
		public void onAdStart() {
			// Called before playing an ad.
		}

		@Override
		public void onAdUnavailable(String reason) {
			// Called when VunglePub.playAd() was called but no ad is available to show to the user.
		}

		@Override
		public void onAdEnd(boolean wasCallToActionClicked) {
			// Called when the user leaves the ad and control is returned to your application.
		}

		@Override
		public void onAdPlayableChanged(boolean isAdPlayable) {
			// Called when ad playability changes.
			Log.d("DefaultListner", "This is a default eventlistner.");
		}
	};

	private final EventListener vungleSecondListener = new EventListener() {
		@Override
		public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
			// Called each time a video completes.  isCompletedView is true if >= 80% of the video was watched.
		}

		@Override
		public void onAdStart() {
			// Called before playing an ad.
		}

		@Override
		public void onAdUnavailable(String reason) {
			// Called when VunglePub.playAd() was called but no ad is available to show to the user.
		}

		@Override
		public void onAdEnd(boolean wasCallToActionClicked) {
			// Called when the user leaves the ad and control is returned to your application.
		}

		@Override
		public void onAdPlayableChanged(boolean isAdPlayable) {
			// Called when ad playability changes.
			Log.d("SecondListner", "This is a second eventlistner.");
		}
	};

	public void onClick(View view) {
		// Check if Vungle Ad is available
		if(!vunglePub.isAdPlayable()) {
			return;
		}

		switch ((view.getId())){
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
		vunglePub.playAd();
	}

	private void PlayAdOptions() {
		// create a new AdConfig object
		final AdConfig overrideConfig = new AdConfig();

		// set any configuration options you like.
		overrideConfig.setOrientation(Orientation.matchVideo);
		overrideConfig.setSoundEnabled(false);
		overrideConfig.setBackButtonImmediatelyEnabled(false);
		overrideConfig.setPlacement("StoreFront");
		//overrideConfig.setExtra1("LaunchedFromNotification");

		// the overrideConfig object will only affect this ad play.
		vunglePub.playAd(overrideConfig);
	}

	private void PlayAdIncentivized() {
		// create a new AdConfig object
		final AdConfig overrideConfig = new AdConfig();

		// set incentivized option on
		overrideConfig.setIncentivized(true);
		overrideConfig.setIncentivizedCancelDialogTitle("Careful!");
		overrideConfig.setIncentivizedCancelDialogBodyText("If the video isn't completed you won't get your reward! Are you sure you want to close early?");
		overrideConfig.setIncentivizedCancelDialogCloseButtonText("Close");
		overrideConfig.setIncentivizedCancelDialogKeepWatchingButtonText("Keep Watching");

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
		// onDestroying objects, remove eventlistners.
		vunglePub.removeEventListeners(vungleDefaultListener, vungleSecondListener);
		super.onDestroy();
	}
}
