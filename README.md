## Getting Started
Vungle's Android SDK V6 Beta

Note: You should always check ad availability by calling canPlayAd method before invoking playAd method. Also you would need to make sure that additional playAd does not get issued before receiving onAdEnd or onError callbacks from the initial playAd call, as ad will not render properly if playAd is repeatedly called in quick succession.
