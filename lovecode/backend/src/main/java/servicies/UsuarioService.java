package com.lovecode.servicies;

import com.lovecode.database.ConexionBD;
import com.lovecode.models.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class UsuarioService {

    public static ArrayList<Usuario> obtenerUsuarios() {
        

        ArrayList<Usuario> usuarios =
                new ArrayList<>();

        try {

            Connection conexion =
                    ConexionBD.conectar();

            String sql =
                    "SELECT * FROM usuarios";

            Statement statement =
                    conexion.createStatement();

            ResultSet resultado =
                    statement.executeQuery(sql);

            while (resultado.next()) {

                Usuario usuario = new Usuario(

                        resultado.getInt("id"),

                        resultado.getString("nombre"),

                        resultado.getString("apellido1"),

                        resultado.getString("apellido"),

                        resultado.getString("email"),

                        resultado.getString("contrasena"),

                        resultado.getDate("fecha_nacimiento")
                );

                usuarios.add(usuario);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return usuarios;
    }
    public static Usuario login(
        String email,
        String contrasena
) {

    Usuario usuario = null;

    try {

        Connection conexion =
                ConexionBD.conectar();

        String sql =
                "SELECT * FROM usuarios " +
                "WHERE email = ? " +
                "AND contrasena = ?";

        PreparedStatement statement =
                conexion.prepareStatement(sql);

        statement.setString(1, email);
        statement.setString(2, contrasena);

        ResultSet resultado =
                statement.executeQuery();

        if (resultado.next()) {

            usuario = new Usuario(

                    resultado.getInt("id"),

                    resultado.getString("nombre"),

                    resultado.getString("apellido1"),

                    resultado.getString("apellido"),

                    resultado.getString("email"),

                    resultado.getString("contrasena"),

                    resultado.getDate("fecha_nacimiento")
            );
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return usuario;
}

public static boolean registrarUsuario(
        String nombre,
        String apellido1,
        String apellido,
        String email,
        String contrasena,
        java.sql.Date fechaNacimiento
) {

    try {

        Connection conexion =
                ConexionBD.conectar();

        String sql =
                "INSERT INTO usuarios " +
                "(nombre, apellido1, apellido, email, contrasena, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement =
                conexion.prepareStatement(sql);

        statement.setString(1, nombre);
        statement.setString(2, apellido1);
        statement.setString(3, apellido);
        statement.setString(4, email);
        statement.setString(5, contrasena);
        statement.setDate(6, fechaNacimiento);

        int filas = statement.executeUpdate();

        return filas > 0;

    } catch (Exception e) {

        e.printStackTrace();
        return false;
    }
}
public static ArrayList<Usuario> obtenerPerfiles(
        int usuarioActual
) {

    ArrayList<Usuario> usuarios =
            new ArrayList<>();

    try {

        Connection conexion =
                ConexionBD.conectar();

        String sql =
                "SELECT * FROM usuarios " +
                "WHERE id <> ?";

        PreparedStatement statement =
                conexion.prepareStatement(sql);

        statement.setInt(1, usuarioActual);

        ResultSet resultado =
                statement.executeQuery();

        while (resultado.next()) {

            Usuario usuario =
                    new Usuario(

                            resultado.getInt("id"),

                            resultado.getString("nombre"),

                            resultado.getString("apellido1"),

                            resultado.getString("apellido"),

                            resultado.getString("email"),

                            resultado.getString("contrasena"),

                            resultado.getDate("fecha_nacimiento")
                    );

            usuarios.add(usuario);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return usuarios;
}
public static void cargarUsuario(
        int idUsuario
) {

    try {

        Connection conexion =
                ConexionBD.conectar();

        CallableStatement statement =
                conexion.prepareCall(
                        "{CALL cargar_usuario(?)}"
                );

        statement.setInt(1, idUsuario);

        ResultSet resultado =
                statement.executeQuery();

        while (resultado.next()) {

            System.out.println(
                    "Nombre: "
                    + resultado.getString("nombre")
            );

            System.out.println(
                    "Email: "
                    + resultado.getString("email")
            );

            System.out.println(
                    "Tecnología: "
                    + resultado.getString("nombre_tecnoligia")
            );

            System.out.println(
                    "Tipo: "
                    + resultado.getString("tipo")
            );

            System.out.println("----------------");
        }

    } catch (Exception e) {

        e.printStackTrace();
    }
}

public static boolean borrarUsuario(
        int idUsuario
) {

    try {

        Connection conexion =
                ConexionBD.conectar();

        CallableStatement statement =
                conexion.prepareCall(
                        "{CALL borrar_usuario(?)}"
                );

        statement.setInt(1, idUsuario);

        statement.execute();

        return true;

    } catch (Exception e) {

        e.printStackTrace();
    }

    return false;
}

}