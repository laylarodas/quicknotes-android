package com.laylarodas.quicknotes.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Note {
    private String title;
    private String content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public JSONObject toJson() {// Note --> JSON
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
            obj.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Note fromJson(JSONObject obj) {
        try {
            String title = obj.getString("title");
            String content = obj.getString("content");
            return new Note(title, content);
        } catch (JSONException e) {
            return new Note("Error", "Invalid data");
        }
    }
}
