# VungleSDK- Android Developer Guide - v4.0.0

This guide will show you how you can easily integrate our SDK into your app so you can start monetizing!

### Requirements

* Android 3.0 (Honeycomb - API version 11) or later
* If your application is written in C/C++, you'll need to use JNI to interface with the Publisher SDK written in Java

### Here are a few important tips:

* If you haven't already done so, head over to our [dashboard](https://v.vungle.com/dashboard/login) and add your app to your account. You need to do this so that you can get your App ID that you’ll be adding to your app with our SDK. It’s in **red** on your app’s page.

* If you’d rather just jump right in with our sample app, see the `sample` folder [here](https://github.com/Vungle/Android-SDK). 

* If you’re using **Adobe Air**, **Unity**, or **Corona**, check out our [plugins page](https://v.vungle.com/dev/plugins).

Otherwise, read on!

## 1. Download the SDK

Next, head [here](https://v.vungle.com/dev/android) to download our SDK. Unzip it.

## 2. Add VungleSDK To Your Project

Copy all of the libraries from the unzipped `/libs` directory into your project's `/libs` directory.  Create this directory if it doesn't already exist in your project.  This should automatically add the libraries to the build path of your project.

This should include the following libraries:
* `dagger-[version].jar`
* `javax.inject-[version].jar`
* `vungle-publisher-[version].jar`

If you already including the same versions of any of the above libraries, you don't need to include them again. If you are using a different version of any of the libraries, please test your app to determine which version works best.

## 4. Update `AndroidManifest.xml`

Add the following lines to your AndroidManifest.xml:

```xml
<manifest>

  ...
  
  <!-- permissions to download and cache video ads for playback -->
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  
  <application>
  
    ...
    
    <!--
      Required Activities for playback of Vungle video ads
    -->
    <activity android:name="com.vungle.publisher.VideoFullScreenAdActivity"
      android:configChanges="keyboardHidden|orientation|screenSize"
      android:theme="@android:style/Theme.NoTitleBar.Fullscreen"/>
    <activity android:name="com.vungle.publisher.MraidFullScreenAdActivity"
      android:configChanges="keyboardHidden|orientation|screenSize"
      android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"/>
    
  </application>
  
</manifest>
```

### Google Play Services (Optional)
Including Google Play Services with your project will allow Vungle to provide a more customized ad experience to the end-user, but it's not required.  We recommend using the current version from Google.

http://developer.android.com/google/play-services/setup.html#Setup

3) In your app, ensure that the device has a sufficiently up-to-date version of Google Play Services:

http://developer.android.com/google/play-services/setup.html#ensure

[Vungle's Google Play Services FAQs](http://www.vungle.com/google-advertising-id-faqs/)

## 5. Initialize & Integrate the SDK

### Application Startup

Initialize the Publisher SDK in your application's first `Activity`. This starts video caching and prepares the SDK to display ads.
```java
import com.vungle.publisher.VunglePub;

public class FirstActivity extends android.app.Activity {

  // get the VunglePub instance
  final VunglePub vunglePub = VunglePub.getInstance();

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      // get your App ID from the app's main page on the Vungle Dashboard after setting up your app
      final String app_id = "your Vungle App ID";
      
      // initialize the Publisher SDK
      vunglePub.init(this, app_id);
  }
}
```

### Each Activity

In addition, override the `onPause` and `onResume` methods in each `Activity` (including the first) to notify the Publisher SDK when your application gains or loses focus:
```java
import com.vungle.publisher.VunglePub;

public class EachActivity extends android.app.Activity {

  // get the VunglePub instance
  final VunglePub vunglePub = VunglePub.getInstance();

  ...
  
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
}
```

## 6. Play an Ad!

### Default Configuration

Almost done! When you want to actually play the ad in your application, simply call `playAd`
```java
import com.vungle.publisher.VunglePub;

public class GameActivity extends android.app.Activity {

  // get the VunglePub instance
  final VunglePub vunglePub = VunglePub.getInstance();

  ...
  
  private void onLevelComplete() {
      vunglePub.playAd();
  }
}
```

**Tip-** If you'd like to check if an ad is available before playing, use:
```java
// indicates if an ad is ready to play
vunglePub.isAdPlayable()
```

That's it! Quick start guide complete. Stick around if you'd like to check out some of the ways you can customize the ad experience, but otherwise go forth and monetize!

<a name="advancedStartupConfig"></a>
## Advanced Settings

Check out our [advanced settings](./android-advanced-settings.md) for instructions on ad customization, debugging, and event callbacks!
