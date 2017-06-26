package services;

import domain.User;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by magic_000 on 25/06/2017.
 */
public class UserManagementServiceImpl implements UserManagementService {

    private Map<String, User> mapUsernameUser;
    private Map<String, User> mapAccessTokenUser;
    private Map<SocketChannel, User> mapSocketChannelUser;
    public UserManagementServiceImpl(){
        mapUsernameUser=new ConcurrentHashMap<>();
        mapAccessTokenUser= new ConcurrentHashMap<>();
        mapSocketChannelUser= new ConcurrentHashMap<>();
    }

    @Override
    public User getUserByAccessToken(String accessToken) {
        return mapAccessTokenUser.get(accessToken);
    }

    @Override
    public boolean removeUserFromMap(User user) {
        return false;
    }

    @Override
    public boolean addUserToMap(User user) {
        mapUsernameUser.put(user.getModel().getUsername(), user);
        mapAccessTokenUser.put(user.getModel().getAccessToken(), user);
        mapSocketChannelUser.put(user.getSocketChannel(), user);
        return true;
    }

    @Override
    public User getUserByUsername(String username) {
        return mapUsernameUser.get(username);
    }

    @Override
    public User getUserBySocketChannel(SocketChannel socketChannel) {
        return mapSocketChannelUser.get(socketChannel);
    }
}
