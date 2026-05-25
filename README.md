# TripForge

TripForge is an Android travel planning application built with Jetpack Compose. It allows users to organize trips end-to-end: itinerary, budget, packing list, map, and scheduled notifications, all stored locally on the device.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
---

## Features

**Trip Management**
- Create, edit, and delete trips with destination, dates, and budget
- Automatic status calculation: Upcoming, Ongoing, or Complete based on current date

**Itinerary**
- Day-by-day activity planning
- Add, edit, and mark activities as completed
- Activity-level date and time tracking

**Budget**
- Set a total trip budget
- Log expenses by category: Transport, Accommodation, Food, Activities, Other
- Visual breakdown of spending per category

**Packing List**
- Create packing items organized by category
- Check items off as they are packed

**Map**
- View trip destination on an interactive Google Map
- Device location awareness with fine and coarse location permissions

**Notifications**
- Scheduled reminders via WorkManager: 7 days before trip, 1 day before trip, packing incomplete alert, and activity reminders
- Per-user notification preferences persisted with DataStore

**Profile**
- User account management
- Travel statistics derived from real trip data
- Settings: notifications, privacy, travel preferences, app appearance

**Home**
- Next or ongoing trip card with countdown, budget progress, packing progress, and next pending activity
- Travel stats summary: total trips, completed trips, total spent, activities done, top expense category

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Navigation | Navigation Compose |
| Local storage | DataStore Preferences, kotlinx-serialization, Gson |
| Image loading | Coil 3 |
| Networking | Retrofit 2, OkHttp 3 |
| Maps | Google Maps SDK, Play Services Location |
| Background work | WorkManager |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

---

## Project Structure

```
app/src/main/java/com/example/tripforge/
|
├── data/
│   ├── AuthenticationDataStore.kt    # User session persistence
│   ├── ImageCacheManager.kt          # Local image caching
│   ├── ImageService.kt               # Remote image fetching
│   ├── LocationData.kt               # Countries and cities data
│   ├── NotificationWorker.kt         # WorkManager periodic worker
│   ├── PreferencesDataStore.kt       # User preferences (notifications, etc.)
│   ├── SampleTripData.kt             # Seed data for development
│   ├── TripDataStore.kt              # Trip CRUD persistence
│   ├── TripNotificationManager.kt    # Notification channel and dispatch
│   └── TripRepository.kt            # Single data access point for trips
│
├── model/
│   ├── TripModel.kt                  # TripSummary, ActivityItem, DayPlan,
│   │                                 #   ExpenseItem, PackingItem, enums
│   └── UserModel.kt                  # User data class
│
├── screens/
│   ├── AddActivityScreen.kt
│   ├── AddTripScreen.kt
│   ├── AppSettingsScreen.kt
│   ├── BudgetScreen.kt
│   ├── EditActivityScreen.kt
│   ├── EditTripScreen.kt
│   ├── HomeScreen.kt
│   ├── ItineraryScreen.kt
│   ├── LoginRegisterScreen.kt
│   ├── MapScreen.kt
│   ├── NotificationsScreen.kt
│   ├── PackingListScreen.kt
│   ├── PrivacyScreen.kt
│   ├── ProfileScreen.kt
│   ├── TravelPreferencesScreen.kt
│   ├── TripDetailsScreen.kt
│   └── TripsScreen.kt
│
├── ui/
│   ├── components/
│   │   ├── BottomNavigation.kt
│   │   ├── Cards.kt
│   │   ├── Fields.kt
│   │   ├── Headers.kt
│   │   ├── Pickers.kt                # DatePickerField, TimePickerDialog
│   │   └── TripComponents.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── activities/
│   └── EditTripActivity.kt
│
├── MainActivity.kt
└── TripForgeApplication.kt
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android device or emulator running API 24 or higher
- A Google Maps API key with the Maps SDK for Android enabled

### Installation

1. Clone the repository:

```bash
git clone https://github.com/your-username/TripForge.git
cd TripForge
```

2. Add your Google Maps API key (see [Configuration](#configuration)).

3. Open the project in Android Studio and let Gradle sync.

4. Run on a device or emulator:

```bash
./gradlew installDebug
```

---

## Configuration

### Google Maps API Key

The app requires a Maps API key for the map screen and location features. Add it to your local `gradle.properties` file (this file should not be committed):

```properties
MAPS_API_KEY=your_api_key_here
```

The key is injected at build time into the manifest and made available via `BuildConfig`.

To get a key, visit the [Google Cloud Console](https://console.cloud.google.com/) and enable the Maps SDK for Android on your project.

### Permissions

The following permissions are declared in the manifest and requested at runtime where applicable:

| Permission | Purpose |
|---|---|
| `INTERNET` | Image loading, future API integration |
| `ACCESS_FINE_LOCATION` | Precise device location on map |
| `ACCESS_COARSE_LOCATION` | Approximate device location fallback |
| `POST_NOTIFICATIONS` | Trip and activity reminders (API 33+) |
| `RECEIVE_BOOT_COMPLETED` | Re-schedule notifications after reboot |

---
