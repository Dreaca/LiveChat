package dao;

import java.sql.SQLException;

public interface CrudDao<T> extends SuperDao {
     T search(String search) throws SQLException;
     boolean add(String username, String passs) throws SQLException;
}
