# 🛒 CampusCart - Android E-Commerce Application

CampusCart is a modern Android e-commerce application developed using **Kotlin** and follows the **MVVM (Model-View-ViewModel)** architecture. The application enables users to register, log in, browse products, upload products, and save favorite items while leveraging Firebase and Room Database for cloud and local data management.

This project was developed as part of the **TuteDude Android Development Assignment**.

---

# Features

## Authentication

* User Registration
* User Login
* Firebase Email & Password Authentication
* Login and Registration Validation

## Home Screen

* Displays all uploaded products
* Product Image
* Product Title
* Short Description
* Product Price
* Navigation to Product Details

## Product Details

* Full Product Description
* Product Images
* Product Price
* Uploader Name
* Contact Information
* Add Product to Favorites

## Upload Product

Users can upload products with:

* Product Title
* Description
* Price
* Minimum Three Images

Product information is stored in Firebase Firestore, and product images are stored in Firebase Storage.

## Favorites

* Save products locally
* Offline access using Room Database
* View and remove favorite products

---

# Architecture

The application follows the MVVM architecture with a clean separation between UI, ViewModel, Repository, and Data layers.

```
UI
│
├── ViewModel
│
├── Repository
│
├── Firebase / Room
│
└── Models
```

---

# Tech Stack

* Kotlin
* MVVM Architecture
* Hilt Dependency Injection
* Firebase Authentication
* Firebase Firestore
* Firebase Storage
* Room Database
* Jetpack Compose / XML
* Navigation Component
* ViewModel
* LiveData / StateFlow

---

# Project Structure

```
CampusCart
│
├── app
├── data
│   ├── local
│   ├── remote
│   ├── repository
│
├── di
│
├── model
│
├── ui
│   ├── auth
│   ├── home
│   ├── details
│   ├── upload
│   ├── favorites
│
├── viewmodel
│
├── utils
│
└── MainActivity.kt
```

---

# Firebase Services

* Firebase Authentication
* Firebase Firestore
* Firebase Storage

---

# Dependencies

* AndroidX
* Material Design
* Firebase Authentication
* Firebase Firestore
* Firebase Storage
* Room Database
* Hilt
* Navigation Component
* Coil / Glide

Optional Libraries:

* Retrofit
* Gson Converter
* FakeStore API

---

# Getting Started

## Prerequisites

* Android Studio Hedgehog or later
* JDK 17 or above
* Android SDK 34+
* Firebase Project

## Installation

Clone the repository:

```bash
git clone https://github.com/your-username/CampusCart.git
```

Open the project in Android Studio.

Download the `google-services.json` file from Firebase Console and place it inside the `app` directory.

Enable the following Firebase services:

* Authentication (Email/Password)
* Cloud Firestore
* Firebase Storage

Sync Gradle and run the application on an Android device or emulator.

---

# Screenshots

## Login Screen

(Add Screenshot)

## Registration Screen

(Add Screenshot)

## Home Screen

(Add Screenshot)

## Product Details Screen

(Add Screenshot)

## Upload Product Screen

(Add Screenshot)

## Favorites Screen

(Add Screenshot)

---

# Assignment Requirements Completed

* Firebase Authentication
* Product Listing
* Product Details
* Product Upload
* Firebase Firestore Integration
* Firebase Storage Integration
* Room Database for Favorites
* MVVM Architecture
* Hilt Dependency Injection
* Material Design UI
* Navigation Component

---

# Optional Features

* Search Products
* Category Filters
* Recommended Products using FakeStore API
* Retrofit Integration
* Push Notifications
* Pagination / Lazy Loading

---

# Project Deliverables

* Android Source Code
* APK File
* Screen Recording
* GitHub Repository
* Project Documentation

---

# Author

**Soumyadeep Das**

B.Tech in Computer Science & Information Technology

---

# License

This project is developed for educational purposes as part of the **TuteDude Android Development Assignment**.

