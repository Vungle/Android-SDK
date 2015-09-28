## Android Resources

Documentation for our current release can be found [here](./current-release). 

Get started with the dev guide, which will help you integrate and play an ad. Once you've done that, our advanced settings doc will help you customize.

You can find our sample app [here](https://github.com/Vungle/publisher-sample-android). Check out the code, or run it for a demo of our ad experience!

## Changelog

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
