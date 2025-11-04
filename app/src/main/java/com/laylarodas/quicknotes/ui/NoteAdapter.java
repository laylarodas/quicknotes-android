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
import com.laylarodas.quicknotes.model.Note;

/**
 * Adapter básico para mostrar una lista de notas (texto simple).
 * Más adelante cambiaré List<String> por el modelo real (Note).
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final List<Note> notes = new ArrayList<>();

    public void submitList(List<Note> newNotes){
        notes.clear();
        if (newNotes != null) notes.addAll(newNotes);
        notifyDataSetChanged();
    }

    public void addNote(Note note){
        notes.add(0,note);
        notifyItemInserted(0);
    }

    public Note getNote(int position){
        return notes.get(position);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position){
        holder.tvNoteText.setText(notes.get(position).getTitle());
    }

    @Override
    public int getItemCount(){
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        final TextView tvNoteText;

        NoteViewHolder(@NonNull View itemView){
            super(itemView);
            tvNoteText = itemView.findViewById(R.id.tvNoteText);
        }
    }

}
