package services;

import domain.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by magic_000 on 25/06/2017.
 */
public class UserManagementServiceImpl implements UserManagementService {

    private Map<String, User> mapUsernameUser;

    public UserManagementServiceImpl() {
        mapUsernameUser = new ConcurrentHashMap<>();
    }



    @Override
    public boolean removeUserFromMap(User user) {
        return false;
    }

    @Override
    public boolean addUserToMap(User user) {
        mapUsernameUser.put(user.getModel().getUsername(), user);
        return true;
    }

    @Override
    public User getUserByUsername(String username) {
        return mapUsernameUser.get(username);
    }
    
}
