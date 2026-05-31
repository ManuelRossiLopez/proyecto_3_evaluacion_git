/**
 * App.js — LoveCode
 * Frontend conectado al servidor Java en http://localhost:8080
 *
 * La API está bajo /api/ para no chocar con los archivos HTML.
 */

const API_URL = 'http://localhost:8080/api';

/* ── Utilidades ─────────────────────────────────────────── */

function mostrarMensaje(id, texto, tipo) {
  var div = document.getElementById(id);
  if (!div) return;
  div.textContent = texto;
  div.className = 'mensaje ' + tipo;
}

function ocultarMensaje(id) {
  var div = document.getElementById(id);
  if (div) div.className = 'mensaje';
}

function calcularEdad(fechaNacimiento) {
  var hoy  = new Date();
  var nac  = new Date(fechaNacimiento);
  var edad = hoy.getFullYear() - nac.getFullYear();
  var mes  = hoy.getMonth() - nac.getMonth();
  if (mes < 0 || (mes === 0 && hoy.getDate() < nac.getDate())) edad--;
  return edad;
}

function obtenerIniciales(nombre, apellido1) {
  return (nombre    ? nombre.charAt(0).toUpperCase()    : '')
       + (apellido1 ? apellido1.charAt(0).toUpperCase() : '');
}

function mostrarMatch(nombreMatch) {
  var toast   = document.getElementById('match-toast');
  var parrafo = document.getElementById('match-nombre');
  if (!toast) return;
  if (parrafo)
    parrafo.textContent = '¡Tú y ' + nombreMatch + ' os habéis dado like mutuamente!';
  toast.classList.add('visible');
  setTimeout(function () { toast.classList.remove('visible'); }, 4000);
}

/* ── Login ──────────────────────────────────────────────── */

function login() {
  var email      = document.getElementById('email').value.trim();
  var contrasena = document.getElementById('contrasena').value;

  if (!email || !contrasena) {
    mostrarMensaje('mensaje-login', 'Rellena todos los campos.', 'error');
    return;
  }

  ocultarMensaje('mensaje-login');
  mostrarMensaje('mensaje-login', 'Conectando...', 'exito');

  fetch(API_URL + '/login', {
    method:  'POST',
    headers: { 'Content-Type': 'application/json' },
    body:    JSON.stringify({ email: email, contrasena: contrasena })
  })
  .then(function (r) { return r.json(); })
  .then(function (datos) {
    if (datos.ok) {
      sessionStorage.setItem('usuarioId',     datos.id);
      sessionStorage.setItem('usuarioNombre', datos.nombre);
      window.location.href = 'perfiles.html';
    } else {
      mostrarMensaje('mensaje-login',
        datos.mensaje || 'Email o contraseña incorrectos.', 'error');
    }
  })
  .catch(function () {
    mostrarMensaje('mensaje-login',
      'No se puede conectar con el servidor.', 'error');
  });
}

/* ── Registro ───────────────────────────────────────────── */

function registrarUsuario() {
  var nombre           = document.getElementById('nombre').value.trim();
  var apellido1        = document.getElementById('apellido1').value.trim();
  var apellido         = document.getElementById('apellido').value.trim();
  var email            = document.getElementById('email').value.trim();
  var contrasena       = document.getElementById('contrasena').value;
  var fecha_nacimiento = document.getElementById('fecha_nacimiento').value;

  if (!nombre || !apellido1 || !email || !contrasena || !fecha_nacimiento) {
    mostrarMensaje('mensaje-registro', 'Campos obligatorios vacíos.', 'error');
    return;
  }
  if (contrasena.length < 6) {
    mostrarMensaje('mensaje-registro',
      'La contraseña debe tener al menos 6 caracteres.', 'error');
    return;
  }

  ocultarMensaje('mensaje-registro');
  mostrarMensaje('mensaje-registro', 'Registrando...', 'exito');

  fetch(API_URL + '/registro', {
    method:  'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      nombre, apellido1, apellido, email, contrasena, fecha_nacimiento
    })
  })
  .then(function (r) { return r.json(); })
  .then(function (datos) {
    if (datos.ok) {
      mostrarMensaje('mensaje-registro',
        '¡Cuenta creada! Redirigiendo...', 'exito');
      setTimeout(function () {
        window.location.href = 'login.html';
      }, 1500);
    } else {
      mostrarMensaje('mensaje-registro',
        datos.mensaje || 'Error al crear la cuenta.', 'error');
    }
  })
  .catch(function () {
    mostrarMensaje('mensaje-registro',
      'No se puede conectar con el servidor.', 'error');
  });
}

/* ── Cargar perfiles ────────────────────────────────────── */

function cargarPerfiles() {
  var spinner     = document.getElementById('spinner');
  var grid        = document.getElementById('grid-perfiles');
  var sinPerfiles = document.getElementById('sin-perfiles');
  var idActual    = sessionStorage.getItem('usuarioId') || 0;

  fetch(API_URL + '/perfiles?id=' + idActual)
  .then(function (r) { return r.json(); })
  .then(function (perfiles) {
    if (spinner) spinner.style.display = 'none';
    if (!perfiles || perfiles.length === 0) {
      if (sinPerfiles) sinPerfiles.style.display = 'block';
      return;
    }
    perfiles.forEach(function (p) {
      grid.appendChild(crearTarjetaPerfil(p));
    });
  })
  .catch(function () {
    if (spinner) spinner.style.display = 'none';
    if (grid) grid.innerHTML =
      '<p style="color:var(--texto-suave);text-align:center">' +
      'Error al cargar perfiles. ¿Está arrancado Java?</p>';
  });
}

function crearTarjetaPerfil(perfil) {
  var edad           = calcularEdad(perfil.fecha_nacimiento);
  var iniciales      = obtenerIniciales(perfil.nombre, perfil.apellido1);
  var nombreCompleto = perfil.nombre + ' ' + perfil.apellido1 + ' ' + perfil.apellido;

  var div = document.createElement('div');
  div.className = 'perfil-card';
  div.innerHTML =
    '<div style="display:flex;align-items:center;gap:1rem">' +
      '<div class="perfil-avatar">' + iniciales + '</div>' +
      '<div class="perfil-info">' +
        '<h3>' + nombreCompleto + '</h3>' +
        '<span class="edad">' + edad + ' años</span>' +
      '</div>' +
    '</div>' +
    '<div class="perfil-acciones">' +
      '<button class="btn btn-like"  onclick="darLike('  + perfil.id + ',\'' + nombreCompleto + '\',this)">👍 Like</button>' +
      '<button class="btn btn-pasar" onclick="darPasar(' + perfil.id + ',this)">✗ Pasar</button>' +
    '</div>';
  return div;
}

/* ── Like ───────────────────────────────────────────────── */

function darLike(idObjetivo, nombreObjetivo, boton) {
  var idActual = parseInt(sessionStorage.getItem('usuarioId') || '0');
  if (boton) boton.disabled = true;

  fetch(API_URL + '/like', {
    method:  'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      idQuienDaLike:   idActual,
      idQuienRecibeEl: idObjetivo
    })
  })
  .then(function (r) { return r.json(); })
  .then(function (datos) {
    if (datos.ok && datos.match) mostrarMatch(nombreObjetivo);
    if (boton) ocultarTarjeta(boton.closest('.perfil-card'));
  })
  .catch(function () {
    if (boton) boton.disabled = false;
  });
}

/* ── Pasar ──────────────────────────────────────────────── */

function darPasar(idObjetivo, boton) {
  if (boton) ocultarTarjeta(boton.closest('.perfil-card'));
}

function ocultarTarjeta(tarjeta) {
  if (!tarjeta) return;
  tarjeta.style.transition = 'opacity 0.25s, transform 0.25s';
  tarjeta.style.opacity    = '0';
  tarjeta.style.transform  = 'scale(0.95)';
  setTimeout(function () { tarjeta.style.display = 'none'; }, 250);
}