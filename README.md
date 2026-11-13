# Creta Style Launcher — Android 10 / Android Studio project

An OEM-inspired landscape launcher configured for Android 10 (API 29) and a 9-inch Android head unit such as the Blaupunkt Key Largo 980. It combines a Creta-inspired dark dashboard layout with Android shortcuts.

## Included
- Landscape home dashboard designed to scale to common 9-inch head-unit resolutions
- Clock/date, navigation shortcut, music-app shortcut, phone dialer, Bluetooth settings and Android settings
- Launcher/Home intent so it can be selected as the default home app
- Dark, large-touch-target interface

## Important limitations
- This is an original Creta-inspired interface, not Hyundai's proprietary software or an exact pixel-for-pixel copy.
- Climate controls, vehicle settings, steering-wheel buttons, reverse camera, vehicle data and OEM vehicle integration only work if the head unit/car exposes a compatible API or hardware interface. This project does not control those functions.
- Music playback controls shown are shortcuts/visual controls; actual playback metadata and next/previous commands need a compatible media-session integration.
- The Maps shortcut opens Google navigation. Install Google Maps for best results.
- Some head-unit Android builds restrict default launcher selection or background behavior.

## Build APK
1. Install Android Studio on a computer.
2. Open this folder as an existing project.
3. Allow Gradle sync to finish (Android Studio will download required dependencies).
4. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
5. Find the APK under `app/build/outputs/apk/debug/`.

## Install on the head unit
1. Copy the APK to a USB drive or transfer it to the head unit.
2. Enable installation from the file manager if the unit permits it.
3. Install the APK.
4. Press Home and select **Creta Style Launcher**. If offered, choose Always.

## Android 10 compatibility
The project uses minSdk 29 and targetSdk 29 for Android 10-oriented compatibility. It still needs to be built and tested on the actual Blaupunkt unit; launcher selection and app behavior can vary by head-unit firmware.

## Target display: 1024×600
The launcher is locked to landscape and designed to scale to your 1024×600 Blaupunkt screen. The layout uses flexible columns and weighted panels, but must still be tested on the actual unit for safe-area, status/navigation bar and touch-target fit.

## Easiest phone-only build (GitHub Actions)
This project includes `.github/workflows/build-apk.yml` so GitHub can build the APK for you in the cloud.

1. On your phone, open https://github.com and sign in or create a free account.
2. Tap **+** → **New repository**. Name it `creta-oem-launcher`, choose **Public** (or Private if you prefer), then create it.
3. Open the new repository and choose **Add file** → **Upload files**. Upload the contents of this ZIP (extract it first using your phone's Files app; include the `.github` folder). Commit the upload to `main`.
4. Open the repository's **Actions** tab. Choose **Build Creta OEM Launcher APK** and tap **Run workflow** if it hasn't started automatically.
5. Wait for the run to finish with a green check. Open that run, scroll to **Artifacts**, and tap **Creta-OEM-Launcher-APK** to download the ZIP containing `app-debug.apk`.
6. Extract the downloaded ZIP and tap `app-debug.apk` to install it. If Android asks, allow your browser or Files app to install unknown apps.

Note: this produces a debug APK for testing, not a Play Store release. Keep the GitHub repository private if you do not want your source code public. Do not share your GitHub password or verification codes.
