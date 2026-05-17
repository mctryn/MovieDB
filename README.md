# MovieDB 🎬

**MovieDB** is an **Android application** built with **Jetpack Compose** that allows users to browse popular movies, view details, and manage their favorites. The app follows **Clean Architecture** principles, ensuring a clear separation of concerns between the **presentation**, **domain**, and **data** layers.

---

## 📌 Features

- ✅ **Browse Popular Movies**: Fetch and display a list of popular movies from the **TMDB API** (or fallback to local JSON data).
- ✅ **View Movie Details**: Navigate to a detailed screen for any movie, including its title, overview, poster, release date, and rating.
- ✅ **Manage Favorites**: Mark/unmark movies as favorites, with persistence using **Room Database**.
- ✅ **Offline Support**: Cache movies locally for offline access.
- ✅ **Error Handling**: Graceful handling of network errors, empty states, and loading states.
- ✅ **Modern UI**: Built with **Jetpack Compose** and **Material Design 3** for a smooth and responsive user experience.
- ✅ **Navigation**: Bottom navigation bar for switching between **Movie List** and **Favorites** screens.

---

## 🏗️ Architecture

The app follows **Clean Architecture** with the following layers:

### 📂 **Project Structure**

```
MovieDB/
├── app/                          # Main Android module
│   ├── src/
│   │   ├── main/                 # Production code
│   │   │   ├── kotlin/com/mctryn/moviedb/
│   │   │   │   ├── data/         # Data Layer (Repositories, Data Sources, DB)
│   │   │   │   ├── domain/       # Domain Layer (Use Cases, Models, Interfaces)
│   │   │   │   ├── navigation/   # Navigation components
│   │   │   │   ├── presentation/ # Presentation Layer (Compose Screens, ViewModels)
│   │   │   │   └── di/           # Dependency Injection (Koin)
│   │   │   └── res/             # Resources (Strings, Colors, Icons, etc.)
│   │   ├── test/                 # Unit Tests
│   │   └── androidTest/          # Instrumented Tests
│   ├── build.gradle.kts          # App-level Gradle config
│   └── AndroidManifest.xml       # App manifest
├── gradle/                       # Gradle configuration
├── libs.versions.toml            # Dependency versions (Version Catalog)
├── settings.gradle.kts           # Project settings
└── README.md                     # This file
```

### 🔄 **Data Flow**

```
API/JSON → Repository → Use Cases → ViewModel → UI
```

1. **Data Layer**: Fetches data from **TMDB API** or **local JSON**, caches it in **Room DB**, and provides it to the domain layer.
2. **Domain Layer**: Contains **use cases** and **domain models** to encapsulate business logic.
3. **Presentation Layer**: Uses **Jetpack Compose** to display data and handle user interactions via **ViewModels**.


---

## 🛠️ Tech Stack

### **Core Dependencies**

| Category | Dependency | Version | Purpose |
|----------|------------|---------|---------|
| **AndroidX** | `androidx.core:core-ktx` | 1.18.0 | Kotlin extensions for Android core |
| | `androidx.lifecycle:lifecycle-runtime-ktx` | 2.10.0 | Lifecycle components |
| | `androidx.lifecycle:lifecycle-viewmodel-compose` | 2.10.0 | ViewModel integration with Compose |
| | `androidx.activity:activity-compose` | 1.13.0 | Compose integration with `ComponentActivity` |
| **Jetpack Compose** | `androidx.compose:compose-bom` | 2026.05.00 | Compose BOM (Bill of Materials) |
| | `androidx.compose.ui:ui` | - | Compose UI toolkit |
| | `androidx.compose.material3:material3` | - | Material Design 3 components |
| | `androidx.compose.material:material-icons-extended` | - | Material icons |
| **Navigation** | `androidx.navigation3:navigation3-ui` | 1.1.1 | Jetpack Compose Navigation 3 |
| | `androidx.navigation3:navigation3-runtime` | 1.1.1 | Navigation runtime |
| **Dependency Injection** | `io.insert-koin:koin-android` | 4.2.1 | Dependency injection for Android |
| | `io.insert-koin:koin-compose` | 4.2.1 | Koin integration with Compose |
| **Database** | `androidx.room:room-runtime` | 2.8.4 | Room database |
| | `androidx.room:room-ktx` | 2.8.4 | Kotlin extensions for Room |
| **Networking** | `com.squareup.retrofit2:retrofit` | 3.0.0 | HTTP client for API calls |
| | `com.squareup.retrofit2:converter-gson` | 3.0.0 | Gson converter for Retrofit |
| | `com.squareup.okhttp3:okhttp` | 5.3.2 | OkHttp client |
| **Image Loading** | `io.coil-kt:coil-compose` | 2.7.0 | Image loading for Compose |
| **Coroutines** | `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.11.0 | Kotlin coroutines for Android |
| **Serialization** | `org.jetbrains.kotlinx:kotlinx-serialization-json` | 1.11.0 | JSON serialization |

### **Testing Dependencies**

| Category | Dependency | Version | Purpose |
|----------|------------|---------|---------|
| **Unit Testing** | `junit:junit` | 4.13.2 | JUnit 4 |
| | `org.mockito.kotlin:mockito-kotlin` | 6.3.0 | Mocking framework |
| | `androidx.arch.core:core-testing` | 2.2.0 | AndroidX testing utilities |
| | `com.squareup.okhttp3:mockwebserver` | 5.3.2 | Mock HTTP server for API testing |
| **Instrumented Testing** | `androidx.test.ext:junit` | 1.3.0 | AndroidX JUnit |
| | `androidx.test.espresso:espresso-core` | 3.7.0 | Espresso for UI testing |
| | `io.insert-koin:koin-test` | 4.2.1 | Koin testing utilities |

---

## 🚀 Getting Started

### **Prerequisites**

- **Android Studio** (Latest stable version)
- **Java 17+** (Recommended: **JDK 17**)
- **Kotlin 2.0+**
- **Android Gradle Plugin** (AGP) 8.5.0+

### **Setup**

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/MovieDB.git
   cd MovieDB
   ```

2. **Add TMDB API Key (Optional)**:
   - The app can use **TMDB API** for fetching real movie data.
   - To enable this, add your **TMDB API key** in `app/build.gradle.kts`:
     ```kotlin
     buildConfigField("String", "TMDB_API_KEY", "YOUR_API_KEY_HERE")
     ```
   - If no API key is provided, the app will fall back to **local JSON data** (`movies.json`).

3. **Sync Gradle**:
   - Open the project in **Android Studio** and sync Gradle.

4. **Build & Run**:
   - Build the app (`Build > Make Project`).
   - Run on an **emulator** or **physical device** (`Run > Run 'app'`).

---

## 📦 Project Modules

### **1. `:app` (Main Module)**
- Contains all the **app logic**, including:
  - **Activities**: `MainActivity.kt` (Entry point)
  - **Screens**: `MovieListScreen`, `MovieDetailsScreen`, `FavoritesScreen`
  - **ViewModels**: `MovieListViewModel`, `MovieDetailsViewModel`, `FavoritesViewModel`
  - **Use Cases**: `GetPopularMoviesUseCase`, `ToggleFavoriteUseCase`, `RefreshPopularMoviesUseCase`
  - **Repositories**: `MovieRepositoryImpl`
  - **Data Sources**: `RemoteDataSource`, `LocalJsonDataSource`, `CacheDataSource`
  - **Database**: `MovieDatabase`, `MovieDao`, `MovieEntity`

---

## 🌐 API Usage

### **TMDB API**
- **Base URL**: `https://api.themoviedb.org/3/`
- **Endpoints**:
  - `movie/popular` (Fetch popular movies)
  - `movie/{movie_id}` (Fetch movie details)
- **Authentication**: API key required (set in `build.gradle.kts`).

### **Fallback JSON**
- If no API key is provided, the app uses **local JSON data** (`src/main/res/raw/movies.json`).

---

## 🧪 Testing

The app includes **comprehensive testing** for:

### **Unit Tests**
- **Data Layer**: `RemoteDataSourceTest`, `LocalJsonDataSourceTest`, `CacheDataSourceTest`
- **Repository**: `MovieRepositoryImplTest`
- **Use Cases**: `GetPopularMoviesUseCaseTest`, `ToggleFavoriteUseCaseTest`

### **Instrumented Tests**
- **UI Tests**: `MovieListIntegrationTest`, `MovieDetailsIntegrationTest`, `FavoritesIntegrationTest`
- **Database Tests**: `MovieDaoTest`, `MovieDatabaseTest`
- **Navigation Tests**: `NavigationIntegrationTest`

### **Run Tests**
- **Unit Tests**:
  ```bash
  ./gradlew test
  ```
- **Instrumented Tests**:
  ```bash
  ./gradlew connectedAndroidTest
  ```

---

## 🤝 Contributing

Contributions are welcome! Here’s how you can help:

1. **Fork the Repository**
2. **Create a Feature Branch** (`git checkout -b feature/your-feature`)
3. **Commit Your Changes** (`git commit -m "Add your feature"`)
4. **Push to the Branch** (`git push origin feature/your-feature`)
5. **Open a Pull Request**

---

## 📜 License

This project is licensed under the **MIT License**. See **[LICENSE](LICENSE)** for details.

---

## 🙌 Acknowledgments

- **TMDB API**: For providing movie data.
- **Jetpack Compose**: For modern Android UI development.
- **Koin**: For lightweight dependency injection.
- **Room**: For local database persistence.
- **Retrofit**: For networking.
