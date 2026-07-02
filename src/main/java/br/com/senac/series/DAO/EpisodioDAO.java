package br.com.senac.series.DAO;
import br.com.senac.series.Model.Episodio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EpisodioDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/series-api";
    private static final String USER = "root";
    private static final String PASS = "root";
    public EpisodioDAO() {
    }
    private Connection conectar() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public Episodio inserir(Episodio episodio) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO episodio (titulo, duracao, temporada, id_serie) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, episodio.getTitulo());
            stmt.setInt(2, episodio.getDuracao());
            stmt.setInt(3, episodio.getTemporada());
            stmt.setInt(4, episodio.getIdSerie());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    episodio.setId(rs.getInt(1));
                }
            }
        }
        return episodio;
    }

    public Episodio buscarPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM episodio WHERE id = ?";
        Episodio episodio = null;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    episodio = new Episodio(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getInt("duracao"),
                            rs.getInt("temporada"),
                            rs.getInt("id_serie")
                    );
                }
            }
        }

        return episodio;
    }

    public List<Episodio> listarTodos() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM episodio";
        List<Episodio> episodios = new ArrayList<>();

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                episodios.add(new Episodio(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getInt("duracao"),
                        rs.getInt("temporada"),
                        rs.getInt("id_serie")
                ));
            }
        }

        return episodios;
    }

    public List<Episodio> listarPorSerie(int idSerie) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM episodio WHERE id_serie = ?";
        List<Episodio> episodios = new ArrayList<>();
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSerie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    episodios.add(new Episodio(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getInt("duracao"),
                            rs.getInt("temporada"),
                            rs.getInt("id_serie")
                    ));
                }
            }
        }
        return episodios;
    }

    public boolean atualizar(Episodio episodio) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE episodio SET titulo = ?, duracao = ?, temporada = ?, id_serie = ? WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, episodio.getTitulo());
            stmt.setInt(2, episodio.getDuracao());
            stmt.setInt(3, episodio.getTemporada());
            stmt.setInt(4, episodio.getIdSerie());
            stmt.setInt(5, episodio.getId());
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public boolean excluirPorId(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM episodio WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }
}