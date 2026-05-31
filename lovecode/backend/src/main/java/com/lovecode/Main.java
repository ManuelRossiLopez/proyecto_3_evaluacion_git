package com.lovecode;

/**
 * Punto de entrada de LoveCode.
 * Arranca el servidor HTTP en el puerto 8080.
 */
public class Main {

    public static void main(String[] args) {

        try {
            Servidor.iniciar();
        } catch (Exception e) {
            System.out.println("Error al arrancar el servidor:");
            e.printStackTrace();
        }
    }
}