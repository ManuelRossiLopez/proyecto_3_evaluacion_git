package com.lovecode.models;

public class Tecnologia {

    private int id;
    private String nombreTecnoligia;
    private String tipo;

    public Tecnologia(
            int id,
            String nombreTecnoligia,
            String tipo
    ) {

        this.id = id;
        this.nombreTecnoligia = nombreTecnoligia;
        this.tipo = tipo;
    }

    @Override
    public String toString() {

        return nombreTecnoligia +
                " (" + tipo + ")";
    }
}