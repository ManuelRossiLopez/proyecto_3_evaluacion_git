package com.lovecode.servicies;

import com.lovecode.database.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LikeService {

    public static boolean darLike(
            int usuarioDa,
            int usuarioRecibe
    ) {

        try {

            Connection conexion =
                    ConexionBD.conectar();

            String sql =
                    "INSERT INTO likes " +
                    "(id_usuario_da, id_usuario_recibe) " +
                    "VALUES (?, ?)";

            PreparedStatement statement =
                    conexion.prepareStatement(sql);

            statement.setInt(1, usuarioDa);
            statement.setInt(2, usuarioRecibe);

            statement.executeUpdate();

            System.out.println(
                    "Like registrado correctamente"
            );

            
            if (
                MatchService.existeMatch(
                        usuarioDa,
                        usuarioRecibe
                )
            ) {

                System.out.println(
                        "¡MATCH ENCONTRADO!"
                );

                XmlService.generarXML(
                        usuarioDa,
                        usuarioRecibe
                );
            }

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}