package domain;

import config.ServerConfig;
import model.UserModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Fresher on 22/06/2017.
 */
public class User {
    private UserModel model;
    ConcurrentHashMap<Integer, Task> mapTask;
    Long lastTimeInteract;
    //channel send message to server and receive message from server
    SocketChannel socketChannel;
    // todo: implement logic for user


    public User(UserModel model) {
        this.model = model;
        mapTask = new ConcurrentHashMap<>();
        lastTimeInteract = System.currentTimeMillis() / 1000L;
        InetSocketAddress add = new InetSocketAddress(ServerConfig.BITZERO_SERVER_ADDRESS,
                ServerConfig.BITZERO_SERVER_PORT);
        try {
            socketChannel = SocketChannel.open(add);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public UserModel getModel() {
        return model;
    }

    public void setModel(UserModel model) {
        this.model = model;
    }

    public ConcurrentHashMap<Integer, Task> getMapTask() {
        return mapTask;
    }

    public void setMapTask(ConcurrentHashMap<Integer, Task> mapTask) {
        this.mapTask = mapTask;
    }

    public Long getLastTimeInteract() {
        return lastTimeInteract;
    }

    public void setLastTimeInteract(Long lastTimeInteract) {
        this.lastTimeInteract = lastTimeInteract;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }



}
