package bo.custom.impl;

import bo.custom.LoginBo;
import dao.DaoFactory;
import dao.custom.UserDao;
import dto.UserDto;

import java.sql.SQLException;

public class LoginBoImpl implements LoginBo {
    UserDao dao = (UserDao) DaoFactory.getInstance().getDao(DaoFactory.DAO.USER);
    @Override
    public boolean checkValidity(String username, String password) throws SQLException {
        UserDto search = dao.search(username);
        return (search.getPassWord().equals(password));
    }
}
