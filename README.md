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
- Testing to improve app stability. 
- Allow user to save photo into device.
- Separate into free-trial and premium-trial so that users need to pay for unlocking to see higher resolution photo.
  
