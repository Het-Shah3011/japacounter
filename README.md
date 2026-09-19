# Japa Counter (Deity & Mantra Counter)

A clean, production-ready Android application for counting mantra repetitions (Japa) with support for multiple deities, mala tracking, and haptic feedback.

## Features

- **Interactive Counter**: Tap the large circle to increment your count
- **Mala Tracking**: Automatically tracks completed 108-bead malas
- **108 Milestone Vibration**: Distinct double-pulse haptic feedback when reaching multiples of 108
- **Multiple Deities**: Manage independent counters for different deities/mantras
- **Full CRUD**: Add, edit (rename & adjust count), and delete deity entries
- **Persistent Storage**: All data saved via Room Database
- **Light & Dark Theme**: Material 3 dynamic theming support

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM with Clean Architecture
- **State**: Kotlin StateFlow & Coroutines
- **Database**: Room (SQLite)
- **Navigation**: Jetpack Navigation Compose
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
com.japa.counter/
├── data/
│   ├── entity/
│   │   └── DeityCounterEntity.kt       # Room Entity
│   ├── dao/
│   │   └── DeityDao.kt                 # Data Access Object
│   ├── database/
│   │   └── AppDatabase.kt              # Room Database with prepopulated default
│   └── repository/
│       └── DeityRepository.kt          # Repository layer
├── ui/
│   ├── theme/
│   │   ├── Color.kt                    # Custom color palette
│   │   ├── Theme.kt                    # Light/Dark theme setup
│   │   └── Type.kt                     # Typography definitions
│   ├── screens/
│   │   ├── CounterScreen.kt            # Main counter UI
│   │   ├── DeityListScreen.kt          # Deity management list
│   │   ├── AddEditDeityDialog.kt       # Add/Edit dialogs
│   │   └── Navigation.kt               # Navigation graph
│   └── viewmodel/
│       └── CounterViewModel.kt         # Business logic & state management
├── utils/
│   └── VibrationHelper.kt              # Haptic feedback utility
├── MainActivity.kt                     # Entry point
└── JapaCounterApplication.kt           # Application class
```

## How to Build

1. Open the project in Android Studio (Giraffe or newer)
2. Sync Gradle files
3. Build and run on an emulator or physical device (API 26+)

## Permissions

- `VIBRATE`: Required for haptic feedback on mala completion

## Architecture

The app follows **MVVM** architecture with:
- **Model**: Room entities + Repository pattern
- **ViewModel**: `CounterViewModel` manages all UI state and business logic
- **View**: Jetpack Compose screens observe StateFlow from ViewModel
- **Utils**: `VibrationHelper` abstracts haptic feedback with backward compatibility
