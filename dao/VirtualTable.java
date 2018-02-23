package dao;

import java.util.ArrayList;

public interface VirtualTable<T> {
    void commit();
    void rebase();
    T find(Object key);
    void add(T o);
    void update(Object key, T o);
}
