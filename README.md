# Frida Launcher

**An Android app to easily manage Frida server on your device or emulator**

<div align="center">
  <img width="331" alt="image" src="logo.png" />
</div>

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

## What's New in 1.2

- Refreshed app icons and UI enhancements for a better user experience.
- Added support for custom flags and help dialogs to guide usage.
- Improved logging mechanism and UI responsiveness.

---
## Demo


<div align="center">
  <img src="https://github.com/user-attachments/assets/eee78f07-33b8-4f63-b179-d41a51e1a70b" alt="Project Demo GIF" />
</div>

## Screenshots

### Main Dashboard

<div align="center">
  <img width="331" alt="image" src="https://github.com/user-attachments/assets/8e83df3b-8fc6-49a4-968d-e2939c847c67" />
</div>


### NFC Challenge (Objection Example)

<img width="1000" alt="image" src="https://github.com/user-attachments/assets/de9e564e-f788-4eb3-bda5-54ae9dd0827c" />


### Logs Panel

<div align="center">
<img width="331" alt="image" src="https://github.com/user-attachments/assets/88f44e57-5bf0-40dd-b0e8-e0cdb0f91386" />
</div>

---

## Requirements

- **Android**: Minimum Android version: 7.0 Nougat (API Level 24) or higher
- **USB Debugging**: Enabled on device/emulator
- **Internet**: To download Frida server binaries

---

## Installation

### Download APK

Download the latest `app-debug.apk` from the [Releases page](https://github.com/thecybersandeep/Frida-Launcher/releases/download/v1.2/Frida-Launcher-app-debug.apk) and install with:

```bash
adb install Frida-Launcher-app-debug.apk
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
2. Grant **storage** and **su** permissions when prompted.
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

