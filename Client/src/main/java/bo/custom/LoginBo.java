package bo.custom;

import bo.SuperBo;

import java.sql.SQLException;

public interface LoginBo extends SuperBo {
    boolean checkValidity(String username) throws SQLException;
}
