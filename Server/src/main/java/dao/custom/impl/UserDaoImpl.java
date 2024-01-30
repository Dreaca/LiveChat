package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.UserDao;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    @Override
    public User search(String username) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM user WHERE userName = ? ", username);
        resultSet.next();
        return new User(
                resultSet.getInt("userId"),
                resultSet.getString("userName"),
                resultSet.getString("passWord")
        );
    }

    @Override
    public boolean add(String username, String passs) throws SQLException {
        int nextID = getNextID();
        return SQLUtil.execute("INSERT INTO user VALUES()",nextID,username, passs);
    }

    private int getNextID() throws SQLException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM user");
        rst.next();
        return rst.getInt(1)+1;
    }
}
