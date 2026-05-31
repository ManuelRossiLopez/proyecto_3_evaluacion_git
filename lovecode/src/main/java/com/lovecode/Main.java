import database.ConexionBD;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        Connection conexion = ConexionBD.conectar();

        if (conexion != null) {
            System.out.println("La aplicación funciona correctamente");
        }
    }
}