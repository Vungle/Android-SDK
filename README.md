# Vungle's Android SDK

## Getting Started
To get up and running with Vungle, you'll need to [Create an Account With Vungle](https://v.vungle.com/dashboard) and [Add an Application to the Vungle Dashboard](https://support.vungle.com/hc/en-us/articles/210468678)

Once you've created an account you can follow our [Getting Started for Andriod Guide](https://support.vungle.com/hc/en-us/articles/204222794-Get-started-with-Vungle-Android-SDK) to complete the integration. Remember to get the Vungle App ID from the Vungle dashboard.

### Requirements
* Android 3.0 (Honeycomb - API version 11) or later
* If your application is written in C/C++, you'll need to use JNI to interface with the Publisher SDK written in Java
* Java 1.7 - For Android 5.+ compatibility purposes, JDK 7 is required on the development system 

## Release Notes
### 4.0.0
* Added support for interstitial MRAID ads
* Updated `EventListener.onAdEnd()` api to include wasSuccessfulView parameter
* Deprecated `EventListener.onVideoView()` api
* Increased min Android API level to 3.0 (Honeycomb - version 11)
* Removed dependency on support-v4 library and nineoldandroids library

### 3.3.4
* Fixed a bug that might cause a crash on devices with Android 4.2 or lower OS
* Fixed a bug to resume the video ad upon unlocking the screen on devices with screenlock set to none
* Persist sleeps across app restarts

### 3.3.3
* Added support for Android Marshmallow by simplifying the required app permissions


## License
The Vungle Android-SDK is available under a commercial license. See the LICENSE file for more info.
