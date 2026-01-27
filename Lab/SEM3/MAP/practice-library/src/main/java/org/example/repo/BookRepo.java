package org.example.repo;

import org.example.domain.Book;
import org.example.domain.BorrowRequest;
import org.example.domain.DataBaseConfig;
import org.example.domain.Patron;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepo extends DatabaseConnection {
    public BookRepo(String url, String user, String password) {
        super(url, user, password);
    }

    public BookRepo(DataBaseConfig config) {
        super(config);
    }

    public List<Book> getAll() {

        String sql = "SELECT * FROM books";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Book> values = new ArrayList<>();
            while (rs.next()) {
                Book entity = new Book(rs.getLong("id"), rs.getString("genre"), rs.getString("title"), rs.getString("author"), rs.getBoolean("available"));
                values.add(entity);
            }
            return values;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    public List<String> getGenres() {
        String sql = "SELECT DISTINCT genre FROM books";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<String> values = new ArrayList<>();
            while (rs.next()) {
                values.add(rs.getString("genre"));
            }
            return values;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    public List<Book> getByGenreAvailable(String genre) {
        String sql = "SELECT * FROM books WHERE genre = ? AND available = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, genre);
            ps.setBoolean(2, true);

            try (ResultSet rs = ps.executeQuery()) {
                List<Book> values = new ArrayList<>();
                while (rs.next()) {
                    Book entity = new Book(rs.getLong("id"), rs.getString("genre"), rs.getString("title"), rs.getString("author"), rs.getBoolean("available"));
                    values.add(entity);
                }
                return values;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    public Book find(Long key) {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new Book(
                        key,
                        rs.getString("genre"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("available")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

