package com.laylarodas.quicknotes.data;

import android.content.Context; //objeto global de la app
import android.content.SharedPreferences; //guardar datos persistentes simples (pares clave-valor)

import com.laylarodas.quicknotes.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotesStorage {
    private static final String PREFS_NAME = "quicknotes_prefs";
    private static final String KEY_NOTES_JSON = "notes_json";


    private static SharedPreferences prefs(Context ctx){ //abre el archivo quicknotes_prefs.xml. Tipo de dato Archivo de Preferencias
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static List<Note> load(Context ctx){
        //lee del almacenamiento local el texto (JSON) --> busca el valor guardado con la clave "note_json"
        String raw = prefs(ctx).getString(KEY_NOTES_JSON, "[]");
        List<Note> result = new ArrayList<>();//lista vacia para guardar los objetos Note

        //bloque try/catch, trabajar con json puede generar errores
        try {
            JSONArray arr = new JSONArray(raw);// convierte el texto leído (String JSON)
            for (int i = 0; i < arr.length(); i++) {// arr contiene cada nota como un JSONObject
                JSONObject obj = arr.getJSONObject(i);//obtiene la nota en un JSONObject
                result.add(Note.fromJson(obj));//llama al metodo fromJson()-->Note. Convierte el objeto json en un tipo Note
            }
        } catch (JSONException e) {
            result = new ArrayList<>();
        }
        return result;
    }

    public static void save(Context ctx, List<Note> notes){
        JSONArray arr = new JSONArray();//almacena cada Note convertida en JSON
        for (Note n : notes) {
            arr.put(n.toJson());//cada Note --> JSONObject
        }
        prefs(ctx).edit() //abrir editor
                .putString(KEY_NOTES_JSON, arr.toString())/* guardamos todo el arreglo convertido a string bajo la clave "notes_json" */
                .apply();
    }

}
