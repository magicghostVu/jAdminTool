package model;

/**
 * Created by Fresher on 20/06/2017.
 */
public interface UserDAO {

    UserModel getUserModelByUserName(String username);

    boolean saveUserModel(UserModel model);

    boolean modelExist(UserModel model);

    UserModel getModelByAccessToken(String accTk);

}
