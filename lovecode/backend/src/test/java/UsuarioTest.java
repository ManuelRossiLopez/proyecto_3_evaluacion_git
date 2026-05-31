import com.lovecode.models.Usuario;

import com.lovecode.models.Usuario;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void testGetNombre() {

        Usuario usuario =
                new Usuario(
                        1,
                        "Carlos",
                        "Perez",
                        "Lopez",
                        "carlos@gmail.com",
                        "1234",
                        Date.valueOf("2000-05-10")
                );

        assertEquals(
                "Carlos",
                usuario.getNombre()
        );
    }
}