package bo.custom;

import bo.SuperBo;

public interface LoginBo extends SuperBo {
    boolean checkValidity(String username, String password);
}
