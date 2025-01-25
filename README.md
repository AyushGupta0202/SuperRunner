# Super Runner 🏃‍♂️

A modern fitness tracking application for Android phones and Wear OS devices that helps users track their runs with detailed metrics and analytics.

## Features 🌟

- **Run Tracking**: Real-time GPS tracking for runs with detailed metrics
- **Active Run Service**: Background service for continuous run tracking
- **Wear OS Support**: Dedicated wear module for smartwatch integration
- **Analytics**: Built-in analytics for tracking user engagement and app performance
- **Authentication**: Secure user authentication system
- **Notifications**: Real-time updates and progress notifications
- **Modern UI**: Material Design 3 components with custom design system

### Phone App
- User Authentication (Sign up/Sign in)
- Run Tracking with GPS
- Real-time run metrics:
  - Distance
  - Duration
  - Pace
  - Speed
  - Heart Rate
  - Elevation
- Run History & Statistics
- Dynamic Feature Modules (Analytics module)
- Background Service for run tracking
- Splash Screen
- Dark/Light Theme support

### Wear OS App
- Real-time run tracking
- Heart Rate monitoring
- Distance tracking
- Ambient mode support
- Phone connectivity status
- Background service integration
- Syncs with phone app

## Architecture 📐

### Design Patterns & Principles

- **Architecture**: Clean Architecture with multi-module setup
- **UI Pattern**: MVI (Model-View-Intent) and using ViewModel and StateFlow/Flow for UI state management
- **Reactive Programming**: Kotlin Coroutines & Flow
- **Dependency Injection**: Using Koin
- **Single Activity**: Using Jetpack Navigation
- **State Hoisting**: Composable state management
- **Convention Plugins**: Custom gradle plugins for dependency management

### Project Structure
The project follows a clean, modular architecture:

- `app/` - Main phone application module
- `analyitcs/` - Analytics module
- `wear/` - Wear OS application module
- `core/` - Shared core functionality
  - `presentation/`
  - `domain/`
  - `data/`
  - `utils/`
  - `notification/`
  - `connectivity/`
  - `database/`
- `auth/` - Authentication feature module
- `run/` - Run tracking feature module

- Each module has its own `presentation/`, `domain/`, `data/` and `utils/` folders.

## Tech Stack 🛠

### UI
- Jetpack Compose
- Wear Compose
- Material Design 3
- Custom Design System
- Compose Navigation

### Architecture Components
- ViewModel
- StateFlow/Flow
- WorkManager
- Room Database

### Dependency Injection
- Koin

### Concurrency
- Kotlin Coroutines
- Kotlin Flow

### Location & Sensors
- Google Play Services Location
- Activity Recognition
- Health Services

### Networking
- Ktor Client

### Local Storage
- Room Database
- Encrypted Shared Preferences

### Testing
- JUnit5
- Mockk
- Turbine
- Compose Testing

### Other Libraries
- Timber (Logging)
- Play Services (Wearable)
- SplitInstall (Dynamic Features)
- AndroidX Core KTX

## Setup & Installation 🚀

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Add required API keys in `local.properties`:
   ```properties
   MAPS_API_KEY=your_maps_api_key
   ```
5. Run the app module for phone app
6. Run the wear module for Wear OS app

## Architecture Flow 🔄

1. Presentation Layer (Compose)
3. Domain Layer (Repositories and abstractions)
4. Data Layer (Implementation of repositories)

## Contributing 🤝

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License 📄

This project is licensed under the MIT License - see the LICENSE file for details