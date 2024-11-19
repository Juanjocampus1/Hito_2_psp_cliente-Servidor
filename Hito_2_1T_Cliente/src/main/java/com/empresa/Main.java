package com.empresa;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            boolean continuar = true;
            while (continuar) {
                System.out.println("\nMenú de Opciones:");
                System.out.println("1. Buscar libros por palabra clave");
                System.out.println("2. Agregar un nuevo libro");
                System.out.println("3. Listar todos los libros");
                System.out.println("4. Eliminar un libro por ID");
                System.out.println("5. Actualizar un libro por ID");
                System.out.println("6. Contar libros por autor");
                System.out.println("7. Buscar libros por rango de ID");
                System.out.println("8. Mostrar estadísticas");
                System.out.println("9. Salir");
                System.out.print("Selecciona una opción: ");
                String opcion = scanner.nextLine();
                out.println(opcion);

                switch (opcion) {
                    case "1":  // Buscar libros por palabra clave
                        System.out.print("Introduce la palabra clave: ");
                        out.println(scanner.nextLine());
                        mostrarRespuestaServidor(in, true);
                        break;
                    case "2":  // Agregar un libro
                        System.out.print("Introduce el título del libro: ");
                        out.println(scanner.nextLine());
                        System.out.print("Introduce el autor del libro: ");
                        out.println(scanner.nextLine());
                        mostrarRespuestaServidor(in, false);
                        break;
                    case "3":  // Listar todos los libros
                        mostrarRespuestaServidor(in, true);
                        break;
                    case "4":  // Eliminar un libro por ID
                        System.out.print("Introduce el ID del libro a eliminar: ");
                        out.println(scanner.nextLine());
                        mostrarRespuestaServidor(in, false);
                        break;
                    case "5":  // Actualizar un libro por ID
                        System.out.print("Introduce el ID del libro a actualizar: ");
                        out.println(scanner.nextLine());
                        System.out.print("Introduce el nuevo título: ");
                        out.println(scanner.nextLine());
                        System.out.print("Introduce el nuevo autor: ");
                        out.println(scanner.nextLine());
                        mostrarRespuestaServidor(in, false);
                        break;
                    case "6":  // Contar libros por autor
                        System.out.print("Introduce el nombre del autor: ");
                        out.println(scanner.nextLine());
                        mostrarRespuestaServidor(in, false);
                        break;
                    case "7":  // Buscar libros por rango de ID
                        System.out.print("Introduce el ID de inicio: ");
                        out.println(scanner.nextLine());
                        System.out.print("Introduce el ID de fin: ");
                        out.println(scanner.nextLine());
                        mostrarRespuestaServidor(in, true);
                        break;
                    case "8":  // Mostrar estadísticas
                        mostrarRespuestaServidor(in, false);
                        break;
                    case "9":
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error en la conexión con el servidor: " + e.getMessage());
        }
    }

    private static void mostrarRespuestaServidor(BufferedReader in, boolean esListaLibros) throws IOException {
        List<String> libros = new ArrayList<>();
        String respuesta;
        while (!(respuesta = in.readLine()).equals("END")) {
            libros.add(respuesta);
        }

        if (esListaLibros) {
            mostrarLibrosConFormato(libros);
        } else {
            libros.forEach(System.out::println);
        }
    }

    private static void mostrarLibrosConFormato(List<String> libros) {
        System.out.println("\n" + "*".repeat(40));
        System.out.println("*           Lista de Libros           *");
        System.out.println("*".repeat(40));

        for (String libro : libros) {
            if (libro.isEmpty()) continue;
            String[] detalles = libro.split(", ");
            String id = detalles[0].replace("ID: ", "");
            String titulo = detalles[1].replace("Título: ", "");
            String autor = detalles[2].replace("Autor: ", "");

            System.out.println("*".repeat(40));
            System.out.printf("* %-36s *\n", "ID: " + id);
            System.out.printf("* %-36s *\n", "Título: " + titulo);
            System.out.printf("* %-36s *\n", "Autor: " + autor);
            System.out.println("*".repeat(40));
        }
        System.out.println("*".repeat(40));
    }
}