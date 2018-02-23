package dao;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicVirtualTable<T> implements VirtualTable<T>{
    public BasicVirtualTable() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rebase() {

    }

    @Override
    public T find(Object key) {
        return null;
    }

    @Override
    public void add(T o) {

    }

    @Override
    public void update(Object key, T o) {

    }

    List<T> getAll(String tableName, Class target) {
        try {
            //Reference to your connexion singleton goes here
            Statement statement = MYSQL.getConnection().createStatement();
            statement.execute("SELECT * from " + tableName + ";");
            ResultSet resultSet = statement.getResultSet();
            List<T> result = new ArrayList<>();
            Constructor constructor = getConstructor(target);
            int paramsNb = constructor.getParameterCount();
            Object[] params = new Object[paramsNb];

            while (resultSet.next()) {

                for (int i = 1; i <= paramsNb; ++i) {
                    params[i - 1] = resultSet.getObject(i);
                }
                try {
                    result.add((T) constructor.newInstance(params));

                } catch (Exception e1) {
                    System.err.printf("Error on importing %s from %s with parameters (%s).%n", target.getSimpleName(), tableName, Arrays.toString(params));
                }
            }
            return result;
        } catch (SQLException e) {
            System.err.printf("Error on importing table : %s, error : %s.%n", tableName, e.getMessage());
            return null;
        } catch (NoSuchMethodException e) {
            System.err.printf("No appropriate constructor found for %s, use @DaoConstructor to reference one.", target.getSimpleName());
            return null;
        }
    }

    static Constructor getConstructor(Class target) throws NoSuchMethodException {
        for (Constructor c :
                target.getConstructors()) {
            if (c.isAnnotationPresent(DaoConstructor.class))
                return c;
        }
        throw new NoSuchMethodException();
    }
}
