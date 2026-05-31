package com.lovecode;

import com.lovecode.models.Usuario;
import com.lovecode.servicies.LikeService;
import com.lovecode.servicies.MatchService;
import com.lovecode.servicies.UsuarioService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Servidor HTTP LoveCode.
 * Sirve el frontend estático Y la API en el mismo puerto 8080.
 *
 * Abre en el navegador: http://localhost:8080/login.html
 *
 * RUTAS API  (JSON):
 *   POST /api/login
 *   POST /api/registro
 *   GET  /api/perfiles?id=X
 *   POST /api/like
 *
 * RUTAS ESTÁTICAS (HTML/CSS/JS):
 *   GET  /login.html
 *   GET  /registro.html
 *   GET  /perfiles.html
 *   GET  /css/style.css
 *   GET  /js/App.js
 *
 * Separamos API en /api/* para que nunca choque con los archivos.
 */
public class Servidor {

    private static final int    PUERTO       = 8080;
    private static final String FRONTEND_DIR = "../frontend";

    public static void iniciar() throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(PUERTO), 0
        );

        // ── API bajo /api/ ─────────────────────────────────
        server.createContext("/api/login",    Servidor::manejarLogin);
        server.createContext("/api/registro", Servidor::manejarRegistro);
        server.createContext("/api/perfiles", Servidor::manejarPerfiles);
        server.createContext("/api/like",     Servidor::manejarLike);

        // ── Archivos estáticos: todo lo demás ──────────────
        server.createContext("/", Servidor::manejarEstaticos);

        server.setExecutor(null);
        server.start();

        System.out.println("════════════════════════════════════════");
        System.out.println("  Servidor LoveCode arrancado");
        System.out.println("  http://localhost:" + PUERTO + "/login.html");
        System.out.println("════════════════════════════════════════");
    }


    /* ══════════════════════════════════════════════════════
       ARCHIVOS ESTÁTICOS
    ══════════════════════════════════════════════════════ */
    private static void manejarEstaticos(HttpExchange exchange)
            throws IOException {

        String ruta = exchange.getRequestURI().getPath();

        // Raíz → redirigir a login.html
        if (ruta.equals("/")) {
            ruta = "/login.html";
        }

        File archivo = new File(FRONTEND_DIR + ruta);

        System.out.println("[static] " + archivo.getAbsolutePath());

        if (!archivo.exists() || archivo.isDirectory()) {
            byte[] bytes = ("404 - No encontrado: " + ruta)
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(404, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
            return;
        }

        byte[] bytes = Files.readAllBytes(archivo.toPath());
        exchange.getResponseHeaders().add("Content-Type", contentType(ruta));
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }

    private static String contentType(String ruta) {
        if (ruta.endsWith(".html")) return "text/html; charset=UTF-8";
        if (ruta.endsWith(".css"))  return "text/css; charset=UTF-8";
        if (ruta.endsWith(".js"))   return "application/javascript; charset=UTF-8";
        if (ruta.endsWith(".png"))  return "image/png";
        if (ruta.endsWith(".ico"))  return "image/x-icon";
        return "text/plain";
    }


    /* ══════════════════════════════════════════════════════
       POST /api/login
    ══════════════════════════════════════════════════════ */
    private static void manejarLogin(HttpExchange exchange)
            throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            responderCors(exchange); return;
        }
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            responder(exchange, 405, "{\"error\":\"Método no permitido\"}"); return;
        }

        String body       = leerBody(exchange);
        String email      = extraerCampo(body, "email");
        String contrasena = extraerCampo(body, "contrasena");

        if (email == null || contrasena == null) {
            responder(exchange, 400, "{\"ok\":false,\"mensaje\":\"Faltan campos\"}");
            return;
        }

        Usuario u = UsuarioService.login(email, contrasena);

        if (u != null) {
            responder(exchange, 200, "{"
                    + "\"ok\":true,"
                    + "\"id\":"          + u.getId()              + ","
                    + "\"nombre\":\""    + escapar(u.getNombre()) + "\","
                    + "\"apellido1\":\"" + escapar(u.getApellido1()) + "\""
                    + "}");
        } else {
            responder(exchange, 401,
                    "{\"ok\":false,\"mensaje\":\"Email o contraseña incorrectos\"}");
        }
    }


    /* ══════════════════════════════════════════════════════
       POST /api/registro
    ══════════════════════════════════════════════════════ */
    private static void manejarRegistro(HttpExchange exchange)
            throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            responderCors(exchange); return;
        }
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            responder(exchange, 405, "{\"error\":\"Método no permitido\"}"); return;
        }

        String body       = leerBody(exchange);
        String nombre     = extraerCampo(body, "nombre");
        String apellido1  = extraerCampo(body, "apellido1");
        String apellido   = extraerCampo(body, "apellido");
        String email      = extraerCampo(body, "email");
        String contrasena = extraerCampo(body, "contrasena");
        String fechaStr   = extraerCampo(body, "fecha_nacimiento");

        if (nombre == null || apellido1 == null || email == null
                || contrasena == null || fechaStr == null) {
            responder(exchange, 400,
                    "{\"ok\":false,\"mensaje\":\"Faltan campos obligatorios\"}");
            return;
        }

        Date fecha;
        try {
            fecha = Date.valueOf(fechaStr);
        } catch (Exception e) {
            responder(exchange, 400,
                    "{\"ok\":false,\"mensaje\":\"Formato de fecha incorrecto\"}");
            return;
        }

        boolean ok = UsuarioService.registrarUsuario(
                nombre, apellido1,
                apellido != null ? apellido : "",
                email, contrasena, fecha);

        responder(exchange, ok ? 201 : 500,
                ok ? "{\"ok\":true}"
                   : "{\"ok\":false,\"mensaje\":\"Error al registrar\"}");
    }


    /* ══════════════════════════════════════════════════════
       GET /api/perfiles?id=X
    ══════════════════════════════════════════════════════ */
    private static void manejarPerfiles(HttpExchange exchange)
            throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            responderCors(exchange); return;
        }
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            responder(exchange, 405, "{\"error\":\"Método no permitido\"}"); return;
        }

        int idActual = 0;
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.contains("id=")) {
            try {
                idActual = Integer.parseInt(
                        query.split("id=")[1].split("&")[0]);
            } catch (NumberFormatException ignored) {}
        }

        ArrayList<Usuario> perfiles = UsuarioService.obtenerPerfiles(idActual);

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < perfiles.size(); i++) {
            Usuario u = perfiles.get(i);
            sb.append("{")
              .append("\"id\":").append(u.getId()).append(",")
              .append("\"nombre\":\"").append(escapar(u.getNombre())).append("\",")
              .append("\"apellido1\":\"").append(escapar(u.getApellido1())).append("\",")
              .append("\"apellido\":\"").append(escapar(u.getApellido())).append("\",")
              .append("\"fecha_nacimiento\":\"").append(u.getFechaNacimiento()).append("\"")
              .append("}");
            if (i < perfiles.size() - 1) sb.append(",");
        }
        sb.append("]");

        responder(exchange, 200, sb.toString());
    }


    /* ══════════════════════════════════════════════════════
       POST /api/like
    ══════════════════════════════════════════════════════ */
    private static void manejarLike(HttpExchange exchange)
            throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            responderCors(exchange); return;
        }
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            responder(exchange, 405, "{\"error\":\"Método no permitido\"}"); return;
        }

        String body        = leerBody(exchange);
        String idDaStr     = extraerCampoNumerico(body, "idQuienDaLike");
        String idRecibeStr = extraerCampoNumerico(body, "idQuienRecibeEl");

        if (idDaStr == null || idRecibeStr == null) {
            responder(exchange, 400, "{\"ok\":false,\"mensaje\":\"Faltan ids\"}");
            return;
        }

        int idDa     = Integer.parseInt(idDaStr);
        int idRecibe = Integer.parseInt(idRecibeStr);

        boolean ok       = LikeService.darLike(idDa, idRecibe);
        boolean hayMatch = ok && MatchService.existeMatch(idDa, idRecibe);

        responder(exchange, ok ? 200 : 500,
                ok ? "{\"ok\":true,\"match\":" + hayMatch + "}"
                   : "{\"ok\":false,\"mensaje\":\"Error al registrar el like\"}");
    }


    /* ══════════════════════════════════════════════════════
       UTILIDADES
    ══════════════════════════════════════════════════════ */
    private static String leerBody(HttpExchange e) throws IOException {
        return new String(e.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void responder(HttpExchange exchange, int codigo, String json)
            throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin",  "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(codigo, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static void responderCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin",  "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }

    private static String extraerCampo(String json, String campo) {
        String clave = "\"" + campo + "\":\"";
        int inicio = json.indexOf(clave);
        if (inicio == -1) return null;
        inicio += clave.length();
        int fin = json.indexOf("\"", inicio);
        return fin == -1 ? null : json.substring(inicio, fin);
    }

    private static String extraerCampoNumerico(String json, String campo) {
        String clave = "\"" + campo + "\":";
        int inicio = json.indexOf(clave);
        if (inicio == -1) return null;
        inicio += clave.length();
        StringBuilder sb = new StringBuilder();
        for (int i = inicio; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == ',' || c == '}' || c == ' ' || c == '\n') break;
            sb.append(c);
        }
        String r = sb.toString().trim();
        return r.isEmpty() ? null : r;
    }

    private static String escapar(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\").replace("\"", "\\\"")
                    .replace("\n", "\\n").replace("\r", "\\r");
    }
}