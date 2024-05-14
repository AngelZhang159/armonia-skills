package com.dam.armoniaskills.model;

import android.net.Uri;

import java.util.List;
import java.util.UUID;

public class Skill {

    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private String precio;
    private String ciudad;
    private UUID usuarioId;
    private List<Uri> imagenes;

    public Skill(List<Uri> imagenes, String titulo, String descripcion, String precio, Categoria categoria, String ciudad, UUID usuarioId) {
        this.imagenes = imagenes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.ciudad = ciudad;
        this.usuarioId = usuarioId;
    }

    public List<Uri> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Uri> imagenes) {
        this.imagenes = imagenes;
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
