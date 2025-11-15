package com.laylarodas.quicknotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.laylarodas.quicknotes.database.NoteRepository;
import com.laylarodas.quicknotes.model.Note;

import java.util.List;

/**
 * ViewModel para gestionar los datos de las notas.
 * 
 * Responsabilidades:
 * - Mantiene la lógica de negocio
 * - Provee datos a la UI a través de LiveData
 * - Sobrevive a cambios de configuración (rotación de pantalla)
 * - NO tiene referencias a Views/Activities (evita memory leaks)
 * 
 * AndroidViewModel: Versión de ViewModel que tiene acceso al Application context
 * (necesario para inicializar Room)
 */
public class NoteViewModel extends AndroidViewModel {
    
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    
    // LiveData para el query de búsqueda actual
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    
    // LiveData para el modo de ordenamiento actual
    private String currentSortMode = "modified";
    
    /**
     * Constructor: Inicializa el Repository y carga las notas
     */
    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }
    
    // ==================== OPERACIONES CRUD ====================
    // El ViewModel delega todas las operaciones al Repository
    
    /**
     * Inserta una nueva nota.
     * @param title Título de la nota
     * @param content Contenido de la nota
     */
    public void insert(String title, String content) {
        Note note = new Note(title, content);
        repository.insert(note);
    }
    
    /**
     * Actualiza una nota existente.
     */
    public void update(Note note) {
        repository.update(note);
    }
    
    /**
     * Elimina una nota.
     */
    public void delete(Note note) {
        repository.delete(note);
    }
    
    /**
     * Elimina todas las notas (útil para testing).
     */
    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }
    
    // ==================== LIVEDATA OBSERVABLES ====================
    
    /**
     * Obtiene todas las notas según el ordenamiento y búsqueda actual.
     * La UI observa este LiveData y se actualiza automáticamente.
     */
    public LiveData<List<Note>> getAllNotes() {
        String query = searchQuery.getValue();
        if (query != null && !query.trim().isEmpty()) {
            // Si hay búsqueda activa, retornar resultados filtrados
            return repository.searchNotes(query);
        }
        // Si no hay búsqueda, retornar todas las notas
        return allNotes;
    }
    
    /**
     * Obtiene las notas filtradas por búsqueda.
     * Usa Transformations.switchMap para cambiar dinámicamente el LiveData según la query.
     */
    public LiveData<List<Note>> getSearchResults() {
        return Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                return allNotes;
            } else {
                return repository.searchNotes(query);
            }
        });
    }
    
    // ==================== BÚSQUEDA ====================
    
    /**
     * Establece el texto de búsqueda.
     * Al cambiar este valor, el LiveData se actualiza automáticamente.
     */
    public void setSearchQuery(String query) {
        searchQuery.setValue(query == null ? "" : query);
    }
    
    /**
     * Obtiene el query de búsqueda actual.
     */
    public String getSearchQueryValue() {
        return searchQuery.getValue();
    }
    
    /**
     * Limpia la búsqueda (vuelve a mostrar todas las notas).
     */
    public void clearSearch() {
        searchQuery.setValue("");
    }
    
    // ==================== ORDENAMIENTO ====================
    
    /**
     * Cambia el modo de ordenamiento.
     * @param sortMode "modified", "created", "title_asc", "title_desc"
     */
    public void setSortMode(String sortMode) {
        if (!currentSortMode.equals(sortMode)) {
            currentSortMode = sortMode;
            repository.setSortMode(sortMode);
            allNotes = repository.getAllNotes();
        }
    }
    
    /**
     * Obtiene el modo de ordenamiento actual.
     */
    public String getCurrentSortMode() {
        return currentSortMode;
    }
    
    // ==================== MIGRACIÓN ====================
    
    /**
     * Inserta múltiples notas (útil para migrar desde SharedPreferences).
     */
    public void insertAll(List<Note> notes) {
        repository.insertAll(notes);
    }
}

