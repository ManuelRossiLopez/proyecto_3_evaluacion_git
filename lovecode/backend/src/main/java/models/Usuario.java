package com.lovecode.models;

import java.sql.Date;

public class Usuario {

    private int id;
    private String nombre;
    private String apellido1;
    private String apellido;
    private String email;
    private String contrasena;
    private Date fechaNacimiento;

    public Usuario(
            int id,
            String nombre,
            String apellido1,
            String apellido,
            String email,
            String contrasena,
            Date fechaNacimiento
    ) {

        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    @Override
    public String toString() {

        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}