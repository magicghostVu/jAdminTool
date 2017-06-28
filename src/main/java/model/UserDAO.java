package model;

/**
 * Created by Fresher on 20/06/2017.
 */


/// chỉ cung cấp các hàm làm việc với db, mà không cần nói rõ hàm làm việc như nào
public interface UserDAO {

    UserModel getUserModelByUserName(String username);

    boolean saveUserModel(UserModel model);

    boolean modelExist(UserModel model);

    UserModel getModelByAccessToken(String accTk);

}
