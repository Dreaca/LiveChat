package dao;

import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtil {

    public static  <T>T execute(String SQL,Object...args) throws SQLException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i+1,args);
        }
        if (SQL.startsWith("SELECT")){
            return (T) preparedStatement.executeQuery();
        }
        else {
            return (T)(Boolean)(preparedStatement.executeUpdate()>0);
        }
    }
}
