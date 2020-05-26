![Docker Image CI](https://github.com/JusticeInternational/RedSol-android/workflows/Docker%20Image%20CI/badge.svg?branch=master)
# Introduction


## Development

### Some VS Code Extensions
- Kotlin
- Kotlin Language
- Code Runner
- Task Explorer
- Gradle Language Support

### Install Some Tools
- Install Homebrew
- Update your tools with ; `brew update && brew bundle`
- Install [Android SDK](https://developer.android.com/docs)
- 
### Build The Project
- Setup Android SDK 
    ```shell
    sdkmanager --update
    sdkmanager "platforms;android-25" "build-tools;25.0.2" "extras;google;m2repository" "extras;android;m2repository"
    sdkmanager --licenses
    export ANDROID_SDK_ROOT=/usr/local/Caskroom/android-sdk/4333796
    export PATH=$PATH:$ANDROID_SDK_ROOT/tools
    ```
- Build 
  `gradle build`
  or
  `docker build -t redsol .`
