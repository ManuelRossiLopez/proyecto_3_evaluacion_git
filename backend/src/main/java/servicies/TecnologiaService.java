package com.lovecode.servicies;

import com.lovecode.database.ConexionBD;
import com.lovecode.models.Tecnologia;

import java.sql.*;
import java.util.ArrayList;

public class TecnologiaService {

    public static ArrayList<Tecnologia>
    obtenerTecnologiasUsuario(
            int idUsuario
    ) {

        ArrayList<Tecnologia> tecnologias =
                new ArrayList<>();

        try {

            Connection conexion =
                    ConexionBD.conectar();

            String sql =

                    "SELECT t.* " +

                    "FROM tecnologias t " +

                    "INNER JOIN usuarios_tecnologias ut " +

                    "ON t.id = ut.id_tecnologias " +

                    "WHERE ut.id_usuarios = ?";

            PreparedStatement statement =
                    conexion.prepareStatement(sql);

            statement.setInt(1, idUsuario);

            ResultSet resultado =
                    statement.executeQuery();

            while (resultado.next()) {

                tecnologias.add(

                        new Tecnologia(

                                resultado.getInt("id"),

                                resultado.getString(
                                        "nombre_tecnoligia"
                                ),

                                resultado.getString("tipo")
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return tecnologias;
    }
}
