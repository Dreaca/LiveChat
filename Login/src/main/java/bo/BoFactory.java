package bo;

import bo.custom.impl.LoginBoImpl;

public class BoFactory {
    public static BoFactory boFactory;
    public static BoFactory getInstance(){
        return boFactory == null?new BoFactory():boFactory;
    }
    public SuperBo getBo(BO type){
        switch (type){
            case LOGIN:
                return new LoginBoImpl();
            default:
                return null;
        }
    }
    public  enum BO{
        LOGIN
    }
    private BoFactory(){

    }
}
