package tests;

import com.lovecode.models.Usuario;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioToStringTest {

    @Test
    public void testToString() {

        Usuario usuario = new Usuario(
                1,
                "Ana",
                "Garcia",
                "Ruiz",
                "ana@gmail.com",
                "1234",
                Date.valueOf("1999-03-15")
        );

        String texto = usuario.toString();

        assertTrue(texto.contains("Ana"));
        assertTrue(texto.contains("Garcia"));
        assertTrue(texto.contains("ana@gmail.com"));
    }
}