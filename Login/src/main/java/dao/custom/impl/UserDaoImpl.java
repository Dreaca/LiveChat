package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.UserDao;
import dto.UserDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    @Override
    public UserDto search(String search) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM login WHERE userName = ? ", search);
        resultSet.next();
        return new UserDto(
                resultSet.getString("userName"),
                resultSet.getString("passWord")
        );
    }
}
