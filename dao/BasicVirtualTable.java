package dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class BasicVirtualTable<T> implements VirtualTable<T>{
    private Class myClass;
    private String tableName;
    private Constructor myConstructor;
    private Field key;

    public BasicVirtualTable(Class targeted) {
        myClass = targeted;
        try {
            tableName = ((Entity)myClass.getAnnotationsByType(Entity.class)[0]).tableName();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            myConstructor = getConstructor(myClass);
        } catch (NoSuchMethodException e){
            System.err.printf("No appropriate constructor found for %s, use @DaoConstructor to reference one.", myClass.getSimpleName());
        }
        for (Field field : targeted.getDeclaredFields()){
            if (field.getAnnotation(Key.class) != null)
                key = field;
        }

    }

    @Override
    public void delete(Object key) {

    }

    @Override
    public T find(Object key) {
        if (key == null){
            System.err.printf("No @Key declared for class %s !", myClass.getSimpleName());
            return null;
        }
        String colName = this.key.getAnnotation(Key.class).columnName();
        String formatedKey = key instanceof String ? "\'" + key + "\'" : key.toString();
        Object[] params = null;
        try {

            Statement statement = MYSQL.getConnection().createStatement();
            statement.execute("SELECT * from " + tableName + " WHERE "+colName+" = "+formatedKey+";");
            ResultSet resultSet = statement.getResultSet();
            int paramsNb = myConstructor.getParameterCount();
            params = new Object[paramsNb];
            if (!resultSet.next())
                return null;
            for (int i = 1; i <= paramsNb; ++i)
                params[i - 1] = resultSet.getObject(i);
            return (T) myConstructor.newInstance(params);

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.printf("Error on importing %s from %s with parameters (%s).%n", myClass.getSimpleName(), tableName, Arrays.toString(params));
            return null;
        }

    }

    /**
     * Adds the object to the table
     * @param o the object to insert
     */
    @Override
    public void add(T o) {
        StringBuilder querry = new StringBuilder("INSERT INTO " + tableName + " VALUE (");
        Class c = o.getClass();
        for (Field field : c.getDeclaredFields()){
            if (! Modifier.isTransient(field.getModifiers())){
                field.setAccessible(true);
                try {
                    querry.append(field.get(o).getClass().equals(String.class) ? "\'" + field.get(o) + "\'" : field.get(o));
                    querry.append(",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        querry.setCharAt(querry.lastIndexOf(","), ')');
        querry.append(";");


        try {
            Statement statement = MYSQL.getConnection().createStatement();
            statement.executeUpdate(querry.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Object key, T o) {

    }

    public List<T> getAll() {
        try {
            //Reference to your connexion singleton goes here
            Statement statement = MYSQL.getConnection().createStatement();
            statement.execute("SELECT * from " + tableName + ";");
            ResultSet resultSet = statement.getResultSet();
            List<T> result = new ArrayList<>();
            int paramsNb = myConstructor.getParameterCount();
            Object[] params = new Object[paramsNb];

            while (resultSet.next()) {

                for (int i = 1; i <= paramsNb; ++i) {
                    params[i - 1] = resultSet.getObject(i);
                }
                try {
                    result.add((T) myConstructor.newInstance(params));

                } catch (Exception e1) {
                    System.err.printf("Error on importing %s from %s with parameters (%s).%n", myClass.getSimpleName(), tableName, Arrays.toString(params));
                }
            }
            return result;
        } catch (SQLException e) {
            System.err.printf("Error on importing table : %s, error : %s.%n", tableName, e.getMessage());
            return null;
        }
    }

    public Stream<T> getStream(){
        Function<ResultSet, T> onNext = resultSet -> {
            try {
                int paramsNb = myConstructor.getParameterCount();
                Object[] params = new Object[paramsNb];
                for (int i = 1; i <= paramsNb; ++i) {
                    params[i - 1] = resultSet.getObject(i);
                }
                return (T) myConstructor.newInstance(params);
            } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException e){
                e.printStackTrace();
                return null;
            }


        };

        try {
            Statement statement = MYSQL.getConnection().createStatement();
            statement.execute("SELECT * from " + tableName + ";");
            ResultSet resultSet = statement.getResultSet();
            return new ResultSetIterable<T>(resultSet, onNext).stream();
        } catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<T>().stream();
        }
    }

    private static Constructor getConstructor(Class target) throws NoSuchMethodException {
        for (Constructor c :
                target.getConstructors()) {
            if (c.isAnnotationPresent(DaoConstructor.class))
                return c;
        }
        throw new NoSuchMethodException();
    }

}
