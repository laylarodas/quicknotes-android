# 📱 QuickNotes — Android Notes App (Java)

**QuickNotes** is a lightweight and feature-rich Android app built with **Java** and **Material Design**.  
It was developed as a practice project to strengthen core Android fundamentals:  
Activities, RecyclerView, local data persistence, and modern UI/UX patterns.

<p align="center">
  <img src="https://img.shields.io/badge/Android-Java-green?logo=android" alt="Android Java">
  <img src="https://img.shields.io/badge/API-21%2B-blue" alt="API 21+">
  <img src="https://img.shields.io/badge/Material%20Design-3-purple" alt="Material Design">
</p>

---

## 🎯 Purpose

This project is part of my Android learning path within the **DAM program**.  
It focuses on understanding the **native Android architecture**, clean UI design,  
and efficient state handling with modern Android best practices.

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
- ✅ Local storage with SharedPreferences (JSON)
- ✅ Automatic save on create/edit/delete
- ✅ Data persists across app sessions

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
| Data Persistence   | SharedPreferences (JSON serialization)        |
| Version Control    | Git & GitHub                                  |
| Architecture       | MVC with custom utilities                     |
| Min SDK            | API 21 (Android 5.0)                          |
| Target SDK         | API 34 (Android 14)                           |

---

## 📂 Project Structure

```
QuickNotes/
├── app/src/main/
│   ├── java/com/laylarodas/quicknotes/
│   │   ├── MainActivity.java           # Main activity with search, sort, and CRUD
│   │   ├── model/
│   │   │   └── Note.java              # Note model with UUID and timestamps
│   │   ├── data/
│   │   │   └── NotesStorage.java      # SharedPreferences data layer
│   │   ├── ui/
│   │   │   └── NoteAdapter.java       # RecyclerView adapter
│   │   └── utils/
│   │       └── DateUtils.java         # Time formatting utilities
│   └── res/
│       ├── layout/
│       │   ├── activity_main.xml      # Main screen layout
│       │   ├── item_note.xml          # Note card layout
│       │   └── dialog_new_note.xml    # Create/Edit dialog
│       ├── menu/
│       │   └── main_menu.xml          # Toolbar menu (search, sort)
│       └── values/
│           └── colors.xml             # Purple theme colors
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

### 🔜 **Phase 3 - Next Steps** (Planned)
- 🔜 Migrate to Room Database
- 🔜 Implement MVVM architecture with ViewModel
- 🔜 Add dark mode support
- 🔜 Color-coded categories or tags
- 🔜 Pin important notes
- 🔜 Export/Import notes (backup)

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
