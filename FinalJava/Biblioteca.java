package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Biblioteca {
    private ArrayList<Libro> libros;
    private ArrayList<Usuario> usuarios;
    private HashMap<String, Libro> librosPorISBN;
    private TreeMap<LocalDate, ArrayList<Prestamo>> prestamosPorFecha;
    private ArrayList<Prestamo> prestamosActivos;
	private HashMap<String, Libro> coleccionLibros;

    public Biblioteca() {
        libros = new ArrayList<>();
        usuarios = new ArrayList<>();
        librosPorISBN = new HashMap<>();
        prestamosPorFecha = new TreeMap<>();
        prestamosActivos = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
        librosPorISBN.put(libro.getIsbn(), libro);
    }

    public List<Libro> obtenerLibros(){
        return libros;
    }

    public ArrayList<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Libro buscarLibroPorISBN(String isbn) {
        return librosPorISBN.get(isbn);
    }

    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    public List<Prestamo> obtenerPrestamosPorFecha(LocalDate fecha) {
        return prestamosPorFecha.getOrDefault(fecha, new ArrayList<>());
    }

    public boolean eliminarLibro(String isbn) {
        if (coleccionLibros.containsKey(isbn)) {
            coleccionLibros.remove(isbn);
            return true;
        } else {
            return false;
        }
    }

    public int totalLibros() {
        return coleccionLibros.size();
    }


    public void eliminarUsuario(String id) {
        usuarios.removeIf(usuario -> usuario.getId().equals(id));
    }

    public Prestamo realizarPrestamo(String isbn, String idUsuario, LocalDate fecha) {
        // Buscar el libro por ISBN
        Libro libro = buscarLibroPorISBN(isbn);
        // Buscar el usuario por ID
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        // Verificar que el libro y el usuario existan, y que el libro no esté prestado
        if (libro != null && usuario != null && !libro.estaPrestado()) {
            // Crear un nuevo préstamo
            Prestamo prestamo = new Prestamo(libro, usuario, fecha);
            // Agregar el préstamo a la lista de préstamos activos
            prestamosActivos.add(prestamo);
            // Marcar el libro como prestado
            libro.setPrestado(true);

            // Agregar el préstamo a la lista de préstamos por fecha
            prestamosPorFecha.putIfAbsent(fecha, new ArrayList<>());
            prestamosPorFecha.get(fecha).add(prestamo);

            return prestamo;
        }
        return null;
    }

    public boolean devolverLibro(String isbn, LocalDate fechaDevolucion) {
        for (Prestamo prestamo : prestamosActivos) {
            if (prestamo.getLibro().getIsbn().equals(isbn)) {
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.getLibro().setPrestado(false);
                prestamosActivos.remove(prestamo);
                return true;
            }
        }
        return false;
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        return new ArrayList<>(prestamosActivos);
    }
}


