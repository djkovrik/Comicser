## Udacity Android Developer Nanodegree Program - Capstone project

### Rubric

#### Common Project Requirements
- [x] App conforms to common standards found in the [Android Nanodegree General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html)
- [x] App is written solely in the Java Programming Language

#### Core Platform Development
- [x] App integrates a third-party library.
- [x] App validates all input from servers and users. If data does not exist or is in the wrong format, the app logs this fact and does not crash.
- [x] App includes support for accessibility. That includes content descriptions, navigation using a D-pad, and, if applicable, non-audio versions of audio cues.
- [x] App keeps all strings in a strings.xml file and enables RTL layout switching on all layouts.
- [x] App provides a widget to provide relevant information to the user on the home screen.

#### Google Play Services
- [x] App integrates two or more Google services. Google service integrations can be a part of Google Play Services or Firebase.
- [x] Each service imported in the build.gradle is used in the app.
- [x] If Analytics is used, the app creates only one analytics instance.

#### Material Design
- [x] App theme extends AppCompat.
- [x] App uses an app bar and associated toolbars.
- [x] App uses standard and simple transitions between activities.

#### Building
- [x] App builds from a clean repository checkout with no additional configuration.
- [x] App builds and deploys using the installRelease Gradle task.
- [x] App is equipped with a signing configuration, and the keystore and passwords are included in the repository. Keystore is referred to by a relative path.
- [x] All app dependencies are managed by Gradle.

#### Data Persistence
- [x] App stores data locally either by implementing a ContentProvider OR using Firebase Realtime Database. No third party frameworks nor Room Persistence Library may be used.
- [x] If it regularly pulls or sends data to/from a web service or API, app updates data in its cache at regular intervals using a SyncAdapter or JobDispacter.
- [x] App uses a Loader to move its data to its views.
