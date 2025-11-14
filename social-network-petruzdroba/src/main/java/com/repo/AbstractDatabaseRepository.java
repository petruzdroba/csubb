package com.repo;

import com.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDatabaseRepository<K,T> extends AbstractRepository<K, T> {
    protected final String url;
    protected final String user;
    protected final String password;

    public AbstractDatabaseRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        loadFromDb();
    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, user, password);
    }

    protected abstract void loadFromDb();
    protected abstract void addToDb(K key, T entity) throws SQLException;
    protected abstract void removeFromDb(K key) throws SQLException;
    protected abstract void modifyInDb(K key, T entity) throws SQLException;

    @Override
    public void add(K key, T newData) throws RepositoryException {
        super.add(key, newData);
        try {
            addToDb(key, newData);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to add to database: " + e.getMessage());
        }
    }

    @Override
    public void remove(K key) throws RepositoryException{
        super.remove(key);
        try {
            removeFromDb(key);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to remove from database: " + e.getMessage());
        }
    }

    @Override
    public void modify(K key, T newData) throws RepositoryException{
        super.modify(key, newData);
        try {
            modifyInDb(key, newData);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to modify in database: " + e.getMessage());
        }
    }
}
