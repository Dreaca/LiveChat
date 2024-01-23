package dao;

import dao.custom.impl.UserDaoImpl;

public class DaoFactory {
    public static DaoFactory daoFactory;

    private DaoFactory(){}
    public enum DAO{
        USER
    }
    public static DaoFactory getInstance(){
        return daoFactory == null ? new DaoFactory() : daoFactory;
    }
    public SuperDao getDao(DAO type){
        switch (type){
            case USER:
                return new UserDaoImpl();
            default:
                return null;
        }
    }
}
