package org.example;

import java.util.HashSet;
import java.util.Set;

// Clase Libro
public class Libro {
    private String titulo;
    private String isbn;
    private Set<String> palabrasClave;
    private boolean prestado;

    public Libro(String titulo, String isbn) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.palabrasClave = new HashSet<>();
        this.prestado = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void agregarPalabraClave(String palabra) {
        palabrasClave.add(palabra);
    }

    public Set<String> getPalabrasClave() {
        return new HashSet<>(palabrasClave);
    }

    public boolean estaPrestado() {
        return prestado;
    }

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }
}