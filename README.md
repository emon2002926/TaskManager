# TaskManager

## Overview
TaskManager is a native Android application developed in Kotlin for managing tasks efficiently. It demonstrates the use of Android components, MVVM architecture, and local data storage.

## Features
- Add, edit, and delete tasks
- Mark tasks as completed
- Categorize tasks
- Store tasks locally using Room Database
- Follow MVVM architecture for clean code structure

## Technologies Used
- Kotlin
- Android Jetpack (LiveData, ViewModel, Room)
- RecyclerView
- Coroutines for background tasks

## Project Structure
```plaintext
TaskManager/
│── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/taskmanager/
│   │   │   │   ├── data/
│   │   │   │   ├── ui/
│   │   │   │   ├── viewmodel/
│   │   │   │   ├── repository/
│   │   │   ├── res/
│── README.md
│── LICENSE
```

## Installation & Usage
### Prerequisites
- Android Studio
- Minimum SDK: 21 (Lollipop)
- Kotlin 1.5+

### Steps to Run the Application
1. Clone the repository:
   ```sh
   git clone https://github.com/emon2002926/TaskManager.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle and build the project.
4. Run the app on an emulator or a physical device.

## Architecture
TaskManager follows the **MVVM (Model-View-ViewModel)** architecture:
- **Model:** Handles the data layer (Room Database, API calls if applicable).
- **View:** Activities and Fragments that display UI components.
- **ViewModel:** Manages UI-related data and handles logic.

## Contributing
Contributions are welcome! If you'd like to improve the project, please follow these steps:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature-name`.
3. Commit your changes: `git commit -m 'Add feature-name'`.
4. Push to the branch: `git push origin feature-name`.
5. Open a Pull Request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.



