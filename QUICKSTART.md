# StudyLock - Quick Start Guide

This guide will help you get the StudyLock Android app up and running quickly.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Version 17
- **Android SDK**: API 34 (Android 14)
- **Git**: For cloning the repository

## Step 1: Clone the Repository

```bash
git clone <repository-url>
cd StudyLock
```

## Step 2: Open in Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned `StudyLock` directory
4. Click "OK"

Android Studio will automatically sync Gradle and download dependencies. This may take a few minutes.

## Step 3: Configure Firebase

### Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project"
3. Enter project name and follow the setup wizard

### Add Android App

1. In Firebase Console, click the Android icon
2. Enter package name: `com.studylock.app`
3. Download `google-services.json`
4. Place it in `app/` directory (replace the existing placeholder)

### Enable Required Services

In Firebase Console, enable:

1. **Authentication**
   - Email/Password provider
   - Google provider
   - Phone provider

2. **Realtime Database**
   - Create database in production mode

3. **Cloud Messaging**
   - Get your Server Key and Sender ID

4. **Crashlytics**
   - Enable crash reporting

## Step 4: Configure Supabase

### Create Supabase Project

1. Go to [Supabase Dashboard](https://supabase.com/dashboard)
2. Click "New Project"
3. Enter project details and create

### Get Configuration Details

1. In your project dashboard, go to Settings → API
2. Copy:
   - Project URL
   - anon public key

### Create Database Tables

Run the SQL from the PRD to create the following tables:

```sql
-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    firebase_uid VARCHAR UNIQUE,
    email VARCHAR UNIQUE NOT NULL,
    full_name VARCHAR NOT NULL,
    role VARCHAR NOT NULL CHECK (role IN ('student', 'teacher')),
    profile_photo_url TEXT,
    grade INTEGER,
    institution_name VARCHAR,
    subjects TEXT[],
    parent_email VARCHAR,
    xp INTEGER DEFAULT 0,
    level INTEGER DEFAULT 1,
    current_streak INTEGER DEFAULT 0,
    longest_streak INTEGER DEFAULT 0,
    hill_position FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Study sessions table
CREATE TABLE study_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    subject VARCHAR NOT NULL,
    topic VARCHAR NOT NULL,
    timer_mode VARCHAR NOT NULL,
    study_duration_minutes INTEGER NOT NULL,
    break_duration_minutes INTEGER NOT NULL,
    cycles_completed INTEGER DEFAULT 0,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    status VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Add other tables as per PRD...
```

### Update Code with Configuration

In your app code, replace placeholder Supabase configuration:

```kotlin
// In your application class or repository
val supabaseClient = createSupabaseClient(
    supabaseUrl = "YOUR_SUPABASE_URL",
    supabaseKey = "YOUR_SUPABASE_ANON_KEY"
) {
    install(Postgrest)
    install(Realtime)
    install(Storage)
}
```

## Step 5: Build and Run

### Sync Gradle

If Android Studio hasn't synced automatically:
1. Click "Sync Project with Gradle Files" in the toolbar
2. Wait for sync to complete

### Create Virtual Device or Connect Physical Device

**Option A: Use Emulator**

1. Click Device Manager in Android Studio
2. Click "Create Virtual Device"
3. Select a device (e.g., Pixel 5)
4. Select system image (API 34 recommended)
5. Finish and start emulator

**Option B: Use Physical Device**

1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect via USB
4. Accept debugging prompt on device

### Run the App

1. Click the green "Run" button (▶) in Android Studio
2. Or press `Shift + F10`
3. Select your device
4. Wait for build and installation

## Step 6: Grant Permissions

On first launch, you'll need to grant several permissions:

1. **Display Over Other Apps**: For the lock screen overlay
2. **Device Administrator**: To prevent app uninstallation
3. **Notifications**: For reminders and updates

### Granting Overlay Permission

```bash
adb shell appops set com.studylock.app SYSTEM_ALERT_WINDOW allow
```

### Enabling Device Admin

```bash
adb shell dpm set-active-admin com.studylock.app/com.studylock.app.receiver.DeviceAdminReceiver
```

## Step 7: Test the App

### Test Flow

1. **Onboarding**: Complete the 3-slide onboarding
2. **Role Selection**: Choose "I'm a Student"
3. **Sign Up**: Create a test account (Firebase will handle this)
4. **Home Dashboard**: Explore the home screen
5. **Start Study Session**:
   - Select a subject (Mathematics)
   - Select a topic (Algebra)
   - Choose timer mode (Classic Pomodoro)
   - Start session
6. **Timer**: Watch the countdown, pause/resume
7. **Test Gate**: Answer the sample questions
8. **Result**: See pass/fail result

### Known Limitations

- Firebase Auth is not fully connected - sign-up won't create a real account
- Timer works but doesn't persist data
- Questions are hardcoded samples
- Lock overlay works but doesn't actually block apps
- XP and gamification are UI only

## Troubleshooting

### Build Errors

**"Plugin with id 'com.google.gms.google-services' not found"**
- Ensure `google-services.json` is in the `app/` directory
- Sync Gradle again

**"Supabase dependencies not found"**
- Check your internet connection
- Invalidate caches: File → Invalidate Caches → Invalidate and Restart

### Runtime Errors

**"Firebase initialization failed"**
- Check `google-services.json` content
- Verify package name matches Firebase project
- Check Firebase project status

**"Permission denied"**
- Grant runtime permissions manually in Settings → Apps → StudyLock → Permissions

### App Crashes

Check Logcat for stack traces:
```bash
adb logcat | grep StudyLock
```

## Development Workflow

### Recommended IDE Settings

1. Enable "Auto-import" in Settings → Editor → General → Auto Import
2. Set Kotlin code style to "Official"
3. Enable "Compose Preview" for faster UI development

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

### Building Release APK

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

## Next Steps

After getting the app running, check out:

1. **IMPLEMENTATION_STATUS.md**: See what's done and what's pending
2. **README.md**: Detailed project documentation
3. **mayur app.md**: Complete PRD with all features

## Getting Help

- **Documentation**: Check the inline code comments and docs
- **PRD**: Refer to `mayur app.md` for feature specifications
- **Status**: See `IMPLEMENTATION_STATUS.md` for current progress

## Contributing

If you want to contribute:

1. Create a feature branch from `main`
2. Make your changes
3. Test thoroughly
4. Submit a pull request

---

Happy coding! 🚀
