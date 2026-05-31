package com.lovecode.servicies;

import com.lovecode.database.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;

public class MatchService {

    public static boolean existeMatch(
            int usuario1,
            int usuario2
    ) {

        try {

            Connection conexion =
                    ConexionBD.conectar();

            String sql =
                    "SELECT * FROM matches " +
                    "WHERE " +
                    "(id_usuario1 = ? AND id_usuario2 = ?) " +
                    "OR " +
                    "(id_usuario1 = ? AND id_usuario2 = ?)";

            PreparedStatement statement =
                    conexion.prepareStatement(sql);

            statement.setInt(1, usuario1);
            statement.setInt(2, usuario2);
            statement.setInt(3, usuario2);
            statement.setInt(4, usuario1);

            ResultSet resultado =
                    statement.executeQuery();

            return resultado.next();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public static int contarMatches(
            int idUsuario
    ) {

        try {

            Connection conexion =
                    ConexionBD.conectar();

            CallableStatement statement =
                    conexion.prepareCall(
                            "{CALL conteo_matches(?)}"
                    );

            statement.setInt(1, idUsuario);

            ResultSet resultado =
                    statement.executeQuery();

            if (resultado.next()) {

                return resultado.getInt(
                        "total_matches"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }
}