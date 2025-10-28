package main.java.com.repo;

import main.java.com.exceptions.RepositoryException;

public abstract class AbstractFileRepository<K,T> extends AbstractRepository<K, T>{
    protected final String filePath;

    public AbstractFileRepository(String filePath) {
        this.filePath = filePath;
        loadFile();
    }

    protected abstract void loadFile();

    protected abstract void overwriteFile();

    @Override
    public void add(K key, T newData) throws RepositoryException {
        super.add(key, newData);
        overwriteFile();
    }

    @Override
    public void remove(K key) throws RepositoryException{
        super.remove(key);
        overwriteFile();
    }

    @Override
    public void modify(K key, T newData) throws RepositoryException{
        super.modify(key, newData);
        overwriteFile();
    }
}
