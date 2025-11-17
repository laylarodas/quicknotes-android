package com.laylarodas.quicknotes.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Entity de Room que representa una nota en la base de datos.
 * Room creará automáticamente una tabla llamada "note_table" con estas columnas.
 */
@Entity(tableName = "note_table")
public class Note {
    
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;
    
    @ColumnInfo(name = "title")
    private String title;
    
    @ColumnInfo(name = "content")
    private String content;
    
    @ColumnInfo(name = "createdAt")
    private long createdAt;
    
    @ColumnInfo(name = "modifiedAt")
    private long modifiedAt;
    
    @ColumnInfo(name = "category", defaultValue = "NONE")
    private String category;
    
    @ColumnInfo(name = "isPinned", defaultValue = "0")
    private boolean isPinned;

    /**
     * Constructor usado por Room para recrear objetos desde la base de datos.
     * Room requiere un constructor con todos los campos.
     */
    public Note(@NonNull String id, String title, String content, long createdAt, long modifiedAt, String category, boolean isPinned) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.category = category != null ? category : "NONE";
        this.isPinned = isPinned;
    }
    
    /**
     * Constructor de conveniencia para crear notas nuevas.
     * @Ignore le dice a Room que ignore este constructor.
     */
    @Ignore
    public Note(String title, String content) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
        this.category = "NONE";
        this.isPinned = false;
    }

    // ==================== GETTERS ====================
    
    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getModifiedAt() {
        return modifiedAt;
    }
    
    public String getCategory() {
        return category != null ? category : "NONE";
    }
    
    public boolean isPinned() {
        return isPinned;
    }

    // ==================== SETTERS ====================
    // Room necesita setters para todos los campos
    
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
        this.modifiedAt = System.currentTimeMillis();
    }
    
    public void setContent(String content) {
        this.content = content;
        this.modifiedAt = System.currentTimeMillis();
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    
    public void setCategory(String category) {
        this.category = category != null ? category : "NONE";
    }
    
    public void setPinned(boolean pinned) {
        this.isPinned = pinned;
    }

    // ==================== MÉTODOS DE MIGRACIÓN ====================
    // Estos métodos se mantendrán temporalmente para migrar datos de SharedPreferences a Room.
    // Se pueden eliminar después de la migración.
    
    /**
     * @deprecated Mantenido solo para migración de SharedPreferences. Usar Room en su lugar.
     */
    @Deprecated
    @Ignore
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("title", title);
            obj.put("content", content);
            obj.put("createdAt", createdAt);
            obj.put("modifiedAt", modifiedAt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @deprecated Mantenido solo para migración de SharedPreferences. Usar Room en su lugar.
     */
    @Deprecated
    @Ignore
    public static Note fromJson(JSONObject obj) {
        try {
            String id = obj.optString("id", UUID.randomUUID().toString());
            String title = obj.getString("title");
            String content = obj.getString("content");
            long createdAt = obj.optLong("createdAt", System.currentTimeMillis());
            long modifiedAt = obj.optLong("modifiedAt", System.currentTimeMillis());
            String category = obj.optString("category", "NONE");
            boolean isPinned = obj.optBoolean("isPinned", false);
            return new Note(id, title, content, createdAt, modifiedAt, category, isPinned);
        } catch (JSONException e) {
            return new Note("Error", "Invalid data");
        }
    }
}
