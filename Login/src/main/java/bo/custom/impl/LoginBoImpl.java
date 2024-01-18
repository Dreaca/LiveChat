package bo.custom.impl;

import bo.custom.LoginBo;

public class LoginBoImpl implements LoginBo {
    @Override
    public boolean checkValidity(String username, String password) {
        return false;
    }
}
