[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/silvzr/PrivateDNSAndroidTV/total)](https://github.com/silvzr/PrivateDNSAndroidTV/releases/latest)
[![GitHub Release](https://img.shields.io/github/v/release/silvzr/PrivateDNSAndroidTV)](https://github.com/silvzr/PrivateDNSAndroidTV/releases/latest)

# Private DNS TV Toggle
An app to easily enable private DNS servers on Android TVs (where the setting is usually hidden) without having to connect to a VPN server or switch to a static IP. Supports any number of providers. Makes it easy to turn adblocking DNS servers on or off with just a single tap.

![Private DNS app screenshot](https://raw.githubusercontent.com/silvzr/PrivateDNSAndroidTV/main/readme.jpg)

## Installation

### GitHub Releases
Get the latest APK on the [releases page](https://github.com/silvzr/PrivateDNSAndroidTV/releases/latest) 

## Activation
To change the system DNS options the app requires `android.permission.WRITE_SECURE_SETTINGS` permission. There are multiple ways to provide it.

### Automatic (Shizuku)
1. Install and start [Shizuku](https://shizuku.rikka.app/).
2. Start the app and allow Shizuku access when prompted.

### Manual
For the app to work properly you'll need to provide it permissions via ADB:

### Computer
1. Get to your PC and download platform tools from Google [here](https://developer.android.com/studio/releases/platform-tools).
2. Extract the tools, and open terminal in the same directory ([Windows guide](https://youtu.be/6vVFmOcIADg?t=38), [macos guide](https://www.howtogeek.com/210147/how-to-open-terminal-in-the-current-os-x-finder-location/)).
3. Turn on USB Debugging on your TV, generally [this video guide](https://youtu.be/Ucs34BkfPB0?t=29) should work on most devices
4. Connect your TV to your PC (adb connect is recommended)
5. Run this command in the terminal
```
./adb shell pm grant ru.karasevm.privatednstoggle.tv android.permission.WRITE_SECURE_SETTINGS
```
6. That's it, app should now work (it has be to installed beforehand for the command to work).

### Android
1. Install Termux from [GitHub](https://github.com/termux/termux-app/releases/latest)
2. Run this command in terminal
```
pkg install android-tools
```
3. Turn on USB Debugging on your TV, generally [this video guide](https://youtu.be/Ucs34BkfPB0?t=29) should work on most devices
4. Connect your TV to your phone (adb connect is recommended)
5. Run this command in the terminal
```
adb shell pm grant ru.karasevm.privatednstoggle.tv android.permission.WRITE_SECURE_SETTINGS
```
6. That's it, app should now work (it has be to installed beforehand for the command to work).

## Testing
To test if the private DNS server was applied correctly I suggest using [this site](https://dnscheck.tools)

## Contributing
Thanks to [karasevm](https://github.com/karasevm) for the app's base and so most of the code.
PRs are welcomed
