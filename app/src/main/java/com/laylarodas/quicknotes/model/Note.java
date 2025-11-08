package com.laylarodas.quicknotes.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Note {
    private String id;
    private String title;
    private String content;
    private long createdAt;
    private long modifiedAt;

    public Note(String title, String content) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }
    
    // Constructor privado para fromJson
    private Note(String id, String title, String content, long createdAt, long modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

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

    public void setTitle(String title) {
        this.title = title;
        this.modifiedAt = System.currentTimeMillis();
    }
    
    public void setContent(String content) {
        this.content = content;
        this.modifiedAt = System.currentTimeMillis();
    }

    public JSONObject toJson() {// Note --> JSON
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

    public static Note fromJson(JSONObject obj) {
        try {
            String id = obj.optString("id", UUID.randomUUID().toString()); // Compatibilidad con notas antiguas
            String title = obj.getString("title");
            String content = obj.getString("content");
            long createdAt = obj.optLong("createdAt", System.currentTimeMillis());
            long modifiedAt = obj.optLong("modifiedAt", System.currentTimeMillis());
            return new Note(id, title, content, createdAt, modifiedAt);
        } catch (JSONException e) {
            return new Note("Error", "Invalid data");
        }
    }
}
