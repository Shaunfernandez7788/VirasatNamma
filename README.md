# Virasat-Namma Guide — Complete Android Studio Project

## 📁 Full Project Structure

```
VirasatNamma/
├── settings.gradle
├── build.gradle                          ← root build file
├── gradle.properties
│
└── app/
    ├── build.gradle                      ← app-level dependencies
    ├── proguard-rules.pro
    │
    └── src/main/
        ├── AndroidManifest.xml
        │
        ├── res/
        │   ├── values/
        │   │   ├── strings.xml
        │   │   ├── themes.xml
        │   │   └── colors.xml
        │   │
        │   ├── drawable/                 ← PLACE IMAGES HERE (see below)
        │   │   ├── site_hampi.jpg
        │   │   ├── site_mysore.jpg
        │   │   ├── site_badami.jpg
        │   │   ├── site_golgumbaz.jpg
        │   │   └── site_belur.jpg
        │   │
        │   └── raw/                      ← PLACE AUDIO HERE (see below)
        │       └── audio_guide.mp3
        │
        └── java/com/example/virasatnamma/
            │
            ├── MainActivity.kt
            │
            ├── data/
            │   ├── model/
            │   │   ├── HeritageSite.kt
            │   │   └── ChatMessage.kt
            │   ├── local/
            │   │   ├── CheckInEntity.kt
            │   │   ├── CheckInDao.kt
            │   │   └── VirasatDatabase.kt
            │   └── repository/
            │       ├── HeritageSiteData.kt
            │       └── HeritageRepository.kt
            │
            ├── ui/
            │   ├── theme/
            │   │   └── Theme.kt
            │   ├── navigation/
            │   │   └── NavGraph.kt
            │   └── screens/
            │       ├── splash/
            │       │   └── SplashScreen.kt
            │       ├── home/
            │       │   └── HomeScreen.kt
            │       ├── detail/
            │       │   └── DetailScreen.kt
            │       ├── scanner/
            │       │   └── ScannerScreen.kt
            │       ├── passport/
            │       │   └── PassportScreen.kt
            │       └── chat/
            │           └── ChatScreen.kt
            │
            └── viewmodel/
                └── HeritageViewModel.kt
```

---

## 🖼️ Required Assets

### Drawable Images (res/drawable/)
Place 5 JPG/PNG images named exactly:
- `site_hampi.jpg`      → photo of Hampi Virupaksha Temple
- `site_mysore.jpg`     → photo of Mysore Palace
- `site_badami.jpg`     → photo of Badami Cave Temples
- `site_golgumbaz.jpg`  → photo of Gol Gumbaz, Bijapur
- `site_belur.jpg`      → photo of Belur Chennakesava Temple

**Quick option:** Download free-use images from Wikimedia Commons or Unsplash for
each site. Rename them exactly as above and drop into `app/src/main/res/drawable/`.

If no images are available, the app will gracefully show a fallback gallery icon
(the `error` parameter in `AsyncImage` handles this automatically).

### Audio File (res/raw/)
- Create a folder `app/src/main/res/raw/` if it doesn't exist.
- Place any `.mp3` file named `audio_guide.mp3` inside it.
- You can use any short free audio clip (e.g. from freesound.org), or record
  a 10-second sample yourself. The file must be named exactly `audio_guide.mp3`.

---

## 🚀 Step-by-Step Setup in Android Studio

### Step 1 — Create Project
1. Open Android Studio (Hedgehog 2023.1.1 or newer recommended).
2. Click **File → New → New Project → Empty Activity**.
3. Set:
   - **Name:** `VirasatNamma`
   - **Package:** `com.example.virasatnamma`
   - **Language:** Kotlin
   - **Min SDK:** API 24
4. Click **Finish** and wait for the initial Gradle sync.

### Step 2 — Replace Gradle Files
Copy the provided files over the auto-generated ones:

| Provided file          | Destination in project           |
|------------------------|----------------------------------|
| `settings.gradle`      | `[project root]/settings.gradle` |
| `build.gradle` (root)  | `[project root]/build.gradle`    |
| `app/build.gradle`     | `[project root]/app/build.gradle`|
| `gradle.properties`    | `[project root]/gradle.properties`|

### Step 3 — Copy res/ Files
Copy `strings.xml`, `themes.xml`, `colors.xml` into `app/src/main/res/values/`.

### Step 4 — Copy AndroidManifest
Replace `app/src/main/AndroidManifest.xml` with the provided one.

### Step 5 — Create Package Structure
In `app/src/main/java/com/example/virasatnamma/`, create these packages
(right-click → New → Package):
- `data.model`
- `data.local`
- `data.repository`
- `ui.theme`
- `ui.navigation`
- `ui.screens.splash`
- `ui.screens.home`
- `ui.screens.detail`
- `ui.screens.scanner`
- `ui.screens.passport`
- `ui.screens.chat`
- `viewmodel`

### Step 6 — Copy All Kotlin Files
Copy each `.kt` file into the matching package as shown in the structure above.

### Step 7 — Add Assets
- Drop image files into `app/src/main/res/drawable/`
- Drop `audio_guide.mp3` into `app/src/main/res/raw/` (create folder if needed)

### Step 8 — Sync & Run
1. Click **File → Sync Project with Gradle Files** (or the elephant icon in toolbar).
2. Wait for sync to complete (first sync downloads dependencies ~200 MB).
3. Connect a physical device or start an emulator (API 24+).
4. Click **Run ▶**.

---

## 📱 QR Code Testing

To test the QR Scanner without a physical QR code:
1. Generate a QR code at https://www.qr-code-generator.com
2. Set the content to one of: `site_001`, `site_002`, `site_003`, `site_004`, or `site_005`
3. Display the QR on another screen and scan it with the app.

Alternatively the app also accepts the format `virasat://site_001`.

---

## 🎨 Features Summary

| Screen      | Key Features                                               |
|-------------|-----------------------------------------------------------|
| Splash      | Animated logo, auto-navigates to Home after 2.4s          |
| Home        | Card list of 5 Karnataka heritage sites with images        |
| Detail      | Hero image, description, audio play/pause, check-in, AI   |
| QR Scanner  | CameraX + ML Kit barcode scanning, permission handling     |
| Passport    | Room DB-backed list of visited sites with timestamps       |
| AI Chat     | Conversational mock AI with context-aware responses        |

---

## ⚠️ Common Issues

| Problem                         | Fix                                                    |
|---------------------------------|--------------------------------------------------------|
| Gradle sync fails               | Check internet. Ensure Android Studio is up to date.  |
| Images not showing              | Verify file names match exactly (case-sensitive).      |
| Audio not playing               | Verify `audio_guide.mp3` is in `res/raw/`.            |
| Camera not working on emulator  | Use a physical device; emulator camera is unreliable. |
| Build error on KSP              | Ensure KSP version matches Kotlin version (1.9.22).   |

---

## 🛠️ Tech Stack Used

- **Language:** Kotlin 1.9.22
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM (ViewModel + Repository)
- **Navigation:** Navigation Compose 2.7.7
- **Database:** Room 2.6.1 with KSP
- **Camera:** CameraX 1.3.2
- **Barcode Scanning:** ML Kit Barcode Scanning 17.2.0
- **Image Loading:** Coil 2.5.0
- **Permissions:** Accompanist Permissions 0.34.0
- **Audio:** Android MediaPlayer (built-in)
