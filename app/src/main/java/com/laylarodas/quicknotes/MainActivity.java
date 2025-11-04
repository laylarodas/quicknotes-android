package com.laylarodas.quicknotes;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import com.laylarodas.quicknotes.model.Note;
import com.laylarodas.quicknotes.data.NotesStorage;
import com.laylarodas.quicknotes.ui.NoteAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter adapter;

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

        List<String> demoNotes = Arrays.asList(
                "Buy milk",
                "Call Julia about schedule",
                "Draft QuickNotes README");
        adapter.submitList(demoNotes);

        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(v -> {
            adapter.addNote("New note " + (adapter.getItemCount() + 1));
        });

        // ===== Quick test: load -> add -> save -> reload =====
        List<Note> notes = NotesStorage.load(this);// this --> Context de la Activity. Return empty si no hay nada
                                                   // guardado
        if (notes.isEmpty()) {// si esta vacio, agregamos nota de prueba
            notes.add(new Note("Hello QuickNotes", "First saved note!"));
            NotesStorage.save(this, notes);// guarda nota, serializa a JSON
        }

        List<Note> reloaded = NotesStorage.load(this); // volver a cargar, verifica guardado
        Log.d("QuickNotes", "Notes count after save: " + reloaded.size());
        Toast.makeText(this, "Notes: " + reloaded.size(), Toast.LENGTH_SHORT).show();

    }

}