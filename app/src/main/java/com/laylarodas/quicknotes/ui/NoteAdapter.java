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
import com.laylarodas.quicknotes.utils.DateUtils;

/**
 * Adapter básico para mostrar una lista de notas (texto simple).
 * Más adelante cambiaré List<String> por el modelo real (Note).
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final List<Note> notes = new ArrayList<>();
    private OnNoteClickListener onNoteClickListener;
    private OnNoteLongClickListener onNoteLongClickListener;

    // Interfaces para los listeners
    public interface OnNoteClickListener {
        void onNoteClick(Note note, int position);
    }

    public interface OnNoteLongClickListener {
        void onNoteLongClick(Note note, int position);
    }

    // Setters para los listeners
    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.onNoteClickListener = listener;
    }

    public void setOnNoteLongClickListener(OnNoteLongClickListener listener) {
        this.onNoteLongClickListener = listener;
    }

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
        Note note = notes.get(position);
        
        // Mostrar título
        holder.tvNoteTitle.setText(note.getTitle());
        
        // Mostrar contenido (o un mensaje si está vacío)
        String content = note.getContent();
        if (content == null || content.trim().isEmpty()) {
            holder.tvNoteContent.setText("Sin contenido");
            holder.tvNoteContent.setAlpha(0.5f);
        } else {
            holder.tvNoteContent.setText(content);
            holder.tvNoteContent.setAlpha(1.0f);
        }
        
        // Mostrar fecha de última modificación
        holder.tvNoteDate.setText(DateUtils.getTimeAgo(note.getModifiedAt()));
        
        // Configurar click listener para editar
        holder.itemView.setOnClickListener(v -> {
            if (onNoteClickListener != null) {
                onNoteClickListener.onNoteClick(note, position);
            }
        });
        
        // Configurar long click listener para eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (onNoteLongClickListener != null) {
                onNoteLongClickListener.onNoteLongClick(note, position);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount(){
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        final TextView tvNoteTitle;
        final TextView tvNoteContent;
        final TextView tvNoteDate;

        NoteViewHolder(@NonNull View itemView){
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNoteContent = itemView.findViewById(R.id.tvNoteContent);
            tvNoteDate = itemView.findViewById(R.id.tvNoteDate);
        }
    }

}
