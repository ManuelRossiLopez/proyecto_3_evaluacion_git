package tests;

import com.lovecode.database.ConexionBD;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ConexionBDTest {

    @Test
    public void testConexion() {

        Connection conexion =
                ConexionBD.conectar();

        assertNotNull(conexion);
    }
}