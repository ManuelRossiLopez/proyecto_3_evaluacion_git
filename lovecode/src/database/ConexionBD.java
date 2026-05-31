package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://IP_MAQUINA_VIRTUAL:3306/NOMBRE_BD";
    private static final String USER = "desarrollador";
    private static final String PASSWORD = "TU_PASSWORD";

    public static Connection conectar() {

        Connection conexion = null;

        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión correcta a la base de datos");

        } catch (SQLException e) {
            System.out.println("Error de conexión");
            e.printStackTrace();
        }

        return conexion;
    }
}