package com.laylarodas.quicknotes.model;

/**
 * Define las categorías disponibles para las notas con sus colores asociados.
 */
public enum NoteCategory {
    NONE("Sin categoría", "#6750A4"),           // Púrpura (default)
    WORK("Trabajo", "#1976D2"),                 // Azul
    PERSONAL("Personal", "#388E3C"),            // Verde
    IDEAS("Ideas", "#FBC02D"),                  // Amarillo
    IMPORTANT("Importante", "#D32F2F"),         // Rojo
    SHOPPING("Compras", "#00796B"),             // Verde azulado
    STUDY("Estudio", "#7B1FA2");                // Púrpura oscuro
    
    private final String displayName;
    private final String colorHex;
    
    NoteCategory(String displayName, String colorHex) {
        this.displayName = displayName;
        this.colorHex = colorHex;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColorHex() {
        return colorHex;
    }
    
    /**
     * Obtiene una categoría por su nombre.
     * Si no existe, devuelve NONE.
     */
    public static NoteCategory fromString(String name) {
        if (name == null || name.isEmpty()) {
            return NONE;
        }
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}

