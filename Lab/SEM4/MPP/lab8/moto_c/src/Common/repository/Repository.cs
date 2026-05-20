namespace moto_c.repository;

public interface Repository<K, T>
{
    T find(K key);
    
    List<T> getAll();  
}