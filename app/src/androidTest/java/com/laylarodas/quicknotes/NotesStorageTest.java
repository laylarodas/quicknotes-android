package com.laylarodas.quicknotes;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import com.laylarodas.quicknotes.data.NotesStorage;
import com.laylarodas.quicknotes.model.Note;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class NotesStorageTest {

    @Test
    public void testSaveAndLoadNotes() {
        // Obtener contexto de la app (necesario para acceder a SharedPreferences)
        Context ctx = ApplicationProvider.getApplicationContext();

        // Crear una nota de prueba
        Note note = new Note("Test Title", "Test Content");
        List<Note> notes = java.util.Collections.singletonList(note);

        // Guardar la lista
        NotesStorage.save(ctx, notes);

        // Volver a cargar las notas desde almacenamiento
        List<Note> loaded = NotesStorage.load(ctx);

        // Verificar que los datos guardados y cargados coincidan
        assertEquals(1, loaded.size());
        assertEquals("Test Title", loaded.get(0).getTitle());
        assertEquals("Test Content", loaded.get(0).getContent());
    }
}

