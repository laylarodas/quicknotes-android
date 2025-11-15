package com.laylarodas.quicknotes;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.laylarodas.quicknotes.model.Note;
import com.laylarodas.quicknotes.ui.NoteAdapter;
import com.laylarodas.quicknotes.viewmodel.NoteViewModel;

import java.util.List;

/**
 * MainActivity - Activity principal que muestra la lista de notas.
 * 
 * Arquitectura MVVM:
 * - Esta Activity SOLO maneja la UI (sin lógica de negocio)
 * - Observa el ViewModel y actualiza la UI cuando los datos cambian
 * - NO accede directamente a la base de datos
 * - El ViewModel sobrevive a rotaciones de pantalla
 */
public class MainActivity extends AppCompatActivity {

    private NoteViewModel viewModel;
    private NoteAdapter adapter;
    private View layoutEmptyState;
    private TextView tvEmptyIcon;
    private TextView tvEmptyTitle;
    private TextView tvEmptyMessage;
    private SearchView searchView;
    private List<Note> currentNotes; // Lista actual para el estado vacío

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

        // Configurar adapter
        adapter = new NoteAdapter();
        rvNotes.setAdapter(adapter);

        // ==================== INICIALIZAR VIEWMODEL ====================
        // ViewModelProvider crea o recupera el ViewModel
        // Si ya existe (ej: después de rotación), reutiliza la instancia existente
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        
        // ==================== OBSERVAR LIVEDATA ====================
        // Observar los resultados de búsqueda
        // Cuando los datos cambian, esta función se ejecuta automáticamente
        viewModel.getSearchResults().observe(this, notes -> {
            // Actualizar el adapter con las nuevas notas
            adapter.submitList(notes);
            currentNotes = notes;
            // Actualizar el estado vacío
            updateEmptyState();
        });

        // Configurar listener para editar nota al hacer click
        adapter.setOnNoteClickListener((note, position) -> showEditNoteDialog(note));
        
        // Configurar listener para eliminar nota al hacer long-press
        adapter.setOnNoteLongClickListener((note, position) -> showDeleteConfirmationDialog(note));

        // Configurar FAB para crear notas
        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(v -> showNewNoteDialog());
    }

    /**
     * Muestra el diálogo para crear una nueva nota.
     */
    private void showNewNoteDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_note, null);
        
        TextInputEditText etTitle = dialogView.findViewById(R.id.etNoteTitle);
        TextInputEditText etContent = dialogView.findViewById(R.id.etNoteContent);
        
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        
        // Botón Guardar
        dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            
            // Validar título
            if (title.isEmpty()) {
                etTitle.setError("El título es obligatorio");
                etTitle.requestFocus();
                return;
            }
            
            // ¡MUCHO MÁS SIMPLE! Solo llamar al ViewModel
            viewModel.insert(title, content);
            
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        // Botón Cancelar
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }

    /**
     * Muestra el diálogo para editar una nota existente.
     * Ya no necesita la posición porque trabajamos con objetos Note directamente.
     */
    private void showEditNoteDialog(Note note) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_note, null);
        
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextInputEditText etTitle = dialogView.findViewById(R.id.etNoteTitle);
        TextInputEditText etContent = dialogView.findViewById(R.id.etNoteContent);
        
        tvDialogTitle.setText("Editar Nota");
        etTitle.setText(note.getTitle());
        etContent.setText(note.getContent());
        
        // Diálogo con botón de compartir
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setNeutralButton("📤 Compartir", (dialog, which) -> shareNote(note));
        AlertDialog dialog = builder.create();
        
        // Botón Guardar
        dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            
            if (title.isEmpty()) {
                etTitle.setError("El título es obligatorio");
                etTitle.requestFocus();
                return;
            }
            
            // Actualizar los campos de la nota
            note.setTitle(title);
            note.setContent(content);
            
            // ¡MUCHO MÁS SIMPLE! Solo llamar al ViewModel
            viewModel.update(note);
            
            Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        // Botón Cancelar
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }

    /**
     * Muestra el diálogo de confirmación para eliminar una nota.
     * Ya no necesita la posición.
     */
    private void showDeleteConfirmationDialog(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar nota")
                .setMessage("¿Estás seguro de que deseas eliminar esta nota?\n\n\"" + note.getTitle() + "\"")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    // ¡MUCHO MÁS SIMPLE! Solo llamar al ViewModel
                    viewModel.delete(note);
                    
                    Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Actualiza la visibilidad del estado vacío según si hay notas o no.
     * Mucho más simple que antes: solo verificamos currentNotes.
     */
    private void updateEmptyState() {
        if (currentNotes == null || currentNotes.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            
            String query = searchView.getQuery().toString().trim();
            if (!query.isEmpty()) {
                // Búsqueda sin resultados
                tvEmptyIcon.setText("🔍");
                tvEmptyTitle.setText("No se encontraron notas");
                tvEmptyMessage.setText("No hay notas que coincidan con \"" + query + "\"");
            } else {
                // No hay notas en absoluto
                tvEmptyIcon.setText("📝");
                tvEmptyTitle.setText("No tienes notas aún");
                tvEmptyMessage.setText("Presiona el botón + para crear tu primera nota");
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
                viewModel.clearSearch(); // Limpiar búsqueda
            } else {
                searchView.setVisibility(View.VISIBLE);
                searchView.requestFocus();
            }
            return true;
        } else if (id == R.id.sort_by_modified) {
            item.setChecked(true);
            viewModel.setSortMode("modified"); // ViewModel maneja el ordenamiento
            Toast.makeText(this, "Ordenado por fecha de modificación", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_created) {
            item.setChecked(true);
            viewModel.setSortMode("created");
            Toast.makeText(this, "Ordenado por fecha de creación", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_title_asc) {
            item.setChecked(true);
            viewModel.setSortMode("title_asc");
            Toast.makeText(this, "Ordenado alfabéticamente (A-Z)", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_title_desc) {
            item.setChecked(true);
            viewModel.setSortMode("title_desc");
            Toast.makeText(this, "Ordenado alfabéticamente (Z-A)", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    /**
     * Configura el SearchView para búsqueda en tiempo real.
     * Mucho más simple: solo actualiza el ViewModel.
     */
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // ¡MUCHO MÁS SIMPLE! Solo actualizar el ViewModel
                viewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    /**
     * Comparte una nota usando el sistema de compartir de Android.
     */
    private void shareNote(Note note) {
        String shareText = note.getTitle();
        if (note.getContent() != null && !note.getContent().trim().isEmpty()) {
            shareText += "\n\n" + note.getContent();
        }
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        try {
            startActivity(Intent.createChooser(shareIntent, "Compartir nota mediante"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No hay aplicaciones para compartir", Toast.LENGTH_SHORT).show();
        }
    }
}