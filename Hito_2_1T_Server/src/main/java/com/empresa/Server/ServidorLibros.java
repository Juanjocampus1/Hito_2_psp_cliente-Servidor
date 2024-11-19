package com.empresa.Server;

import com.empresa.Model.Libro;
import com.empresa.Repository.LibroDAO;

import java.io.*;
import java.net.*;
import java.util.List;

public class ServidorLibros {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        LibroDAO libroDAO = new LibroDAO();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor escuchando en el puerto " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Cliente conectado.");
                    String opcion;
                    while ((opcion = in.readLine()) != null) {
                        switch (opcion) {
                            case "1":  // Buscar libros por palabra clave
                                String palabraClave = in.readLine();
                                List<Libro> libros = libroDAO.buscarLibros(palabraClave);
                                enviarLibros(libros, out);
                                break;
                            case "2":  // Agregar un libro
                                String titulo = in.readLine();
                                String autor = in.readLine();
                                libroDAO.agregarLibro(new Libro(0, titulo, autor));
                                out.println("Libro agregado con éxito.");
                                break;
                            case "3":  // Listar todos los libros
                                libros = libroDAO.listarLibros();
                                enviarLibros(libros, out);
                                break;
                            case "4":  // Eliminar un libro por ID
                                int id = Integer.parseInt(in.readLine());
                                if (libroDAO.eliminarLibro(id)) {
                                    out.println("Libro eliminado con éxito.");
                                } else {
                                    out.println("No se encontró un libro con el ID especificado.");
                                }
                                break;
                            case "5":  // Actualizar libro por ID
                                id = Integer.parseInt(in.readLine());
                                titulo = in.readLine();
                                autor = in.readLine();
                                if (libroDAO.actualizarLibro(id, titulo, autor)) {
                                    out.println("Libro actualizado con éxito.");
                                } else {
                                    out.println("No se encontró un libro con el ID especificado.");
                                }
                                break;
                            case "6":  // Contar libros por autor
                                autor = in.readLine();
                                int cantidad = libroDAO.contarLibrosPorAutor(autor);
                                out.println("El autor " + autor + " ha escrito " + cantidad + " libro(s).");
                                break;
                            case "7":  // Buscar libros por rango de ID
                                int idInicio = Integer.parseInt(in.readLine());
                                int idFin = Integer.parseInt(in.readLine());
                                libros = libroDAO.buscarLibrosPorRangoID(idInicio, idFin);
                                enviarLibros(libros, out);
                                break;
                            case "8":  // Mostrar estadísticas
                                int totalLibros = libroDAO.contarTotalLibros();
                                int autoresUnicos = libroDAO.contarAutoresUnicos();
                                out.println("Total de libros: " + totalLibros);
                                out.println("Autores únicos: " + autoresUnicos);
                                break;
                            default:
                                out.println("Opción no válida.");
                        }
                        out.println("END");
                    }
                } catch (IOException e) {
                    System.err.println("Error con el cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void enviarLibros(List<Libro> libros, PrintWriter out) {
        if (libros.isEmpty()) {
            out.println("No se encontraron libros.");
        } else {
            for (Libro libro : libros) {
                out.println("ID: " + libro.getId() + ", Título: " + libro.getTitulo() + ", Autor: " + libro.getAutor());
            }
        }
    }
}
