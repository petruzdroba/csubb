package org.example.repo;

import org.example.domain.Book;
import org.example.domain.BorrowRequest;
import org.example.domain.DataBaseConfig;
import org.example.domain.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowRepo extends DatabaseConnection {
    private final BookRepo bookRepo;
    private final PatronRepo patronRepo;

    public BorrowRepo(String url, String user, String password, BookRepo bookRepo, PatronRepo patronRepo) {
        super(url, user, password);
        this.bookRepo = bookRepo;
        this.patronRepo = patronRepo;
    }

    public BorrowRepo(DataBaseConfig config, BookRepo bookRepo, PatronRepo patronRepo) {
        super(config);
        this.bookRepo = bookRepo;
        this.patronRepo = patronRepo;
    }

    public List<BorrowRequest> getAll() {
        String sql = "select * from book_requests WHERE status = 'REQUESTED'";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){


            List<BorrowRequest> borrowRequests = new ArrayList<>();
            while (rs.next()) {
                BorrowRequest req = new BorrowRequest(
                        rs.getLong("id"),
                        patronRepo.find(rs.getLong("patron_id")),
                        new ArrayList<>(),
                        rs.getTimestamp("date").toLocalDateTime(),
                        Status.valueOf(rs.getString("status"))
                );

                req.setBooks(fetchBooks(connection, rs.getLong("id")));
                borrowRequests.add(req);
            }
            return borrowRequests;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BorrowRequest> getAllByPatron(Long patronId) {
        String sql = "select * from book_requests WHERE patron_id = ? and status='BORROWED'";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, patronId);

            try(ResultSet rs = ps.executeQuery()){
                List<BorrowRequest> borrowRequests = new ArrayList<>();
                while (rs.next()) {
                    BorrowRequest req = new BorrowRequest(
                            rs.getLong("id"),
                            patronRepo.find(rs.getLong("patron_id")),
                            new ArrayList<>(),
                            rs.getTimestamp("date").toLocalDateTime(),
                            Status.valueOf(rs.getString("status"))
                    );

                    req.setBooks(fetchBooks(connection, rs.getLong("id")));
                    borrowRequests.add(req);
                }
                return borrowRequests;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public BorrowRequest find(Long key) {
        String sql = "SELECT * FROM book_requests WHERE id = ? and  status = 'REQUESTED'";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new BorrowRequest(
                        key,
                        patronRepo.find(rs.getLong("patron_id")),
                        new ArrayList<>(),
                        rs.getTimestamp("date").toLocalDateTime(),
                        Status.valueOf(rs.getString("status"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(Long requestId,Status status){
        String sql = "update book_requests set status=? where id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status.toString());
            ps.setLong(2, requestId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No notification found with id " + requestId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> fetchBooks(Connection connection, Long request_id) {
        List<Book> subs = new ArrayList<>();
        String sql = "SELECT * FROM borrowed_books WHERE request_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, request_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book u = bookRepo.find(rs.getLong("book_id"));
                    if (u != null) subs.add(u);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subs;
    }

    public void add(BorrowRequest request){
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                long reqId = insertRequest(connection, request);
                insertBooks(connection,reqId, request.getBooks());

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long insertRequest(Connection connection,BorrowRequest request){
        String sql = "insert into book_requests (patron_id, date, status) values (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, request.getPatron().getId());
            ps.setTimestamp(2, Timestamp.valueOf(request.getDate()));
            ps.setString(3, request.getStatus().toString());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);//db genearted id
                } else {
                    throw new SQLException("Failed to get generated ID for message");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertBooks(Connection connection, Long requestId, List<Book> books){
        String sql = "INSERT INTO borrowed_books (book_id, request_id) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Book book : books) {
                ps.setLong(1, book.getId());
                ps.setLong(2, requestId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
