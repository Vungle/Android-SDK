## Changelog

### VERSION 6.12.0 (August 2, 2022)
* New ad format: Rewarded Interstitial (closed beta)
* In-app bidding enhancements
* Performance optimizations & bug fixes

### VERSION 6.11.0 (May 3, 2022)
* Added support for Native Ads format for non-HB placements (closed beta)
* In-app bidding enhancements
* Support of deep-linking URLs into other apps

### VERSION 6.10.5 (March 10, 2022)
* Fixed a random crash owing to a synchronization issue around handling Gson lib JsonObject.

### VERSION 6.10.4 (February 9, 2022)
* Updated SDK to be fully compliant with [Google Privacy Policies](https://developer.android.com/about/versions/12). 
* COPPA API is now generally available.
* Stability Improvements.

### VERSION 6.10.3 (December 9, 2021)
* Added support for Google’s  [Android 12](https://developer.android.com/about/versions/12).
* SDK now supports Android  [Target API 31](https://developer.android.com/sdk/api_diff/31/changes).
* Added the framework for COPPA API, which can be used as an additional tool to assist publishers with obligations under COPPA. This API is in Beta release, and behavior might change in future versions of SDK.

### VERSION 6.10.2 (September 1, 2021)
* Fixed low occurring `ConcurrentModificationException` crash

### VERSION 6.10.1 (August 5, 2021)
* Moved to mavenCentral repository
  * SDK artifacts for gradle integration now distributed through mavenCentral, using the same maven artifactID
* Increased minimum supported API level to 21
* Addition of creativeId event to PlayAdCallback
* Stability improvements
#### Mediation In-App Bidding (Closed Beta)
* Banner / MREC Bidding Support in Bidding 
* Caching enhancements for in-app bidding Placements
* New SDK APIs for in-app bidding (only applicable to mediation partners)

### VERSION 6.9.1 (January 21, 2021)
* OM SDK Integration - The Open Measurement Software Development Kit (OM SDK) is designed to facilitate third-party viewability and verification measurement for ads served by Vungle
* Stability improvements

### VERSION 6.8.1 (October 14, 2020)
* onAdViewed - new callback introduced to more accurately track ad impressions
* Stability improvements

### VERSION 6.8.0 (September 15, 2020)
* Android 11 Support (Since 6.7.1)
* Removed Flex Feed and Flex View formats
* Stability improvements

### VERSION 6.7.1 (August 17, 2020)
* Support for Android 11
  * Explicitly enabled WebSettings.setAllowFileAccess to improve video rendering in WebView 
  * Improvements to handling Intent.resolveActivity that could fail requiring new Android 11 permissions that could affect click of ads
* Gson updated to 2.8.6
* Okhttp updated to 3.12.12
* Logging-interceptor no longer required
* Various bug fixes

### VERSION 6.7.0 (June 22, 2020)
* CCPA Support – Compliance with new government regulations while assuring your users their personal data is being used the way they want
* Multiple banner ad support – Display multiple banners on the screen to leverage more ways to monetize your inventory and attract Premium Buyers
* Upgrade moat v2.6.3 – To enhance your analytics and improve strategic decisions based on data
* Improved real-time ad play callbacks for click and rewarded events for a better end-user experience
* Removed Retrofit library to now use GSON 2.8.
* Backend performance improvements to continually optimize your monetization efforts
* Various bug fixes

### VERSION 6.5.3 (March 24, 2020)
* Bugs and issues resolved

### VERSION 6.5.2 (February 14, 2020)
* Programmatic Demand Banner Support
* Bug Fixes

### VERSION 6.5.1 (January 30, 2020)
* Included new Target API 29 (Android 10) and AndroidX support (including dependencies based on AndroidX )
* Optimized caching again to reduce data usage
* Added addition support for new banner sizes (320x50, 300x50, 728x90)
* MOAT viewability tracking fix
* Improved ad playback on Android 7 and below
* Fixed memory leaks
* Made background webview of banner transparent
* Added annotations to lower size of SDK using Proguard
* Improved how our SDK handles orientation settings and changes
* Various bugs squashed and stability improvements

### VERSION 6.5.0
* Android 10 support and build for API 29
* AndroidX support
* Introduction of Banner Format
* Continuous improvements to ad caching
* Fixed an issue with Moat tracker
* Critical bug-fixes

### VERSION 6.4.11
* Cache Optimization — Automatically optimizes ad caching to ensure ads are available faster. No additional work from developer needed
* Introduced new MREC Video placement type to serve higher performing banners
* Privacy by Design — Removed latitude and longitude collection to protect users
* Updated Moat to no longer collect location data
* Removed Fetch library dependency
* Publisher controls to override minimum disk requirements, helping to ensure good user experiences

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

### VERSION 3.3.0

* Added support for latest Google Play Services (6.5.87+)
* The Vungle SDK can now be integrated without Google Play Services if desired
* Changed the `VunglePub` public API
  * Added support for multiple EventListeners
    * Renamed `setEventListener()` to `setEventListeners()`
    * Added `addEventListeners()`, `clearEventListeners()`, and `removeEventListeners()`
  * Renamed `isCachedAdAvailable()` to `isAdPlayable()`
* Renamed `EventListener.onCachedAdAvailable()` to `EventListener.onAdPlayableChanged()`
* `VunglePub.isAdPlayable()` and `EventListener.onAdPlayableChanged()` take into account minimum mdelays between ads and other conditions which might prevent an ad from being played if requested.  Previously, `VunglePub.isCachedAdAvailable()` and `EventListener.onCachedAdAvailable()` did not take these factors into account.
* Ad close now enabled during and after incentivized alert dialog
* Improved local caching performance
* Improved cached ad retrieval logic and timing
* Improved network request handling
  * Improved notifications around failed network requests
  * Improved logic and timing of queued network requests

### VERSION 3.2.2

* new 'adaptiveId' that uses [Android Advertising ID](https://developer.android.com/google/play-services/id.html) for attribution if available, otherwise falls back to [Android ID](http://developer.android.com/reference/android/provider/Settings.Secure.html#ANDROID_ID) and wifi MAC address (if available)
* fixed a bug with extra `EventListener.onAdEnd()` notifications

### VERSION 3.2.1

* fixed Vungle `User-Agent` for requests to Vungle servers
* use browser `User-Agent` for requests to non-Vungle servers
* prevented video exit buttons from being clicked multiple times
* fixed ads not playing under certain conditions in `singleTask` `Activity` launch mode

### VERSION 3.2.0 

* changes to maintain server-determined order for ads
* added `AdConfig.setImmersiveMode()` to enable immersive mode in KitKat+ devices (default is `false`, which is a change from versions 3.1.1 and prior)
* added parameter to callback `EventListener.onAdEnd(boolean wasCallToActionClicked)` to indicate whether the user clicked the call-to-action (usually 'Download') button (breaks backwards compatibility)
* modified `VunglePub.init()` to return a `boolean` indicating whether intialization was successful rather than `void`
* added missing callbacks to `EventListener.onAdUnavailable()` in a few rare circumstances
* unbundled libraries as separate, required jars: `dagger-[version].jar` and `nineoldandroids-[version].jar`
* added Javadoc for `AdConfig`

### VERSION 3.1.1

* added geolocation support in apps with `ACCESS_COARSE_LOCATION` or `ACCESS_FINE_LOCATION` permission
* fixed bug where ads would stop playing until app was restarted
* fixed bug in reporting streaming ads
* fixed bug where some ad progress messages were not being sent
* fixed bug where the ad report of the currently playing ad could be deleted
* reduced delay between `VunglePub.init()` and initial ad request from 3 seconds to 2 seconds
* delete old version 1.x.x cache directory if it exists
* hid some debug logging messages that were being shown in production mode 

### VERSION 3.1.0 

* added support for [Android Advertising ID](https://developer.android.com/google/play-services/id.html)
* removed references to `android.provider.Settings.Secure.ANDROID_ID`
* added `AdConfig.setPlacement()` for tracking ad performance by placement location
* added `AdConfig.setExtra1-8()` for passing developer-defined key-value pairs
* added 3 second delay between `VunglePub.init()` and initial ad request to allow for global `AdConfig` configuration
* removed deprecated methods `AdConfig.setShowClose()` and `AdConfig.isShowClose()` (please confugre these from the [Vungle Dashboard](https://v.vungle.com))
* fixed `Activity` and `Fragment` recreation if they are destroyed while in the background
* fixed sound bug with ads starting muted
* fixed bugs affecting session length calculation 
* added `logcat` warning messages for missing `AndroidManifest.xml` permissions and config
