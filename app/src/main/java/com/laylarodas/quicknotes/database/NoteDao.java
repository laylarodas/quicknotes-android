package com.laylarodas.quicknotes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laylarodas.quicknotes.model.Note;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Note.
 * Define todas las operaciones de base de datos disponibles para las notas.
 * Room generará automáticamente la implementación de esta interface.
 */
@Dao
public interface NoteDao {
    
    // ==================== OPERACIONES BÁSICAS (CRUD) ====================
    
    /**
     * Inserta una nota en la base de datos.
     * Si ya existe una nota con el mismo ID, falla.
     */
    @Insert
    void insert(Note note);
    
    /**
     * Actualiza una nota existente.
     * Busca por el ID de la nota y actualiza todos sus campos.
     */
    @Update
    void update(Note note);
    
    /**
     * Elimina una nota de la base de datos.
     */
    @Delete
    void delete(Note note);
    
    /**
     * Elimina TODAS las notas de la base de datos.
     * Útil para limpiar datos en pruebas.
     */
    @Query("DELETE FROM note_table")
    void deleteAllNotes();
    
    // ==================== CONSULTAS CON LIVEDATA ====================
    // LiveData permite observar cambios automáticamente en la UI
    
    /**
     * Obtiene TODAS las notas ordenadas por fecha de modificación (más reciente primero).
     * LiveData: La UI se actualiza automáticamente cuando cambian los datos.
     */
    @Query("SELECT * FROM note_table ORDER BY modifiedAt DESC")
    LiveData<List<Note>> getAllNotesByModified();
    
    /**
     * Obtiene todas las notas ordenadas por fecha de creación (más reciente primero).
     */
    @Query("SELECT * FROM note_table ORDER BY createdAt DESC")
    LiveData<List<Note>> getAllNotesByCreated();
    
    /**
     * Obtiene todas las notas ordenadas alfabéticamente por título (A-Z).
     */
    @Query("SELECT * FROM note_table ORDER BY title COLLATE NOCASE ASC")
    LiveData<List<Note>> getAllNotesByTitleAsc();
    
    /**
     * Obtiene todas las notas ordenadas alfabéticamente por título (Z-A).
     */
    @Query("SELECT * FROM note_table ORDER BY title COLLATE NOCASE DESC")
    LiveData<List<Note>> getAllNotesByTitleDesc();
    
    /**
     * Busca notas que contengan el texto especificado en el título o contenido.
     * El parámetro searchQuery debe incluir los wildcard % (ej: "%texto%")
     * COLLATE NOCASE hace la búsqueda case-insensitive (no distingue mayúsculas).
     */
    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery OR content LIKE :searchQuery ORDER BY modifiedAt DESC")
    LiveData<List<Note>> searchNotes(String searchQuery);
    
    // ==================== CONSULTAS SÍNCRONAS ====================
    // Para operaciones que no necesitan observar cambios (ej: migración)
    
    /**
     * Obtiene todas las notas de forma síncrona (sin LiveData).
     * Útil para migraciones o exportaciones.
     * ADVERTENCIA: No usar en el hilo principal.
     */
    @Query("SELECT * FROM note_table")
    List<Note> getAllNotesSync();
    
    /**
     * Inserta múltiples notas de una vez.
     * Útil para migración desde SharedPreferences.
     */
    @Insert
    void insertAll(List<Note> notes);
}

