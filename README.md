## Getting Started
Please refer to https://support.vungle.com/hc/en-us/articles/360002922871


## Release Notes

### VERSION 6.3.24
* Android 9 Pie is now supported
* Lighter SDK with a lower method count and fewer external 3rd party dependencies
* Programmatic ad performance has been improved to address stability issues
* Improvements in runtime memory usage by SDK
* Custom Creative performance improvements

### VERSION 6.3.17
* Updates for improved performance

### VERSION 6.3.12
* Optional placement list during initialization, support for zero auto-cached Placement
* Removal of Evernote and related transitive dependencies
* Stability fixes

### VERSION 6.2.5
* GDPR compliance
* License update
* Redesigned SDK to lower method count and SDK size
* Reduced time needed to initialize and play a cached Ad
* Deprecated generic callback and introduced API specific callbacks to simplify integration. Please refer Migration section of V6 SDK documentation

### VERSION 5.3.2
* Sleep code to be enforced at placement level
* Ability to close Flex-View ads through API and timer
* Ordinal data reporting
* Bug fixes and performance improvements

### VERSION 5.3.0
* Added support for Amazon appstore/platform and Amazon Advertising ID
* Android Oreo compatibility.
* Ability to detect and target Apps for ‘Allow unknown sources’ on devices
  to enable sideload of apk's
* Bug-fixes for Stability

### VERSION 5.1.0
* Launched new Placements feature.
* Added Native Flex View ad unit.
* Added Moat Viewability technology to provide viewership data on ads.

### VERSION 4.0.3
* Added support for Android Nougat (Android v7.0)
* Reporting more device stats to serve better and better ads
* Upgraded to Dagger 2.7
* Added wrapper-framework values for admob, dfp, heyzap, mopub
* Integrated RxJava architecture for ad preparation

### VERSION 4.0.2
* Fixed the device ID timeout when play-services is not included
* Migrate to Dagger 2
* Cleaned up all the Proguard filters that are not required after Dagger 2 migration
* Fixed a few minor Unity bugs
* Handling SSL errors better
* Developers are warned when invalid App ID is used to initialize
* Added support for interstitial MRAID ads
* Updated `EventListener.onAdEnd()` api to include wasSuccessfulView parameter
* Deprecated `EventListener.onVideoView()` api
* Increased min Android API level to 3.0 (Honeycomb - version 11)
* Removed dependency on support-v4 library and nineoldandroids library

### VERSION 3.3.5
* Added support for Dagger 2
* Increased minimum supported Android SDK level from API 9 to API 11
* Upgraded maximum Google Play Services version to 8.3.0
* Removed dependencies on support-v4 library and nineoldandroids library
* Fixed black screen issue with Unity plugin

### VERSION 3.3.4
* Fixed a bug that might cause a crash on devices with Android 4.2 or lower OS
* Fixed a bug to resume the video ad upon unlocking the screen on devices with screenlock set to none
* Persist sleeps across app restarts

### VERSION 3.3.3
* Added support for Android Marshmallow by simplifying the required app permissions

### VERSION 3.3.2
* Fixed a bug that might cause ad to crash when returning to foreground

### VERSION 3.3.1

* Improved event bus performance
* Added support for remote error reporting
* Added support for installed app data collection
* Updated incentivized dialogs to use the default application theme
* Added link to Vungle's privacy policy in the ad experience
* Replaced the countdown timer with a progress bar timer
* Fixed some minor bugs
