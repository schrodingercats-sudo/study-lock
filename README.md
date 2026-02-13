# StudyLock - Android App

StudyLock is an Android application designed to enforce disciplined study habits by locking the user's home screen and restricting phone usage to only whitelisted essential apps. The phone unlocks only after the user completes a Pomodoro study session followed by a knowledge test with a minimum 80% pass threshold.

## Features

### Core Features
- **Home Screen Lock System**: Blocks non-essential apps during study sessions
- **Pomodoro Timer**: Multiple timer modes (Classic, Extended, Custom, Exam)
- **Test Gate**: Post-session knowledge test with 80% pass threshold
- **Gamification**: XP system, levels, badges, and Hill Climb Racing-themed rewards
- **Leaderboards**: Global, School, Subject, and Friends leaderboards
- **Online Quizzes**: Real-time quizzes with matchmaking
- **Doubt Solving**: Teacher-student interaction for doubt resolution
- **Progress Analytics**: Detailed graphs and reports
- **Anti-Cheat**: Multiple security measures during tests

### Tech Stack

#### Frontend (Android)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navigation**: Navigation Component
- **DI**: Hilt
- **Async**: Coroutines & Flow
- **Image Loading**: Coil
- **Minimum SDK**: API 26 (Android 8.0)
- **Target SDK**: API 34

#### Backend Services
- **Authentication**: Firebase Auth (Email, Google, Phone OTP)
- **Realtime Database**: Firebase Realtime Database
- **Cloud Messaging**: Firebase Cloud Messaging (FCM)
- **Primary Database**: Supabase (PostgreSQL)
- **File Storage**: Supabase Storage
- **Edge Functions**: Supabase Edge Functions (Deno)
- **Real-time**: Supabase Realtime
- **Voice/Video**: WebRTC

#### Third-Party Integrations
- **OCR**: Google ML Kit Text Recognition
- **AI**: OpenAI/Anthropic APIs
- **PDF**: iText for Android
- **Analytics**: Mixpanel/Amplitude
- **Crash Reporting**: Firebase Crashlytics

## Project Structure

```
app/
├── src/main/java/com/studylock/app/
│   ├── MainActivity.kt
│   ├── StudyLockApplication.kt
│   ├── MainViewModel.kt
│   ├── domain/
│   │   ├── model/
│   │   │   ├── User.kt
│   │   │   ├── StudySession.kt
│   │   │   └── Test.kt
│   │   └── usecase/
│   │       └── auth/
│   │           ├── GetCurrentUserUseCase.kt
│   │           └── ObserveAuthStateUseCase.kt
│   ├── presentation/
│   │   ├── auth/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── SignUpScreen.kt
│   │   │   └── RoleSelectionScreen.kt
│   │   ├── home/
│   │   │   └── HomeScreen.kt
│   │   ├── onboarding/
│   │   │   └── OnboardingScreen.kt
│   │   ├── study/
│   │   │   ├── StudyModeScreen.kt
│   │   │   └── TimerScreen.kt
│   │   ├── test/
│   │   │   └── TestGateScreen.kt
│   │   └── navigation/
│   │       ├── Screen.kt
│   │       └── StudyLockNavigation.kt
│   ├── service/
│   │   ├── LockScreenService.kt
│   │   └── TimerService.kt
│   ├── receiver/
│   │   ├── BootReceiver.kt
│   │   └── DeviceAdminReceiver.kt
│   └── ui/
│       └── theme/
│           ├── Color.kt
│           ├── Theme.kt
│           └── Type.kt
└── src/main/res/
    ├── layout/
    │   └── lock_screen_overlay.xml
    ├── values/
    │   ├── strings.xml
    │   └── themes.xml
    └── xml/
        └── device_admin_receiver.xml
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK API 34
- Firebase account
- Supabase account

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd StudyLock
   ```

2. **Configure Firebase**
   - Create a new project in Firebase Console
   - Add an Android app with package name `com.studylock.app`
   - Download `google-services.json` and place it in `app/` directory
   - Enable the following services:
     - Authentication (Email, Google, Phone)
     - Realtime Database
     - Cloud Messaging
     - Crashlytics

3. **Configure Supabase**
   - Create a new project in Supabase Dashboard
   - Set up the database tables according to the schema in the PRD
   - Generate your Supabase URL and anon key
   - Update the Supabase configuration in the code

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

## Architecture

The app follows Clean Architecture principles with clear separation of concerns:

- **Domain Layer**: Contains business logic, entities, and use cases
- **Presentation Layer**: Contains UI components, ViewModels, and navigation
- **Data Layer**: Contains repositories, data sources, and API services (to be implemented)

## Screens Implemented

1. ✅ Onboarding (3 slides)
2. ✅ Role Selection (Student/Teacher)
3. ✅ Login Screen
4. ✅ Sign Up Screen
5. ✅ Home Dashboard (Student)
6. ✅ Study Mode Configuration
7. ✅ Active Timer Screen
8. ✅ Test Gate with Questions
9. ✅ Test Result (Pass/Fail)

## Services Implemented

1. ✅ LockScreenService - Foreground service for overlay lock
2. ✅ TimerService - Foreground service for Pomodoro timer
3. ✅ BootReceiver - Restarts lock on device boot
4. ✅ DeviceAdminReceiver - Prevents uninstallation during lock

## TODO - Remaining Features

### Phase 2 - Social & Competition
- [ ] Leaderboard implementation
- [ ] Online Quiz matchmaking
- [ ] Friend system
- [ ] Social sharing

### Phase 3 - Teacher Ecosystem
- [ ] Teacher dashboard
- [ ] Quiz creation
- [ ] Doubt-solving system
- [ ] Teacher analytics

### Phase 4 - Advanced Features
- [ ] Voice/Video calls (WebRTC)
- [ ] AI question generation
- [ ] Advanced anti-cheat
- [ ] Voucher rewards
- [ ] Premium subscription

### Phase 5 - Scale & Polish
- [ ] Performance optimization
- [ ] Localization
- [ ] Parent dashboard
- [ ] Study groups

## Database Schema (Supabase)

See the full database schema in `mayur app.md` document, including tables for:
- users
- study_sessions
- tests
- test_questions
- question_bank
- quizzes
- quiz_participants
- doubts
- doubt_messages
- rewards
- screen_time
- reports
- leaderboard_cache
- friendships
- quotes
- teacher_availability
- call_logs

## Contributing

This is a work in progress project. Contributions are welcome!

## License

See LICENSE file for details.

## Product Requirements Document

For detailed requirements, user flows, algorithms, and specifications, see `mayur app.md`.
