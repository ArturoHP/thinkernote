package com.youi.thinkernote;

public class ConstNote {

    String titulo, descripcion;
    int id;

    public ConstNote(String titulo, String descripcion, int id) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
