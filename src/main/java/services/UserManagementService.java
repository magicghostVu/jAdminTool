package services;

import domain.User;

import java.nio.channels.SocketChannel;

/**
 * Created by magic_000 on 24/06/2017.
 */
public interface UserManagementService {
    User getUserByAccessToken(String accessToken);
    boolean removeUserFromMap(User user);
    boolean addUserToMap(User user);
    User getUserByUsername(String username);
    User getUserBySocketChannle(SocketChannel socketChannel);
}
