package domain;

import model.UserModel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Fresher on 22/06/2017.
 */
public class User {
    private UserModel model;
    ConcurrentHashMap<Integer, Task> mapTask;
    Thread threadListenMessageFromServer;
    Long lastTimeInteract;


    // todo: implement logic for user
}
