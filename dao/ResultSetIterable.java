package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ResultSetIterable<T> implements Iterable<T> {

    private final ResultSet rs;
    private final Function<ResultSet, T> onNext;

    public ResultSetIterable(ResultSet rs, Function<ResultSet, T> onNext){
        this.rs = rs;
        this.onNext = onNext;
    }


    @Override
    public Iterator<T> iterator() {

        try {
            return new Iterator<T>() {

                boolean hasNext = rs.next();

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public T next() {

                    T result = onNext.apply(rs);
                    try {
                        hasNext = rs.next();
                    } catch (SQLException e) {
                        System.err.println("Failed to produce next object for Stream (SQL error)!");
                        e.printStackTrace();
                    }
                    return result;
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }
}
