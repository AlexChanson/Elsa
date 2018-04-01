package dao;


import beans.UserCommune;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserComVTable extends BasicVirtualTable<UserCommune> {
    public UserComVTable() {
        super(UserCommune.class);
    }

    @Override
    public List<UserCommune> findAll(Object key1, String colName1, Object key2, String colName2){
        if (key1 == null || key2 == null)
            return null;
        String query = String.format("SELECT * from %s WHERE (%s = %s OR %s = -1) AND %s = %s ;", tableName, colName1, formatObjToString(key1), colName1, colName2, formatObjToString(key2));
        Object[] params = null;
        List<UserCommune> ret = new ArrayList<>();
        try {
            Statement statement = MYSQL.getConnection().createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();
            int paramsNb = myConstructor.getParameterCount();

            params = new Object[paramsNb];
            while (resultSet.next()) {
                for (int i = 1; i <= paramsNb; ++i)
                    params[i - 1] = resultSet.getObject(i);
                ret.add( (UserCommune) myConstructor.newInstance(params));
            }
            resultSet.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.printf("Error on importing %s from %s with parameters (%s).%n", myClass.getSimpleName(), tableName, Arrays.toString(params));

        }
        return ret;

    }
}




