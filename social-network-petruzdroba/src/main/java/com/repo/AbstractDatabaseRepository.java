package com.repo;

import com.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

public abstract class AbstractDatabaseRepository<K,T>{
    protected final String url;
    protected final String user;
    protected final String password;

    public AbstractDatabaseRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RepositoryException(e.getMessage());
        }
        return DriverManager.getConnection(url, user, password);
    }

    public abstract void add(K key, T entity) throws SQLException;
    public abstract void remove(K key) throws SQLException;
    public abstract void modify(K key, T entity) throws SQLException;
    public abstract T find(K key) throws  SQLException;
    public abstract Collection<T> getAll();
    public abstract Collection<K> getKeys();
}
