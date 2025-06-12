package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca {
    private Map<String, Libro> libros;

    public Biblioteca() {
        this.libros = new HashMap<>();
    }

    public void agregarLibro(Libro libro) throws IllegalArgumentException {
        if (libros.containsKey(libro.getIsbn())) {
            throw new IllegalArgumentException("El libro con ISBN " + libro.getIsbn() + " ya existe.");
        }
        libros.put(libro.getIsbn(), libro);
    }

    public Libro buscarLibroPorISBN(String isbn) {
        return libros.get(isbn);
    }

    public List<Libro> buscarLibrosPorAutor(String autor) {
        List<Libro> resultado = new ArrayList<>();
       //TODO Agregar lógica
        return resultado;
    }

    public List<Libro> listarTodosLosLibros() {
        return new ArrayList<>(libros.values());
    }

    public void cargarLibrosDesdeCSV(String nombreArchivo) throws IOException {
        // TODO Agregar Lógica
        // usar metodo  agregarLibro()

    }

    public void guardarLibrosEnCSV(String nombreArchivo) throws IOException {
        // TODO Agregar Lógica
        // el archivo debe contener: Titulo, Autor, Año de Publicacion, ISBN

    }
}
