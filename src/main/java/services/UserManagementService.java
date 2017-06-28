package services;

import domain.User;

/**
 * Created by magic_000 on 24/06/2017.
 */
public interface UserManagementService {

    boolean removeUserFromMap(User user);

    boolean addUserToMap(User user);

    User getUserByUsername(String username);

    //User getUserBySocketChannel(SocketChannel socketChannel);
}
