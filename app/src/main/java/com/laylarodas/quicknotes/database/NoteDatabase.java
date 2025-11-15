package com.laylarodas.quicknotes.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.laylarodas.quicknotes.model.Note;

/**
 * Clase de base de datos de Room para QuickNotes.
 * 
 * @Database define:
 * - entities: Las tablas de la BD (por ahora solo Note)
 * - version: Número de versión de la BD (incrementar si cambias la estructura)
 * - exportSchema: Si exportar el esquema (false en desarrollo)
 * 
 * Patrón Singleton: Solo una instancia de la BD en toda la app.
 */
@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    
    // Instancia única (Singleton)
    private static NoteDatabase instance;
    
    /**
     * Room implementará automáticamente este método.
     * Retorna una instancia de NoteDao.
     */
    public abstract NoteDao noteDao();
    
    /**
     * Obtiene la instancia única de la base de datos.
     * Si no existe, la crea.
     * 
     * synchronized: Evita problemas de concurrencia (que dos threads creen la BD a la vez)
     */
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    "note_database"  // Nombre del archivo de BD
            )
            // Callback para ejecutar código cuando se crea la BD
            .addCallback(roomCallback)
            // En producción usarías migraciones, pero en desarrollo:
            .fallbackToDestructiveMigration()  // Borra y recrea si hay conflicto
            .build();
        }
        return instance;
    }
    
    /**
     * Callback que se ejecuta cuando Room crea la base de datos por primera vez.
     * Aquí puedes insertar datos iniciales si lo deseas.
     */
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Aquí puedes insertar datos iniciales si lo deseas
            // Por ejemplo, notas de ejemplo para la primera vez
            // new PopulateDbAsyncTask(instance).execute();
        }
    };
    
    // AsyncTask para poblar la BD con datos iniciales (opcional)
    /*
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        
        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }
        
        @Override
        protected Void doInBackground(Void... voids) {
            // Insertar notas de ejemplo
            noteDao.insert(new Note("Bienvenido a QuickNotes", "Esta es tu primera nota"));
            return null;
        }
    }
    */
}

