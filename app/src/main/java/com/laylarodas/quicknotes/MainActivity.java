package com.laylarodas.quicknotes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.laylarodas.quicknotes.model.Note;
import com.laylarodas.quicknotes.data.NotesStorage;
import com.laylarodas.quicknotes.ui.NoteAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter adapter;
    private List<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        RecyclerView rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoteAdapter();
        rvNotes.setAdapter(adapter);

        List<Note> loaded = NotesStorage.load(this);
        if (loaded == null) notes = loaded;
        adapter.submitList(new ArrayList<>(notes));

        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(v -> showNewNoteDialog());

        // ===== Quick test: load -> add -> save -> reload =====
        /*List<Note> notes = NotesStorage.load(this);// this --> Context de la Activity. Return empty si no hay nada
                                                   // guardado
        if (notes.isEmpty()) {// si esta vacio, agregamos nota de prueba
            notes.add(new Note("Hello QuickNotes", "First saved note!"));
            NotesStorage.save(this, notes);// guarda nota, serializa a JSON
        }

        List<Note> reloaded = NotesStorage.load(this); // volver a cargar, verifica guardado
        Log.d("QuickNotes", "Notes count after save: " + reloaded.size());
        Toast.makeText(this, "Notes: " + reloaded.size(), Toast.LENGTH_SHORT).show();
        */
    }

    private void showNewNoteDialog() {
        // Inflar el layout del diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_note, null);
        
        // Obtener referencias a los campos de entrada
        TextInputEditText etTitle = dialogView.findViewById(R.id.etNoteTitle);
        TextInputEditText etContent = dialogView.findViewById(R.id.etNoteContent);
        
        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        
        // Configurar el botón Guardar
        dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            
            // Validar que el título no esté vacío
            if (title.isEmpty()) {
                etTitle.setError("El título es obligatorio");
                etTitle.requestFocus();
                return;
            }
            
            // Crear y guardar la nueva nota
            Note newNote = new Note(title, content);
            notes.add(0, newNote);
            
            // Guardar en almacenamiento persistente
            NotesStorage.save(this, notes);
            
            // Actualizar el RecyclerView
            adapter.submitList(new ArrayList<>(notes));
            
            // Mostrar mensaje de confirmación
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show();
            
            // Cerrar el diálogo
            dialog.dismiss();
        });
        
        // Configurar el botón Cancelar
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        // Mostrar el diálogo
        dialog.show();
    }

}