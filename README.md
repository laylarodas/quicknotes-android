# 📱 QuickNotes — Android Notes App (Java)

**QuickNotes** is a lightweight and feature-rich Android app built with **Java**, **Room Database**, and **MVVM architecture**.  
It was developed as a practice project to strengthen core Android fundamentals:  
Activities, RecyclerView, Room persistence, ViewModel & LiveData, and modern UI/UX patterns.

<p align="center">
  <img src="https://img.shields.io/badge/Android-Java-green?logo=android" alt="Android Java">
  <img src="https://img.shields.io/badge/API-21%2B-blue" alt="API 21+">
  <img src="https://img.shields.io/badge/Material%20Design-3-purple" alt="Material Design">
  <img src="https://img.shields.io/badge/Architecture-MVVM-orange" alt="MVVM">
  <img src="https://img.shields.io/badge/Database-Room%202.6.1-red" alt="Room Database">
</p>

---

## 🎯 Purpose

This project is part of my Android learning path within the **DAM program**.  
It focuses on understanding **professional Android architecture** (MVVM), **Room Database**,  
clean UI design with Material Design, and efficient state handling with **ViewModel & LiveData**.

---

## ✨ Features

### 📝 **Note Management**
- ✅ Create, edit, and delete notes
- ✅ Notes with title and content
- ✅ Unique ID (UUID) for each note
- ✅ Timestamps (creation and modification dates)
- ✅ Long-press to delete with confirmation dialog

### 🔍 **Search & Filter**
- ✅ Real-time search by title or content
- ✅ Search results update as you type
- ✅ Clear visual feedback when no results found

### 🔄 **Sorting Options**
- ✅ Sort by modification date (most recent first)
- ✅ Sort by creation date
- ✅ Sort alphabetically (A-Z)
- ✅ Sort alphabetically (Z-A)

### 🎨 **UI/UX**
- ✅ Material Design 3 components
- ✅ Note preview with title, content snippet, and timestamp
- ✅ Relative time display ("2 minutes ago", "1 hour ago")
- ✅ Empty state with contextual messages
- ✅ Smooth animations and transitions
- ✅ Purple-themed color scheme

### 📤 **Sharing**
- ✅ Share notes via WhatsApp, Email, SMS, etc.
- ✅ Native Android share sheet integration

### 💾 **Data Persistence**
- ✅ Room Database (SQLite) with MVVM architecture
- ✅ Automatic save on create/edit/delete
- ✅ Data persists across app sessions
- ✅ Export notes to JSON file (Downloads folder)

### 🌙 **Theme & Customization**
- ✅ Dark Mode with complete theme switching
- ✅ Persistent theme preference
- ✅ Light and dark color palettes
- ✅ Smooth theme transitions

### 🏷️ **Categories**
- ✅ 7 color-coded categories (Work, Personal, Ideas, Important, Shopping, Study, None)
- ✅ Visual category indicators (colored bar on note cards)
- ✅ Category selector in create/edit dialogs
- ✅ Categories visible at a glance

### 📌 **Pin Notes**
- ✅ Pin/unpin important notes
- ✅ Pinned notes always appear at the top
- ✅ Visual pin indicator on pinned notes
- ✅ Pin/Unpin button in edit dialog

---

## 🧠 Key Learning Goals

- ✅ Understand the **Android project structure** (`manifests`, `java`, `res`, Gradle)
- ✅ Design responsive interfaces using **XML layouts**
- ✅ Manage **Activity lifecycle** and user interactions
- ✅ Display dynamic lists with **RecyclerView** and custom adapters
- ✅ Implement **local data storage** with SharedPreferences
- ✅ Handle user input with **SearchView** and **Toolbar menus**
- ✅ Implement **sorting and filtering** algorithms
- ✅ Use **Java Streams** for data manipulation
- ✅ Practice **version control** and project documentation on GitHub

---

## 🏗️ Tech Stack

| Category           | Technology                                    |
|--------------------|-----------------------------------------------|
| Language           | **Java (Android SDK)**                        |
| IDE                | **Android Studio (Latest)**                   |
| UI Components      | XML Layouts + Material Design 3               |
| Layout             | ConstraintLayout, LinearLayout                |
| Database           | **Room 2.6.1** (SQLite abstraction)           |
| Architecture       | **MVVM** (Model-View-ViewModel)               |
| Lifecycle          | **ViewModel + LiveData 2.7.0**                |
| Async Operations   | AsyncTask (Room operations)                   |
| Version Control    | Git & GitHub                                  |
| Min SDK            | API 21 (Android 5.0)                          |
| Target SDK         | API 34 (Android 14)                           |

---

## 🏛️ Architecture: MVVM Pattern

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern with **Room Database** for robust and scalable data management.

### **Data Flow:**

```
┌──────────────────────────────────────────────────────┐
│                    MainActivity                       │
│                     (View Layer)                      │
│  - Observes LiveData                                 │
│  - Updates UI automatically                          │
│  - Handles user interactions                         │
└───────────────────┬──────────────────────────────────┘
                    │ observe()
                    ↓
┌──────────────────────────────────────────────────────┐
│                   NoteViewModel                       │
│                (ViewModel Layer)                      │
│  - Holds UI-related data (survives rotation)         │
│  - Provides LiveData to UI                           │
│  - Manages business logic                            │
└───────────────────┬──────────────────────────────────┘
                    │ delegates to
                    ↓
┌──────────────────────────────────────────────────────┐
│                  NoteRepository                       │
│               (Repository Pattern)                    │
│  - Abstracts data sources                            │
│  - Executes AsyncTasks for background operations     │
│  - Single source of truth                            │
└───────────────────┬──────────────────────────────────┘
                    │ uses
                    ↓
┌──────────────────────────────────────────────────────┐
│                     NoteDao                           │
│              (Data Access Object)                     │
│  - SQL queries (@Query)                              │
│  - CRUD operations (@Insert, @Update, @Delete)       │
│  - Returns LiveData for automatic updates            │
└───────────────────┬──────────────────────────────────┘
                    │ executes on
                    ↓
┌──────────────────────────────────────────────────────┐
│                  Room Database                        │
│                  (SQLite Layer)                       │
│  - note_table with 5 columns                         │
│  - Type-safe SQL queries                             │
│  - Compile-time verification                         │
└──────────────────────────────────────────────────────┘
```

### **Key Benefits:**

- ✅ **Separation of Concerns:** UI, logic, and data are completely separated
- ✅ **Testability:** Each layer can be tested independently
- ✅ **Lifecycle Awareness:** ViewModel survives configuration changes (rotation)
- ✅ **Reactive UI:** LiveData automatically updates the UI when data changes
- ✅ **Type Safety:** Room provides compile-time SQL verification
- ✅ **Scalability:** Easy to add new features without breaking existing code

---

## 📂 Project Structure

```
QuickNotes/
├── app/src/main/
│   ├── java/com/laylarodas/quicknotes/
│   │   ├── MainActivity.java            # Main UI (View layer)
│   │   ├── model/
│   │   │   └── Note.java               # Note entity with @Entity annotation
│   │   ├── database/
│   │   │   ├── NoteDao.java            # Data Access Object (@Dao)
│   │   │   ├── NoteDatabase.java       # Room database singleton (@Database)
│   │   │   └── NoteRepository.java     # Repository pattern implementation
│   │   ├── viewmodel/
│   │   │   └── NoteViewModel.java      # ViewModel with LiveData
│   │   ├── ui/
│   │   │   └── NoteAdapter.java        # RecyclerView adapter
│   │   └── utils/
│   │       └── DateUtils.java          # Time formatting utilities
│   └── res/
│       ├── layout/
│       │   ├── activity_main.xml       # Main screen with Toolbar & SearchView
│       │   ├── item_note.xml           # Note card layout with details
│       │   └── dialog_new_note.xml     # Create/Edit dialog
│       ├── menu/
│       │   └── main_menu.xml           # Toolbar menu (search, sort options)
│       └── values/
│           └── colors.xml              # Purple theme colors
└── build.gradle                         # Dependencies (Room, Lifecycle)
```

---

## 🚀 Current Progress

### ✅ **Phase 1 - Core Functionality** (Completed)
- ✅ Unique IDs and timestamps for notes
- ✅ Enhanced note view with title, content preview, and date
- ✅ Empty state with contextual messages
- ✅ Relative time formatting utility

### ✅ **Phase 2 - Advanced Features** (Completed)
- ✅ Real-time search functionality
- ✅ Multiple sorting options
- ✅ Share notes integration
- ✅ Toolbar with menu options

### ✅ **Phase 3 - Professional Architecture** (Completed)
- ✅ **Migrated to Room Database** (SQLite)
- ✅ **Implemented MVVM architecture** with ViewModel and LiveData
- ✅ **Repository Pattern** for data abstraction
- ✅ **Automatic UI updates** with LiveData observers
- ✅ **State persistence** survives screen rotation
- ✅ **AsyncTask** for background database operations
- ✅ **SQL queries** for fast search and sorting
- ✅ **Unlimited note capacity** (vs SharedPreferences 1MB limit)

### ✅ **Phase 4 - Advanced Features** (Completed)
- ✅ **Dark Mode** with complete theme switching and persistent preference
- ✅ **Color-coded Categories** (7 categories: Work, Personal, Ideas, Important, Shopping, Study, None)
- ✅ **Pin Notes** functionality to keep important notes at the top
- ✅ **Export Notes** to JSON file in Downloads folder
- ✅ **Visual indicators** for categories (colored bar) and pinned notes (pin icon)
- ✅ **Category selector** in create/edit dialogs
- ✅ **Pin/Unpin button** in edit dialog
- ✅ **Database version 3** with category and isPinned fields

---

## 📘 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/laylarodas/quicknotes-android.git
   cd quicknotes-android
   ```

2. **Open in Android Studio:**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned folder

3. **Run the app:**
   - Connect an Android device or start an emulator (API 21+)
   - Click the **Run** button (▶️) or press `Shift + F10`
   - The app will install and launch automatically

4. **Build APK (optional):**
   ```bash
   ./gradlew assembleDebug
   ```
   APK location: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🎮 How to Use

1. **Create a note:** Tap the purple **+** button
2. **Edit a note:** Tap on any note card
3. **Delete a note:** Long-press on a note → confirm deletion
4. **Search notes:** Tap the 🔍 icon → type to filter
5. **Sort notes:** Tap the sort icon → choose sorting method
6. **Share a note:** Open a note → tap **📤 Compartir**

---

## 📸 Screenshots

*Coming soon...*

---

## 🐛 Known Issues

- None at the moment! 🎉

---

## 📝 License

This project is for educational purposes as part of the DAM program.  
Feel free to fork and learn from it!

---

## ✍️ Author

**Layla Rodas**  
💼 [GitHub Profile](https://github.com/laylarodas)  
🧭 **Android Development | Java | DAM Student**

---

> "Great things are built by starting small — and this is getting big! 🚀"
