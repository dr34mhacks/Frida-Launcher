# Frida Launcher

**An Android app to easily manage Frida server on your device or emulator**

![frida launcher](https://github.com/user-attachments/assets/3a235958-4212-4eaf-8f26-f742f3227699)


---

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Screenshots](#screenshots)
- [Requirements](#requirements)
- [Installation](#installation)
  - [Download APK](#download-apk)
  - [Build from Source](#build-from-source)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)

---

**Root access required on your device or emulator.**


## Introduction

Frida Launcher is a lightweight Android application that streamlines the installation, launching, and removal of the Frida server binary on your device or emulator. No more manual adb pushes or shell commands—just tap a button to manage your Frida server.


Before Frida Launcher, penetration testers and reverse engineers had to:

Manually push the Frida server binary via adb push, then set executable permissions
Unzip downloaded Frida releases and manage separate versions for each architecture.
Start the server manually (adb shell ./frida-server &) and stop it between sessions.
Uninstall old binaries to avoid conflicts, repeating these steps every time.
These repetitive tasks slow down your workflow and introduce room for error—Frida Launcher automates them with one-tap actions.

---

## Features

- **Device Status**: Detect device architecture (e.g., `arm64`), Frida server installation status, and running state.
- **Version Selection**: Browse and select from all available Frida server releases.
- **One‑Tap Install**: Download and install the chosen Frida server version directly on your device.
- **Server Controls**: Simplified Start, Stop, Uninstall, and Refresh actions.
- **Live Logs**: View real‑time Frida server logs in‑app, with Copy and Clear controls.

---

## Screenshots

### Main Dashboard

![frida launcher](https://github.com/user-attachments/assets/7b19a551-77cc-46dd-86fc-7dc7b0a89f83)


### NFC Challenge (Objection Example)

![fridalauncher objection](https://github.com/user-attachments/assets/7d27b915-b870-46c4-943e-a3a8baffd04c)


### Logs Panel

![logfrida launcher](https://github.com/user-attachments/assets/9113c051-b099-4fff-a85f-5147f90525e4)


---

## Requirements

- **Android**: Minimum Android version: 7.0 Nougat (API Level 24) or higher
- **USB Debugging**: Enabled on device/emulator
- **ADB**: Installed and available in your `PATH`
- **Internet**: To download Frida server binaries

---

## Installation

### Download APK

Download the latest `app-debug.apk` from the [Releases page](https://github.com/thecybersandeep/Frida-Launcher/releases/download/v1.0.0/app-debug.apk) and install with:

```bash
adb install app-debug.apk
```

### Build from Source

```bash
# Clone this repository
git clone https://github.com/thecybersandeep/Frida-Launcher.git
cd frida-launcher

# Open in Android Studio
gradlew clean assembleDebug
# or build via command line
gradlew installDebug
```

---

## Usage

1. **Launch** the app on your device/emulator.
2. Grant **storage** and **adb shell** permissions when prompted.
3. Tap **Refresh** to detect existing Frida installations.
4. Select the desired Frida server version.
5. Tap **Install** → **Start**.
6. Use **Stop** or **Uninstall** as needed.
7. View logs in the **Logs** panel; copy or clear logs with the provided buttons.

---

## Contributing

Contributions, issues, and feature requests are welcome!

---

## Acknowledgements

- [Frida](https://frida.re) for the powerful instrumentation toolkit

---

*Happy hacking!*

