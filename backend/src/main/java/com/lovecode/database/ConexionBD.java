package com.lovecode.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://192.168.190.133:3306/love_code";

    private static final String USER = "miUsuario";
    private static final String PASSWORD = "miPassword";

    public static Connection conectar() {

        Connection conexion = null;

        try {
            // ← AÑADE ESTA LÍNEA — carga el driver MySQL explícitamente
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Conexión correcta");

        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error de conexión");
            e.printStackTrace();
        }

        return conexion;
    }
}