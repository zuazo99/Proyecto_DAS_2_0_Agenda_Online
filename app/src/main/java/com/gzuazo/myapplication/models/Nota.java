package com.gzuazo.myapplication.models;

public class Nota {

    //El id de la nota --> correo Usuario + fecha_hora_actual
    String id_nota, uid_usuario, correo_usuario, fechaHoraActual, tituloNota, descripcion, fechaNota, estado;


    public Nota(){

    }

    public Nota(String id_nota, String uid_usuario, String correo_usuario, String fechaHoraActual, String tituloNota, String descripcion, String fechaNota, String estado) {
        this.id_nota = id_nota;
        this.uid_usuario = uid_usuario;
        this.correo_usuario = correo_usuario;
        this.fechaHoraActual = fechaHoraActual;
        this.tituloNota = tituloNota;
        this.descripcion = descripcion;
        this.fechaNota = fechaNota;
        this.estado = estado;
    }

    public String getId_nota() {
        return id_nota;
    }

    public void setId_nota(String id_nota) {
        this.id_nota = id_nota;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getFechaHoraActual() {
        return fechaHoraActual;
    }

    public void setFechaHoraActual(String fechaHoraActual) {
        this.fechaHoraActual = fechaHoraActual;
    }

    public String getTituloNota() {
        return tituloNota;
    }

    public void setTituloNota(String tituloNota) {
        this.tituloNota = tituloNota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaNota() {
        return fechaNota;
    }

    public void setFechaNota(String fechaNota) {
        this.fechaNota = fechaNota;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
