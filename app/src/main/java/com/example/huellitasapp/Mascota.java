package com.example.huellitasapp;

import com.google.firebase.firestore.Exclude;

/**
 * Modelo de la colección "mascotas" de Firestore.
 * Requiere constructor vacío para que Firestore pueda convertir documentos con toObject().
 */
public class Mascota {

    private String id;          // ID del documento (no se guarda como campo)
    private String nombre;
    private String especie;     // Perro | Gato | Otro
    private int edad;           // años
    private boolean vacunado;
    private String estado;      // Disponible | En proceso | Adoptado
    private String descripcion;

    // Requerido por Firestore
    public Mascota() {
    }

    public Mascota(String nombre, String especie, int edad, boolean vacunado,
                   String estado, String descripcion) {
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.vacunado = vacunado;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isVacunado() {
        return vacunado;
    }

    public void setVacunado(boolean vacunado) {
        this.vacunado = vacunado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
