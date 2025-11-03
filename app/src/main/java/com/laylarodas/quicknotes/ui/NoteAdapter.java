package com.laylarodas.quicknotes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.laylarodas.quicknotes.R;
/**
 * Adapter básico para mostrar una lista de notas (texto simple).
 * Más adelante cambiaré List<String> por el modelo real (Note).
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final List<String> notes = new ArrayList<>();

    // --- ViewHolder: representa una fila de la lista ---
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNoteText;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteText = itemView.findViewById(R.id.tvNoteText);
        }
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // obtener un "inflador" a partir del contexto del padre
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // inflar el XML del item y obtener la View resultante
        View itemView = inflater.inflate(R.layout.item_note, parent, /* attachToRoot = */ false);
        // crear el ViewHolder pasándole la View del item
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        // obtener el texto correspondiente a esta posición
        String text = notes.get(position);

        // vincular ese texto con el TextView del ViewHolder
        holder.tvNoteText.setText(text);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    /** Reemplaza toda la lista de notas y redibuja el RecyclerView. */
    public void submitList(@androidx.annotation.Nullable List<String> newNotes) {
        notes.clear();                 // vacía la lista actual
        if (newNotes != null) {        // evita NullPointerException
            notes.addAll(newNotes);    // copia los datos nuevos
        }
        notifyDataSetChanged();        // avisa al RecyclerView que redibuje todo
    }

    /** Inserta una nota al inicio y anima la inserción. */
    public void addNote(@NonNull String text) {
        notes.add(0, text);      //  agrega en posición 0 (arriba)
        notifyItemInserted(0);   //  notifica solo ese cambio → animación
    }


}
