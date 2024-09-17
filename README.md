# MobileChallenge
## About
The application is a simple movie app that displays a list of popular movies, and now playing movies. The user can add movies to favorites and remove those movies. User can be able to check the information for these movies and filter by name.
 If for some reason the user doesn't have internet, once the internet is back Users can swipe to refresh  the new data from the API.

Libraries Used
Kotlin: Programming language used for development.
Hilt: Dependency injection library for managing dependencies.
Retrofit: HTTP client for making API requests.
Paging 3: For paginated data loading.
Room: Local database for offline data storage.
Glide: Image loading and caching.
Kotlinx Coroutines: For handling asynchronous operations.
Mockito: For mocking dependencies in unit tests.
Jetpack Navigation: For enabling the nav between the fragments
ViewModel: For managing UI-related data in a lifecycle-conscious way and handling business logic.
build-logic: (Managing dependencies using toml)
Modular Design: Core:network, Core:database, Core:Model, Core:data


# Setup
# Prerequisites
Android Studio Bumblebee or newer.
Java 11 or newer.
Internet connection for fetching data from the API.

# Architecture
The project uses a well-defined architecture, including MVVM (Model-View-ViewModel) with Hilt for dependency injection. Integrating this architecture required careful consideration of data flow and lifecycle management:

MVVM Pattern: Ensuring that the ViewModel effectively interacts with the repository and updates the UI was challenging. The separation of concerns between the UI (Fragment), business logic (ViewModel), and data (Repository) required meticulous implementation.
Dependency Injection: Setting up Hilt for dependency injection added complexity, particularly in configuring test modules and mock dependencies.
Paging Library: Integrating the Paging library for efficient movie list loading and handling different states (loading, error, empty) required attention to detail.

