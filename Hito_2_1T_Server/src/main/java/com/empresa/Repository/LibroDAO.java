package com.empresa.Repository;

import com.empresa.Model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/servidor_libros";
    private static final String USER = "root";
    private static final String PASSWORD = "curso";

    public LibroDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL: " + e.getMessage());
        }
    }

    public List<Libro> buscarLibros(String palabraClave) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE titulo LIKE ? OR autor LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + palabraClave + "%");
            stmt.setString(2, "%" + palabraClave + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                libros.add(new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor")));
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage());
        }
        return libros;
    }

    public void agregarLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar el libro: " + e.getMessage());
        }
    }

    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                libros.add(new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los libros: " + e.getMessage());
        }
        return libros;
    }

    public boolean eliminarLibro(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el libro: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarLibro(int id, String nuevoTitulo, String nuevoAutor) {
        String sql = "UPDATE libros SET titulo = ?, autor = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoTitulo);
            stmt.setString(2, nuevoAutor);
            stmt.setInt(3, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el libro: " + e.getMessage());
        }
        return false;
    }

    public int contarLibrosPorAutor(String autor) {
        String sql = "SELECT COUNT(*) AS cantidad FROM libros WHERE autor = ?";
        int cantidad = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, autor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cantidad = rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar libros por autor: " + e.getMessage());
        }
        return cantidad;
    }

    public List<Libro> buscarLibrosPorRangoID(int idInicio, int idFin) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE id BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInicio);
            stmt.setInt(2, idFin);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                libros.add(new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor")));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libros por rango de ID: " + e.getMessage());
        }
        return libros;
    }

    public int contarTotalLibros() {
        String sql = "SELECT COUNT(*) AS total FROM libros";
        int totalLibros = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                totalLibros = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar el total de libros: " + e.getMessage());
        }
        return totalLibros;
    }

    public int contarAutoresUnicos() {
        String sql = "SELECT COUNT(DISTINCT autor) AS autoresUnicos FROM libros";
        int autoresUnicos = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                autoresUnicos = rs.getInt("autoresUnicos");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar autores únicos: " + e.getMessage());
        }
        return autoresUnicos;
    }
}
