# publisher-sample-android

## Introduction

This sample app shows how to integrate the Vungle Publisher SDK into an Android application.

Supported IDEs:
* [Android Studio](#androidstudio)
* [Eclipse](#eclipse)

Supported build systems:
* [Gradle](#gradle)
* [Ant](#ant)

## <a name="androidstudio"></a>Importing into Android Studio

0. Choose `Import Project...` and navigate to the `publisher-sample-android` directory.

  Done!

## <a name="eclipse"></a>Importing into Eclipse

0. Import `publisher-sample-android` using:

  ```
  File > Import... > Android > Existing Android Code Into Workspace
  ```

0. The project will have some errors, including one like this:

  ```
  Error: No resource found that matches the given name (at 'value' with value '@integer/google_play_services_version').
  ```

0. If you haven't already, import the Google Play Services library project into Eclipse:

  ```
  File > Import... > Android > Existing Android Code Into Workspace
  ```
  Navigate to:

  ```
  <android-sdk>/extras/google/google_play_services/libproject/google-play-services_lib
  ```
0. In `publisher-sample-android`, go to: 

  ```
  Project > Properties > Android
  ```
  Add the Google Play Services project as a library.
0. Clean and rebuild `publisher-sample-android`.  This should fix the project errors.

## <a name="gradle"></a>Building with Gradle

0. Build the sample app using:

  ```
  ./gradlew clean build`
  ```
0. To see a list of available tasks, execute:

  ```
  ./gradlew tasks
  ```

## <a name="ant"></a>Building with Ant

0. Build the sample app using:

  ```
  ant clean debug
  ```
0. To see a list of available tasks, execute:

  ```
  ant help
  ```

### Note
If you downloaded the the sample app from [GitHub](http://github.com/Vungle/publisher-sample-android), you'll need to download the Publisher SDK from the [Vungle Dashboard](https://v.vungle.com) and add it to the `libs` directory.
