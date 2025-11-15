package com.laylarodas.quicknotes.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.laylarodas.quicknotes.model.Note;

import java.util.List;

/**
 * Repository - Patrón de repositorio para abstraer el acceso a datos.
 * 
 * El ViewModel NO sabe de dónde vienen los datos (Room, API, cache, etc).
 * El Repository decide la fuente de datos y maneja toda la lógica.
 * 
 * Beneficios:
 * - Punto único de acceso a datos
 * - Fácil de testear (puedes hacer mock del Repository)
 * - Puedes cambiar la fuente de datos sin afectar el ViewModel
 * - Maneja operaciones en background automáticamente
 */
public class NoteRepository {
    
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private String currentSortMode = "modified"; // Por defecto: más reciente primero
    
    /**
     * Constructor: Inicializa el DAO y obtiene las notas
     */
    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        // Por defecto cargamos notas ordenadas por modificación
        allNotes = noteDao.getAllNotesByModified();
    }
    
    // ==================== OPERACIONES CRUD ====================
    // Todas las operaciones de escritura se ejecutan en background (AsyncTask)
    
    /**
     * Inserta una nota nueva en la base de datos.
     * Se ejecuta en un hilo de background automáticamente.
     */
    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    
    /**
     * Actualiza una nota existente.
     */
    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    
    /**
     * Elimina una nota.
     */
    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    
    /**
     * Elimina todas las notas (útil para testing).
     */
    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }
    
    // ==================== CONSULTAS CON LIVEDATA ====================
    
    /**
     * Obtiene todas las notas con el ordenamiento actual.
     * LiveData: La UI se actualiza automáticamente cuando cambian los datos.
     */
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
    
    /**
     * Cambia el ordenamiento de las notas.
     * @param sortMode: "modified", "created", "title_asc", "title_desc"
     */
    public void setSortMode(String sortMode) {
        this.currentSortMode = sortMode;
        switch (sortMode) {
            case "modified":
                allNotes = noteDao.getAllNotesByModified();
                break;
            case "created":
                allNotes = noteDao.getAllNotesByCreated();
                break;
            case "title_asc":
                allNotes = noteDao.getAllNotesByTitleAsc();
                break;
            case "title_desc":
                allNotes = noteDao.getAllNotesByTitleDesc();
                break;
            default:
                allNotes = noteDao.getAllNotesByModified();
        }
    }
    
    /**
     * Busca notas por texto.
     * @param query: Texto a buscar (sin %, el Repository lo agrega)
     */
    public LiveData<List<Note>> searchNotes(String query) {
        // Agregamos los wildcards % para búsqueda parcial
        String searchQuery = "%" + query + "%";
        return noteDao.searchNotes(searchQuery);
    }
    
    /**
     * Inserta múltiples notas (para migración desde SharedPreferences).
     */
    public void insertAll(List<Note> notes) {
        new InsertAllNotesAsyncTask(noteDao).execute(notes);
    }
    
    // ==================== ASYNCTASKS PARA OPERACIONES EN BACKGROUND ====================
    // Room no permite operaciones de escritura en el hilo principal
    // AsyncTask mueve estas operaciones a un hilo de background
    
    /**
     * AsyncTask para insertar una nota en background.
     */
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        
        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    
    /**
     * AsyncTask para actualizar una nota en background.
     */
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        
        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    
    /**
     * AsyncTask para eliminar una nota en background.
     */
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        
        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    
    /**
     * AsyncTask para eliminar todas las notas en background.
     */
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        
        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
    
    /**
     * AsyncTask para insertar múltiples notas en background (para migración).
     */
    private static class InsertAllNotesAsyncTask extends AsyncTask<List<Note>, Void, Void> {
        private NoteDao noteDao;
        
        private InsertAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        
        @Override
        protected Void doInBackground(List<Note>... lists) {
            noteDao.insertAll(lists[0]);
            return null;
        }
    }
}

