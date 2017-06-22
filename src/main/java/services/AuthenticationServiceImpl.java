package services;

import model.UserDAO;
import model.UserModel;
import utils.MD5Utils;
import utils.MyPair;

/**
 * Created by Fresher on 22/06/2017.
 */


// add this class to bean
public class AuthenticationServiceImpl implements AuthenticationService {

    // use cache strategy here, not implement cache
    //todo: implement in future

    UserDAO userDAO;

    public AuthenticationServiceImpl() {
        super();
    }

    @Override
    public MyPair<String, String> authenUsernamePassword(String username, String password) {
        UserModel model = userDAO.getUserModelByUserName(username);
        if (model == null) {
            return null;
        } else {
            String passwordHash= MD5Utils.hashStringToMD5(password);
            // authenticate successfully
            if(passwordHash.equals(model.getPasswordHash())){
                String accToken=  model.updateAccessToken();
                MyPair<String, String> pairAuthen= new MyPair<>(username, accToken);
                return pairAuthen;
            }else{
                return null;
            }
        }
    }

    @Override
    public UserModel authenAccessToken(String accessToken) {
        UserModel model= userDAO.getModelByAccessToken(accessToken);
        return model;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
