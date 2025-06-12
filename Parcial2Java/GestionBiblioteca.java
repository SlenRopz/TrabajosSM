package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GestionBiblioteca {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        try {
            biblioteca.cargarLibrosDesdeCSV("libros.csv");
        } catch (IOException e) {
            System.out.println("Error al cargar los libros: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Agregar Libro");
            System.out.println("2. Buscar Libro por ISBN");
            System.out.println("3. Buscar Libros por Autor");
            System.out.println("4. Listar Todos los Libros");
            System.out.println("5. Salir y Guardar");
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (opcion) {
                case 1:
                    System.out.print("Titulo: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();
                    System.out.print("Año de Publicación: ");
                    int año = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine();
                    try {
                        biblioteca.agregarLibro(new Libro(titulo, autor, año, isbn));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("ISBN: ");
                    isbn = scanner.nextLine();
                    Libro libro = biblioteca.buscarLibroPorISBN(isbn);
                    if (libro != null) {
                        System.out.println(libro);
                    } else {
                        System.out.println("Libro no encontrado.");
                    }
                    break;
                case 3:
                    System.out.print("Autor: ");
                    autor = scanner.nextLine();
                    List<Libro> libros = biblioteca.buscarLibrosPorAutor(autor);
                    for (Libro l : libros) {
                        System.out.println(l);
                    }
                    break;
                case 4:
                    List<Libro> todosLosLibros = biblioteca.listarTodosLosLibros();
                    for (Libro l : todosLosLibros) {
                        System.out.println(l);
                    }
                    break;
                case 5:
                    try {
                        biblioteca.guardarLibrosEnCSV("libros.csv");
                    } catch (IOException e) {
                        System.out.println("Error al guardar los libros: " + e.getMessage());
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
