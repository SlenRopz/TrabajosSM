package org.example;
import java.time.LocalDate;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SistemaBiblioteca {
    private final Biblioteca biblioteca;
    private final Scanner scanner;
    private static final String ARCHIVO_LIBROS = "libros.txt";
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_PRESTAMOS = "prestamos.txt";
    private static final String ARCHIVO_LOG = "log.txt";

    public SistemaBiblioteca() {
        biblioteca = new Biblioteca();
        scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        cargarDatos();
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = obtenerOpcion();
            salir = procesarOpcion(opcion);
        }
        guardarDatos();
    }

    private void mostrarMenu() {
        System.out.println("\n--- Sistema de Biblioteca ---");
        System.out.println("1. Agregar libro");
        System.out.println("2. Eliminar libro");
        System.out.println("3. Buscar libro");
        System.out.println("4. Agregar usuario");
        System.out.println("5. Eliminar usuario");
        System.out.println("6. Realizar préstamo");
        System.out.println("7. Devolver libro");
        System.out.println("8. Ver préstamos activos");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarLibro();
                break;
            case 2:
                eliminarLibro();
                break;
            case 3:
                buscarLibro();
                break;
            case 4:
                agregarUsuario();
                break;
            case 5:
                eliminarUsuario();
                break;
            case 6:
                realizarPrestamo();
                break;
            case 7:
                devolverLibro();
                break;
            case 8:
                verPrestamosActivos();
                break;
            case 9:
                System.out.println("Gracias por usar el Sistema de Biblioteca.");
                return true;
            default:
                System.out.println("Opción no válida. Por favor, intente de nuevo.");
        }
        return false;
    }

    private void agregarLibro() {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese el ISBN del libro: ");
        String isbn = scanner.nextLine();
        Libro libro = new Libro(titulo, isbn);
        System.out.print("Ingrese palabras clave (separadas por comas): ");
        String[] palabrasClave = scanner.nextLine().split(",");
        for (String palabra : palabrasClave) {
            libro.agregarPalabraClave(palabra.trim());
        }
        biblioteca.agregarLibro(libro);
        System.out.println("Libro agregado con éxito.");
        registrarLog("Libro agregado: " + isbn);
    }

    private void eliminarLibro() {
        System.out.print("Ingrese el ISBN del libro a eliminar: ");
        String isbn = scanner.nextLine();
        biblioteca.eliminarLibro(isbn);
        System.out.println("Libro eliminado con éxito.");
        registrarLog("Libro eliminado: " + isbn);
    }

    private void buscarLibro() {
        System.out.print("Ingrese el ISBN del libro a buscar: ");
        String isbn = scanner.nextLine();
        Libro libro = biblioteca.buscarLibroPorISBN(isbn);
        if (libro != null) {
            System.out.println("Libro encontrado: " + libro.getTitulo());
            System.out.println("ISBN: " + libro.getIsbn());
            System.out.println("Palabras clave: " + String.join(", ", libro.getPalabrasClave()));
            System.out.println("Estado: " + (libro.estaPrestado() ? "Prestado" : "Disponible"));
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void agregarUsuario() {
        System.out.print("Ingrese el nombre del usuario: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el ID del usuario: ");
        String id = scanner.nextLine();
        Usuario usuario = new Usuario(nombre, id);
        biblioteca.agregarUsuario(usuario);
        System.out.println("Usuario agregado con éxito.");
        registrarLog("Usuario agregado: " + id);
    }

    private void eliminarUsuario() {
        System.out.print("Ingrese el ID del usuario a eliminar: ");
        String id = scanner.nextLine();
        biblioteca.eliminarUsuario(id);
        System.out.println("Usuario eliminado con éxito.");
        registrarLog("Usuario eliminado: " + id);
    }

    private void realizarPrestamo() {
        System.out.print("Ingrese el ISBN del libro: ");
        String isbn = scanner.nextLine();
        System.out.print("Ingrese el ID del usuario: ");
        String idUsuario = scanner.nextLine();
        LocalDate fechaPrestamo = LocalDate.now();
        Prestamo prestamo = biblioteca.realizarPrestamo(isbn, idUsuario, fechaPrestamo);
        if (prestamo != null) {
            System.out.println("Préstamo realizado con éxito.");
            registrarLog("Préstamo realizado: " + isbn + " a " + idUsuario);
        } else {
            System.out.println("No se pudo realizar el préstamo. Verifique los datos e intente nuevamente.");
        }
    }

    private void devolverLibro() {
        System.out.print("Ingrese el ISBN del libro a devolver: ");
        String isbn = scanner.nextLine();
        LocalDate fechaDevolucion = LocalDate.now();
        if (biblioteca.devolverLibro(isbn, fechaDevolucion)) {
            System.out.println("Libro devuelto con éxito.");
            registrarLog("Libro devuelto: " + isbn);
        } else {
            System.out.println("No se pudo realizar la devolución. Verifique el ISBN e intente nuevamente.");
        }
    }

    private void verPrestamosActivos() {
        List<Prestamo> prestamosActivos = biblioteca.obtenerPrestamosActivos();
        if (prestamosActivos.isEmpty()) {
            System.out.println("No hay préstamos activos en este momento.");
        } else {
            System.out.println("Préstamos activos:");
            for (Prestamo prestamo : prestamosActivos) {
                System.out.println("Libro: " + prestamo.getLibro().getTitulo() +
                        " (ISBN: " + prestamo.getLibro().getIsbn() + ")");
                System.out.println("Usuario: " + prestamo.getUsuario().getNombre() +
                        " (ID: " + prestamo.getUsuario().getId() + ")");
                System.out.println("Fecha de préstamo: " + prestamo.getFechaPrestamo());
                System.out.println("--------------------");
            }
        }
    }

    private void cargarDatos() {
        cargarLibros();
        cargarUsuarios();
        cargarPrestamos();
    }

    private void cargarLibros() {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_LIBROS));
            for (String linea : lineas) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    Libro libro = new Libro(partes[0], partes[1]);
                    biblioteca.agregarLibro(libro);
                }
            }
        } catch (IOException e) {
            registrarLog("Error al cargar libros: " + e.getMessage());
        }
    }

    private void cargarUsuarios() {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_USUARIOS));
            for (String linea : lineas) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    Usuario usuario = new Usuario(partes[0], partes[1]);
                    biblioteca.agregarUsuario(usuario);
                }
            }
        } catch (IOException e) {
            registrarLog("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void cargarPrestamos() {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_PRESTAMOS));
            for (String linea : lineas) {
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    String isbn = partes[0];
                    String idUsuario = partes[1];
                    LocalDate fechaPrestamo = LocalDate.parse(partes[2]);
                    LocalDate fechaDevolucion = partes[3].equals("null") ? null : LocalDate.parse(partes[3]);
                    Prestamo prestamo = biblioteca.realizarPrestamo(isbn, idUsuario, fechaPrestamo);
                    if (prestamo != null && fechaDevolucion != null) {
                        biblioteca.devolverLibro(isbn, fechaDevolucion);
                    }
                }
            }
        } catch (IOException e) {
            registrarLog("Error al cargar préstamos: " + e.getMessage());
        }
    }

    private void guardarDatos() {
        guardarLibros();
        guardarUsuarios();
        guardarPrestamos();
    }

    private void guardarLibros() {
        try {
            List<String> lineas = new ArrayList<>();
            for (Libro libro : biblioteca.obtenerLibros()) {
                lineas.add(libro.getTitulo() + "," + libro.getIsbn() + "," + String.join(";", libro.getPalabrasClave()));
            }
            Files.write(Paths.get(ARCHIVO_LIBROS), lineas);
        } catch (IOException e) {
            registrarLog("Error al guardar libros: " + e.getMessage());
        }
    }

    private void guardarUsuarios() {
        try {
            List<String> lineas = new ArrayList<>();
            for (Usuario usuario : biblioteca.obtenerUsuarios()) {
                lineas.add(usuario.getNombre() + "," + usuario.getId());
            }
            Files.write(Paths.get(ARCHIVO_USUARIOS), lineas);
        } catch (IOException e) {
            registrarLog("Error al guardar usuarios: " + e.getMessage());
        }
    }

    private void guardarPrestamos() {
        try {
            List<String> lineas = new ArrayList<>();
            for (Prestamo prestamo : biblioteca.obtenerPrestamosActivos()) {
                lineas.add(prestamo.getLibro().getIsbn() + "," +
                        prestamo.getUsuario().getId() + "," +
                        prestamo.getFechaPrestamo() + "," +
                        (prestamo.getFechaDevolucion() == null ? "null" : prestamo.getFechaDevolucion()));
            }
            Files.write(Paths.get(ARCHIVO_PRESTAMOS), lineas);
        } catch (IOException e) {
            registrarLog("Error al guardar préstamos: " + e.getMessage());} }
            	
            	public void registrarLog(String mensaje) {
            	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            	    String fechaHoraActual = LocalDateTime.now().format(formatter);

            	    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARCHIVO_LOG), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            	        writer.write(fechaHoraActual + ": " + mensaje);
            	        writer.newLine();
            	    } catch (IOException e) {
            	        System.err.println("Error al registrar log: " + e.getMessage());
            	    }
            	
        
    

            	}
            }
        
