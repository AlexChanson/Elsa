package dao;

import java.util.List;

/**
 * Interface to be implemented by concrete DAO classes
 * @param <T> The type of the object mapped to the sql table
 */
public interface VirtualTable<T> {
    T find(Object key);
    void add(T o);
    void update(Object key, T o);
    void delete(Object key);
    List<T> getAll();
}
