package br.com.senac.series.DAO;

import br.com.senac.series.Model.Serie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SerieDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/series-api";
    private static final String USER = "root";
    private static final String PASS = "root";

    public SerieDAO() {
    }

    private Connection conectar() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public Serie inserir(Serie serie) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO serie (nome, genero) VALUES (?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, serie.getNome());
            stmt.setString(2, serie.getGenero());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    serie.setId(rs.getInt(1));
                }
            }
        }
        return serie;
    }

    public Serie buscarPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM serie WHERE id = ?";
        Serie serie = null;
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    serie = new Serie(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("genero")
                    );
                }
            }
        }
        return serie;
    }

    public List<Serie> listarTodos() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM serie";
        List<Serie> series = new ArrayList<>();
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                series.add(new Serie(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("genero")
                ));
            }
        }
        return series;
    }

    public boolean atualizar(Serie serie) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE serie SET nome = ?, genero = ? WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, serie.getNome());
            stmt.setString(2, serie.getGenero());
            stmt.setInt(3, serie.getId());
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public boolean excluirPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM serie WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

}