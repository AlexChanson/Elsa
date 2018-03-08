package dao;

import java.util.ArrayList;
import java.util.List;

public interface VirtualTable<T> {
    T find(Object key);
    void add(T o);
    void update(Object key, T o);
    void delete(Object key);
    List<T> getAll();
}
