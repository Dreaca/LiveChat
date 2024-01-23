package dao;

import java.sql.SQLException;

public interface CrudDao<T> extends SuperDao {
    public T search(String search) throws SQLException;
}
