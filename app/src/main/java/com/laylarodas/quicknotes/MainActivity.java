package com.laylarodas.quicknotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.laylarodas.quicknotes.model.Note;
import com.laylarodas.quicknotes.data.NotesStorage;
import com.laylarodas.quicknotes.ui.NoteAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter adapter;
    private List<Note> notes = new ArrayList<>();
    private List<Note> filteredNotes = new ArrayList<>();
    private View layoutEmptyState;
    private TextView tvEmptyIcon;
    private TextView tvEmptyTitle;
    private TextView tvEmptyMessage;
    private SearchView searchView;
    private String currentSortMode = "modified"; // modified, created, title_asc, title_desc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar RecyclerView
        RecyclerView rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el layout de estado vacío
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        tvEmptyIcon = findViewById(R.id.tvEmptyIcon);
        tvEmptyTitle = findViewById(R.id.tvEmptyTitle);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        // Configurar SearchView
        searchView = findViewById(R.id.searchView);
        setupSearchView();

        adapter = new NoteAdapter();
        rvNotes.setAdapter(adapter);

        List<Note> loaded = NotesStorage.load(this);
        if (loaded != null) notes = loaded;
        
        // Ordenar notas por defecto (más reciente primero)
        sortNotes();
        
        filteredNotes = new ArrayList<>(notes);
        adapter.submitList(new ArrayList<>(filteredNotes));
        
        // Actualizar estado vacío
        updateEmptyState();

        // Configurar listener para editar nota al hacer click
        adapter.setOnNoteClickListener((note, position) -> showEditNoteDialog(note, position));
        
        // Configurar listener para eliminar nota al hacer long-press
        adapter.setOnNoteLongClickListener((note, position) -> showDeleteConfirmationDialog(note, position));

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
            
            // Ordenar y filtrar
            sortNotes();
            applySearch();
            updateEmptyState();
            
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

    private void showEditNoteDialog(Note note, int position) {
        // Inflar el layout del diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_note, null);
        
        // Obtener referencias a los campos de entrada
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextInputEditText etTitle = dialogView.findViewById(R.id.etNoteTitle);
        TextInputEditText etContent = dialogView.findViewById(R.id.etNoteContent);
        
        // Cambiar el título del diálogo
        tvDialogTitle.setText("Editar Nota");
        
        // Precargar los valores actuales de la nota
        etTitle.setText(note.getTitle());
        etContent.setText(note.getContent());
        
        // Crear el diálogo con botón de compartir
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setNeutralButton("📤 Compartir", (dialog, which) -> shareNote(note));
        AlertDialog dialog = builder.create();
        
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
            
            // Actualizar la nota existente
            note.setTitle(title);
            note.setContent(content);
            
            // Guardar en almacenamiento persistente
            NotesStorage.save(this, notes);
            
            // Ordenar y filtrar
            sortNotes();
            applySearch();
            updateEmptyState();
            
            // Mostrar mensaje de confirmación
            Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show();
            
            // Cerrar el diálogo
            dialog.dismiss();
        });
        
        // Configurar el botón Cancelar
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        // Mostrar el diálogo
        dialog.show();
    }

    private void showDeleteConfirmationDialog(Note note, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar nota")
                .setMessage("¿Estás seguro de que deseas eliminar esta nota?\n\n\"" + note.getTitle() + "\"")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    // Eliminar la nota de la lista original (buscar por ID para evitar problemas con posiciones filtradas)
                    Note noteToDelete = filteredNotes.get(position);
                    notes.removeIf(n -> n.getId().equals(noteToDelete.getId()));
                    
                    // Guardar en almacenamiento persistente
                    NotesStorage.save(this, notes);
                    
                    // Ordenar y filtrar
                    sortNotes();
                    applySearch();
                    updateEmptyState();
                    
                    // Mostrar mensaje de confirmación
                    Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Actualiza la visibilidad del estado vacío según si hay notas o no
     */
    private void updateEmptyState() {
        if (filteredNotes.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            
            // Verificar si hay una búsqueda activa
            String query = searchView.getQuery().toString().trim();
            if (!query.isEmpty()) {
                // Búsqueda sin resultados
                tvEmptyIcon.setText("🔍");
                tvEmptyTitle.setText("No se encontraron notas");
                tvEmptyMessage.setText("No hay notas que coincidan con \"" + query + "\"");
            } else if (notes.isEmpty()) {
                // No hay notas en absoluto
                tvEmptyIcon.setText("📝");
                tvEmptyTitle.setText("No tienes notas aún");
                tvEmptyMessage.setText("Presiona el botón + para crear tu primera nota");
            } else {
                // Hay notas pero el filtro/orden no muestra ninguna (caso raro)
                tvEmptyIcon.setText("🔍");
                tvEmptyTitle.setText("No se encontraron notas");
                tvEmptyMessage.setText("Intenta cambiar el criterio de ordenamiento");
            }
        } else {
            layoutEmptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_search) {
            // Mostrar/ocultar SearchView
            if (searchView.getVisibility() == View.VISIBLE) {
                searchView.setVisibility(View.GONE);
                searchView.setQuery("", false);
                applySearch();
            } else {
                searchView.setVisibility(View.VISIBLE);
                searchView.requestFocus();
            }
            return true;
        } else if (id == R.id.sort_by_modified) {
            currentSortMode = "modified";
            item.setChecked(true);
            sortNotes();
            applySearch();
            Toast.makeText(this, "Ordenado por fecha de modificación", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_created) {
            currentSortMode = "created";
            item.setChecked(true);
            sortNotes();
            applySearch();
            Toast.makeText(this, "Ordenado por fecha de creación", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_title_asc) {
            currentSortMode = "title_asc";
            item.setChecked(true);
            sortNotes();
            applySearch();
            Toast.makeText(this, "Ordenado alfabéticamente (A-Z)", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_title_desc) {
            currentSortMode = "title_desc";
            item.setChecked(true);
            sortNotes();
            applySearch();
            Toast.makeText(this, "Ordenado alfabéticamente (Z-A)", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    /**
     * Configura el SearchView para búsqueda en tiempo real
     */
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applySearch();
                return true;
            }
        });
    }

    /**
     * Ordena las notas según el modo actual
     */
    private void sortNotes() {
        switch (currentSortMode) {
            case "modified":
                notes.sort((n1, n2) -> Long.compare(n2.getModifiedAt(), n1.getModifiedAt()));
                break;
            case "created":
                notes.sort((n1, n2) -> Long.compare(n2.getCreatedAt(), n1.getCreatedAt()));
                break;
            case "title_asc":
                notes.sort((n1, n2) -> n1.getTitle().compareToIgnoreCase(n2.getTitle()));
                break;
            case "title_desc":
                notes.sort((n1, n2) -> n2.getTitle().compareToIgnoreCase(n1.getTitle()));
                break;
        }
    }

    /**
     * Aplica el filtro de búsqueda actual
     */
    private void applySearch() {
        String query = searchView.getQuery().toString().toLowerCase().trim();
        
        if (query.isEmpty()) {
            // Sin búsqueda, mostrar todas las notas
            filteredNotes = new ArrayList<>(notes);
        } else {
            // Filtrar notas por título o contenido
            filteredNotes = notes.stream()
                    .filter(note -> 
                        note.getTitle().toLowerCase().contains(query) ||
                        note.getContent().toLowerCase().contains(query)
                    )
                    .collect(Collectors.toList());
        }
        
        adapter.submitList(new ArrayList<>(filteredNotes));
        updateEmptyState();
    }

    /**
     * Comparte una nota usando el sistema de compartir de Android
     */
    private void shareNote(Note note) {
        // Preparar el contenido a compartir
        String shareText = note.getTitle();
        if (note.getContent() != null && !note.getContent().trim().isEmpty()) {
            shareText += "\n\n" + note.getContent();
        }
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        // Crear el selector con título personalizado
        try {
            startActivity(Intent.createChooser(shareIntent, "Compartir nota mediante"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No hay aplicaciones para compartir", Toast.LENGTH_SHORT).show();
        }
    }

}