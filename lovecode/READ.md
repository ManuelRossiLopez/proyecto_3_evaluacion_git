# LoveCode ❤️💻

## Descripción

LoveCode es una aplicación web desarrollada como proyecto académico que combina el concepto de una aplicación de citas con el mundo de la programación y la tecnología. La plataforma permite que los usuarios se registren, inicien sesión, visualicen perfiles y establezcan conexiones mediante un sistema de likes.

Cuando dos usuarios se dan like mutuamente, se genera automáticamente un match que se almacena en la base de datos y además se crea un archivo XML con la información de la coincidencia.

---

## Objetivos del proyecto

El objetivo principal de LoveCode es aplicar de forma práctica los conocimientos adquiridos durante el ciclo formativo en distintas áreas del desarrollo de software.

Entre los objetivos destacan:

- Desarrollo de aplicaciones Java.
- Diseño y gestión de bases de datos relacionales.
- Creación de interfaces web.
- Uso de SQL avanzado.
- Implementación de triggers y procedimientos almacenados.
- Generación automática de archivos XML.
- Uso de Git y GitHub para el control de versiones.
- Automatización mediante scripts.

---

## Tecnologías utilizadas

### Backend

- Java 17
- Maven
- JDBC

### Base de datos

- MariaDB
- SQL
- Triggers
- Procedimientos almacenados

### Frontend

- HTML5
- CSS3
- JavaScript

### Otros

- XML
- Git
- GitHub

---

## Arquitectura del sistema

El proyecto sigue una arquitectura cliente-servidor compuesta por tres capas principales:

### Frontend

Se encarga de la interacción con el usuario mediante páginas web desarrolladas con HTML, CSS y JavaScript.

### Backend

Implementado en Java, contiene toda la lógica de negocio relacionada con usuarios, likes, matches y generación de archivos XML.

### Base de datos

MariaDB almacena toda la información del sistema y gestiona automáticamente la creación de matches mediante triggers.

```text
Frontend (HTML, CSS, JS)
           │
           ▼
Backend Java (JDBC)
           │
           ▼
Base de Datos MariaDB
           │
           ▼
Generación XML
```

---

## Estructura de la base de datos

La base de datos `love_code` está formada por las siguientes tablas:

### usuarios

Almacena la información de los usuarios registrados.

### tecnologias

Contiene las tecnologías disponibles en la plataforma.

### usuario_tecnologia

Relaciona usuarios con tecnologías.

### likes

Registra los likes enviados entre usuarios.

### matches

Almacena los matches generados automáticamente.

---

## Funcionalidades principales

### Registro de usuarios

Los usuarios pueden crear una cuenta proporcionando sus datos personales.

### Inicio de sesión

Permite acceder al sistema mediante email y contraseña.

### Gestión de perfiles

Los usuarios pueden visualizar perfiles registrados en la plataforma.

### Sistema de likes

Un usuario puede mostrar interés por otro usuario mediante un like.

### Generación automática de matches

Cuando dos usuarios se dan like mutuamente se crea automáticamente un match.

### Generación de XML

Cada match generado produce un archivo XML que almacena la información de la coincidencia.

Ejemplo:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<match>
   <usuario1>2</usuario1>
   <usuario2>4</usuario2>
</match>
```

---

## Estructura del proyecto

```text
lovecode/
│
├── backend/
│   ├── src/main/java/
│   │   ├── com/lovecode/database
│   │   ├── com/lovecode/models
│   │   ├── com/lovecode/services
│   │   └── com/lovecode/Main.java
│   │
│   ├── matches_xml/
│   └── pom.xml
│
├── frontend/
│   ├── login.html
│   ├── registro.html
│   ├── perfiles.html
│   ├── css/
│   └── js/
│
└── README.md
```

---

## Instalación y ejecución


### Acceder al backend

```bash
cd lovecode/backend
```

### Compilar proyecto

```bash
mvn compile
```

### Ejecutar proyecto

```bash
mvn exec:java -Dexec.mainClass=com.lovecode.Main
```

---

## Pruebas

El proyecto incluye pruebas unitarias desarrolladas con JUnit para verificar el correcto funcionamiento de diferentes componentes.

Ejecución:

```bash
mvn test
```
