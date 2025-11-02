package com.laylarodas.quicknotes;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.Gravity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import com.laylarodas.quicknotes.model.Note;
import com.laylarodas.quicknotes.data.NotesStorage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ===== Quick test: load -> add -> save -> reload =====
        List<Note> notes = NotesStorage.load(this);// this --> Context de la Activity. Return empty si no hay nada guardado
        if (notes.isEmpty()) {// si esta vacio, agregamos nota de prueba
            notes.add(new Note("Hello QuickNotes", "First saved note!"));
            NotesStorage.save(this, notes);//guarda nota, serializa a JSON
        }

        List<Note> reloaded = NotesStorage.load(this); //volver a cargar, verifica guardado
        Log.d("QuickNotes", "Notes count after save: " + reloaded.size());
        Toast.makeText(this, "Notes: " + reloaded.size(), Toast.LENGTH_SHORT).show();
    }
}