package services;

import config.ServerConfig;
import domain.User;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by magic_000 on 25/06/2017.
 */
public class UserManagementServiceImpl implements UserManagementService {

    private Map<String, User> mapUsernameUser;


    public UserManagementServiceImpl() {
        mapUsernameUser = new ConcurrentHashMap<>();
        Runnable cleanLongNotInteractUser = () -> mapUsernameUser
                .forEach((username, user) -> {
            long idleTime=System.currentTimeMillis()/1000L- user.getLastTimeInteract();
            if(idleTime> ServerConfig.TIME_OUT_INTERACT){
                System.out.println("user "+ username+ "cleaned");
                removeUserFromMap(user);
            }
        });
        ScheduledExecutorService executorService= Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(cleanLongNotInteractUser, 0L,5L, TimeUnit.SECONDS);
    }


    @Override
    public boolean removeUserFromMap(User user) {
        try {
            System.out.println("try remove user "+user.getModel().getUsername());
            user.getSocketChannel().close();
            user.setRemoving(true);
            user.getThreadListen().join();
            mapUsernameUser.remove(user.getModel().getUsername());
        } catch (IOException | InterruptedException ioe) {
            System.out.println("Close channel err");
            return false;
        }
        return true;
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
