package com.lovecode.servicies;

import java.io.FileWriter;

public class XmlService {

    public static void generarXML(
            int usuario1,
            int usuario2
    ) {

        try {

            String nombreArchivo =
                    "matches_xml/match_"
                    + usuario1
                    + "_"
                    + usuario2
                    + ".xml";

            new java.io.File("matches_xml").mkdirs();

            FileWriter writer =
                    new FileWriter(nombreArchivo);

            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            );

            writer.write("<match>\n");

            writer.write(
                    "   <usuario1>"
                    + usuario1
                    + "</usuario1>\n"
            );

            writer.write(
                    "   <usuario2>"
                    + usuario2
                    + "</usuario2>\n"
            );

            writer.write("</match>");

            writer.close();

            System.out.println(
                    "XML generado correctamente"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}