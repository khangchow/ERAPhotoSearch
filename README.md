# Welcome to ERAPhotoSearch
This is an Android app featuring Pexels search api (https://www.pexels.com/api/documentation/#introduction) allows users to:
- Search photos by entering keyword into search view with search history saved as well as history delete feature.
- Shared element transition when click on searched photos in home screen.
- Searched result photos are displaying in beautiful grid layout inside rounded corner cardviews.
- See selected photo details with photographer, original resolution, current displayed size.
- Pinch to zoom in photo details screen.
- Motion Layout to animate hiding photo information section.
- Click on photographer's name to see personal profile on WebView and current size to switch to desired size.

## Architecture patterns
- MVVM (Model-View-ViewModel): Separates UI from business logic, making the app easier to test and maintain.
- Clean Architecture (follows App Architecture https://developer.android.com/topic/architecture): Organizes code into layers with clear separation of concerns.

## Core technologies
- Lifecycle Components: Helps UI components respond to lifecycle changes.
- Navigation Components: Manages in-app navigation and back stack cleanly.
- SwipeRefreshLayout: Adds pull-to-refresh behavior to scrollable views.
- Glide: Loads and caches images efficiently.
- Paging 3: Efficiently loads paged data from network or database sources.
- Room: Stores and queries local data in a type-safe, reactive way.
- Coroutines: Performs async tasks (network/database) in a non-blocking way.
- Retrofit: Makes API requests and handles JSON serialization/deserialization.
- Hilt: Manages dependencies and simplifies injection across components.

## Things to improve
- More appealing UIs -> Current version only focuses on clean, modern, maintainable, scalable code.

## Things to add
- Add testing to improve app stability. 
- Allow user to save photo into device.
- Separate into free-trial and premium-trial so that users need to pay for unlocking to see higher resolution photo.
  
## Getting started
- Clone the repository using "https://github.com/khangchow/ERAPhotoSearch.git" into your desired directory.
- You can use the current API Key or create your own at "https://www.pexels.com/api/" after logged in.
- If you create your own API Key -> replace it to the current one in keys.c file located at Android view -> app -> cpp -> keys.c
- Make sure your Android Studio Version, Java Version, Gradle Version, AGP Version are compatible to run the project.
  
## Note: The project is developed using 
- Android Studio Meerkat | 2024.3.1 Patch 1
- JavaVersion.VERSION_11
- Android Gradle Plugin Version 8.9.2
- Gradle Version 8.11.1
