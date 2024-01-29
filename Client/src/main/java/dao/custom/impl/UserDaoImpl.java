package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.UserDao;
import dto.UserDto;
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
                resultSet.getString("userName")
        );
    }
}
