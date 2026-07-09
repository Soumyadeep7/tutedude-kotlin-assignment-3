# CampusCart 🛒

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.x-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-purple.svg)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Persistence-Room%20DB-orange.svg)](https://developer.android.com/training/data-storage/room)

**CampusCart** is a modern, visually immersive peer-to-peer marketplace exclusively for university students. Whether you're looking to buy, sell, or rent textbooks, exam prep materials, lab equipment, furniture, or dorm essentials, CampusCart connects you with trusted peers right within your campus community.

---

## 🎨 Visual Identity 

CampusCart delivers a polished, content-rich Material Design 3 experience with edge-to-edge layouts, dynamic promotional carousels, and intuitive visual cues that make browsing effortless.

- **Custom Promo Slider** – Showcases hot deals, rental events, and safety tips right at the top of your feed.
- **Aesthetic Category Badges** – Quickly filter by academic and dorm categories like Books, Electronics, Furniture, Notes, Lab Equipment, and Hostel Essentials.
- **Dual-mode Trade Chips** – Instantly identify listings with colored badges: **Sell** (Green) for one-time purchases or **Rent** (Blue) with clear monthly pricing.
- **Rich Student Profiles** – Build trust with detailed seller info including college, location, and rating indicators.

--- screen video = https://res.cloudinary.com/fpgcjy7h/video/upload/v1783615933/campuscartvideo_edited_zt1bq2.mp4
apk file = https://drive.google.com/file/d/1unkoYKH9wZA8mQAW1i1d72MeDYgF5eD8/view

## 🚀 Key Features

- **Unified Campus Feed** – Browse all listings or filter by category with a single tap on customized category pills.
- **Dual Trading Support (Buy/Sell vs. Rent)** – Clear pricing formats for both flat purchases and monthly rentals (e.g., scientific calculators, study desks, metal beds).
- **Robust Favorites/Wishlist** – Bookmark items instantly with offline-ready persistence powered by Room Database.
- **Streamlined Product Posting** – An intuitive upload dashboard with preset images, automated category-based rental calculations, and built-in error validation.
- **Detailed Product Pages** – Swipeable image galleries, high-contrast action buttons, condition ratings, and verified college affiliation labels for added trust.

---

## 🛠️ Architecture & Tech Stack

Built following Google's **Modern Android Development (MAD)** guidelines, CampusCart leverages a clean, scalable architecture.

- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) – Declarative, state-driven UI with Material Design 3.
- **Architecture:** MVVM with Repository pattern for clean separation of concerns.
- **Local Persistence:** [Room Database](https://developer.android.com/training/data-storage/room) – Offline-first favorites with robust SQLite mapping.
- **Asynchronous Flow:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) – Reactive state management for seamless UI updates.
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) – Optimized, cached image loading with smooth crossfade transitions.
- **System Styling:** Full `enableEdgeToEdge()` support with custom WindowInsets for status and navigation bars.

---

## 📂 Project Directory Structure

```text
app/src/main/java/com/example/
├── CampusCartApplication.kt      # App configuration and initialization
├── MainActivity.kt               # Single-activity setup with edge-to-edge styling
├── data/
│   ├── local/
│   │   └── LocalDb.kt            # Room Database (Entities, DAOs, and Favorites)
│   ├── model/
│   │   └── Models.kt             # Product, category, and core domain models
│   └── repository/
│       ├── AuthRepository.kt     # Authentication layer interfaces
│       ├── FavoriteRepository.kt # Favorites data layer
│       └── ProductRepository.kt  # Product seeding, listing, and upload logic
├── di/
│   └── AppContainer.kt           # Manual dependency injection container
└── ui/
    ├── components/
    │   └── Components.kt         # Reusable UI elements (BannerSlider, ProductCard, CategoryPill)
    ├── navigation/
    │   └── AppNavigation.kt      # Jetpack Navigation Compose routes
    ├── screens/
    │   ├── HomeScreen.kt         # Feed with promo banners and category filters
    │   ├── ProductDetailsScreen.kt # Multi-image gallery with detailed product view
    │   ├── UploadProductScreen.kt # Intuitive form with preset visual aids
    │   ├── FavoritesScreen.kt    # Offline wishlist management
    │   ├── ProfileScreen.kt      # Student profile and stats
    │   └── Login/RegisterScreen.kt # Seamless onboarding flows
    ├── theme/
    │   └── Theme.kt              # Centralized M3 colors and typography
    └── viewmodel/
        ├── AuthViewModel.kt      # User authentication state
        ├── ProductViewModel.kt   # Product catalog and feed logic
        └── UploadViewModel.kt    # Validation and submission logic for new listings

💻 Getting Started
Prerequisites
Android Studio (Ladybug or newer)

JDK 17+

Android SDK level 34+

Installation & Run
Clone the Repository:
