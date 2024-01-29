package bo.custom.impl;

import bo.custom.LoginBo;
import dao.DaoFactory;
import dao.custom.UserDao;
import dto.UserDto;
import entity.User;

import java.sql.SQLException;

public class LoginBoImpl implements LoginBo {
    UserDao dao = (UserDao) DaoFactory.getInstance().getDao(DaoFactory.DAO.USER);
    @Override
    public boolean checkValidity(String username) throws SQLException {
        User search = dao.search(username);
        System.out.println(search);
        return (search.getUserId()>0);
    }
}
