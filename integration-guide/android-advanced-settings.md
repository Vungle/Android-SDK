# VungleSDK- Android Advanced Settings - v4.0.0

## Please note:

This reference covers the more advanced settings and customizeable aspects of Vungle ads. If you're just getting started, you'll want to check out this [guide](android-dev-guide.md). 

## Advanced Configuration 

### Startup Configuration 

After calling `init` you can optionally get access to the global `AdConfig` object. This object allows you to set options that will be automatically applied to every ad you play.
```java
import com.vungle.publisher.VunglePub;
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.Orientation;

public class FirstActivity extends android.app.Activity {

  ...

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      ...

      vunglePub.init(this, app_id);

      // get a reference to the global AdConfig object
      final AdConfig globalAdConfig = vunglePub.getGlobalAdConfig();

      // set any configuration options you like. 
      // For a full description of available options, see the 'Configuration Options' section.
      globalAdConfig.setSoundEnabled(true);
      globalAdConfig.setOrientation(Orientation.portrait);

  }
}
```

### playAd Configuration 

You can optionally customize each individual ad you play by providing an `AdConfig` object to `playAd`. If you set any options in the global ad configuration (above), those options will be overriden by the provided options.
```java
import com.vungle.publisher.VunglePub;
import com.vungle.publisher.AdConfig;

public class GameActivity extends android.app.Activity {
  ...
  
  private void onLevelComplete() {
  	  // create a new AdConfig object
  	  final AdConfig overrideConfig = new AdConfig();

  	  // set any configuration options you like. 
  	  // For a full description of available options, see the 'Configuration Options' section.
  	  overrideConfig.setIncentivized(true);
  	  overrideConfig.setSoundEnabled(false);

  	  // the overrideConfig object will only affect this ad play.
      vunglePub.playAd(overrideConfig);
  }
}
```

### Configuration Options

#### The `AdConfig` Object

One global `AdConfig` object controls settings for all ad plays, and you can optionally pass override instances to each individual ad play. These are the available setters in `AdConfig`:

| Method | Default | Description |
|:------ |:------- |:----------- |
| `setOrientation` | `Orientation.matchVideo` | `Orientation.autoRotate` indicates that the ad will autorotate with the device orientation. `Orientation.matchVideo` indicates that that the ad will play in the best orientation for the video (usually landscape). |
| `setSoundEnabled` | `true` | Sets the starting sound state for the ad. If `true`, the audio respects device volume and sound settings. If `false`, video begins muted but user may modify. |
| `setBackButtonImmediatelyEnabled` | `false` | If `true`, allows the user to immediately exit an ad using the back button.  If `false`, the user cannot use the back button to exit the ad until the on-screen close button is shown.
| `setImmersiveMode` | `false` | Enables or disables [immersive mode](https://developer.android.com/training/system-ui/immersive.html) on KitKat+ devices |
| `setIncentivized` | `false` | Sets the incentivized mode - you must set this to true if you're using server-to-server callbacks for your rewarded ads. If true, user will be prompted with a confirmation dialog when attempting to skip the ad. If false, no confirmation is shown. Further instructions for setting up incentivized ads are [here](../../Incentivized-Ads). |
| `setIncentivizedUserId` | none | Sets the unique user id to be passed to your application to verify that this user should rewarded for watching an incentivized ad. N/A if ad is not incentivized. |
| `setIncentivizedCancelDialogTitle` | "Close video?" | Sets the title of the confirmation dialog when skipping an incentivized ad. N/A if ad is not incentivized. |
| `setIncentivizedCancelDialogBodyText` | "Closing this video early will prevent you from earning your reward. Are you sure?" | Sets the body of the confirmation dialog when skipping an incentivized ad. N/A if ad is not incentivized. | 
| `setIncentivizedCancelDialogCloseButtonText` | "Close video" | Sets the 'cancel button' text of the confirmation dialog when skipping an incentivized ad. N/A if ad is not incentivized. | 
| `setIncentivizedCancelDialogKeepWatchingButtonText` | "Keep watching" | Sets the 'keep watching button' text of the confirmation dialog when skipping an incentivized ad. N/A if ad is not incentivized. |
| `setExtra1..8` | none | You can use this to keep track of metrics such as age group, gender, etc. |
| `setPlacement` | none | Sets an optional ad placement name for enhanced reporting on the dashboard. |

##### Showing the Close Button

To control whether a user has the option to close out of an ad, use the forced view options in your app's advanced settings on the [Vungle Dashboard](https://v.vungle.com/).

#### The `EventListener` Interface
The Publisher SDK raises several events that you can handle programmatically by implementing `com.vungle.publisher.EventListener` classes and registering/removing them using:

```java
VunglePub.setEventListeners(eventListener1, eventListener2, ...)
```

##### UI Thread Note

The callbacks are executed on a background thread, so any UI interaction/updates resulting from an event callback need to be passed to the main UI thread before executing. Two common ways to run your code on the UI thread are:

* [Handler](http://developer.android.com/reference/android/os/Handler.html)

* [Activity.runOnUiThread(Runnable)](http://developer.android.com/reference/android/app/Activity.html#runOnUiThread(java.lang.Runnable))

```java
import com.vungle.publisher.EventListener;
...

public class FirstActivity extends android.app.Activity {
  ...

  private final EventListener vungleListener = new EventListener(){

	@Deprecated
    @Override
    public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
        // This method is deprecated and will be removed. Please do not use it.
    }

    @Override
    public void onAdStart() {
        // Called before playing an ad
    }

    @Override
    public void onAdEnd(boolean wasSuccessfulView, boolean wasCallToActionClicked) {
        // Called when the user leaves the ad and control is returned to your application
		// if wasSuccessfulView is true, the user watched the ad and should be rewarded (if this was a rewarded ad).
		// if wasCallToActionClicked is true, the user clicked the call to action button in the ad.
    }

    @Override
    public void onAdPlayableChanged(boolean isAdPlayable) {
        // Called when the playability state changes. if isAdPlayable is true, you can now play an ad.
        // If false, you cannot yet play an ad.
    }
    
    @Override
    public void onAdUnavailable(String reason) {
        // Called when VunglePub.playAd() was called, but no ad was available to play
    }
    
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
      ...

      vunglePub.init(this, app_id);
      vunglePub.setEventListeners(vungleListener);

  }
}
```

### Debugging Sleeps

When your app sends a requestAd, it's possible our server will return a sleep. The most common ones are:

| Sleep Time  | Meaning     | Tips        | 
| :---------- | :---------- |:----------- |
| **59** | The server is too busy | You should give it some time and then try again. |
| **305** | No app / Unknown app | This probably means you are using the wrong AppID. Make sure it's the one in red, on your app's page on the dashboard. |
| **1800** | Filters eliminated all options | This could be due to fill, or you may have watched the maximum number of daily ads for your app. You can try putting your app in test mode, so you do not have these limits. |

### Proguard

If you are using Proguard, please ensure the following lines are in your Proguard config file:

`-keep class com.vungle.** { public *; }`

`-keep class javax.inject.*`

`-keepattributes *Annotation*`
