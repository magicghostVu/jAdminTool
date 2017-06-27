package domain;

import config.ServerConfig;
import config.constant.CmdDefine;
import model.UserModel;
import receiveFromServer.OriginMessage;
import receiveFromServer.dataMessage.HandshakeMsg;
import sendToServer.cmd.HandShakeCmd;
import sendToServer.cmd.LoginCmd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Fresher on 22/06/2017.
 */
public class User {

    private UserModel model;
    private ConcurrentHashMap<Integer, Task> mapTask;
    private Long lastTimeInteract;

    //channel send message to server and receive message from server
    private SocketChannel socketChannel;
    // todo: implement logic for user


    private Selector selector;

    private Thread threadSelectAndListenDataFromServer;


    //this constructor will be invoke
    public User(UserModel model) {
        this.model = model;
        mapTask = new ConcurrentHashMap<>();
        lastTimeInteract = System.currentTimeMillis() / 1000L;
        InetSocketAddress add = new InetSocketAddress(ServerConfig.BITZERO_SERVER_ADDRESS,
                ServerConfig.BITZERO_SERVER_PORT);
        try {
            socketChannel = SocketChannel.open(add);
            socketChannel.configureBlocking(false);
            this.selector = Selector.open();

            int operation = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
            socketChannel.register(this.selector, operation);

            this.threadSelectAndListenDataFromServer = new Thread(() -> {
                while (socketChannel.isOpen()) {
                    try {
                        selector.select();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        System.out.println("Select error");
                    }
                    Iterator<SelectionKey> allKeySelected = selector.selectedKeys().iterator();
                    allKeySelected.forEachRemaining(keySelected -> {
                        try {
                            allKeySelected.remove();
                            SocketChannel currentChannel = (SocketChannel) keySelected.channel();
                            if (keySelected.isConnectable()) {
                                System.out.println("Finish connect");
                                currentChannel.finishConnect();
                            }
                            if (keySelected.isReadable()) {
                                System.out.println("some data from server");
                                ByteBuffer tmpByteBuffer = ByteBuffer.allocate(1024);
                                currentChannel.read(tmpByteBuffer);
                                ByteBuffer target = ByteBuffer.wrap(tmpByteBuffer.array());
                                OriginMessage oMessage = new OriginMessage(target);
                                System.out.println("received package with cmdId: " + oMessage.getCmdID());

                                if (oMessage.getBinDataLength() > 0) {
                                    handleReadData(oMessage);
                                }
                                // handle read data from server

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("exception in select key");
                        }
                    });

                }
            });
            this.threadSelectAndListenDataFromServer.start();

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


    public OriginMessage executeTask(Task task) {
        mapTask.put(task.idTask, task);
        return task.executeTask(this);
        //return null;
    }

    public void handShakeAndLogin() {
        HandShakeCmd handShakeCmd = new HandShakeCmd("");
        Task taskHandShake = new Task(handShakeCmd);
        OriginMessage res = executeTask(taskHandShake);
        if (res != null) {
            HandshakeMsg msg = new HandshakeMsg(res.getData());
            System.out.println("Session token " + msg.getSessionToken());

            if (msg.getErrorCode() != 0) {
                LoginCmd loginCmd = new LoginCmd(ServerConfig.USERNAME_BITZERO_PREFIX + getModel().getUsername());
                Task taskLogin = new Task(loginCmd);
            }else{
                System.out.println("Loi cmnr "+ msg.);
            }




        } else {
            System.out.println("Handshake err cmnr");
        }

    }


    void handleReadData(OriginMessage originMessage) {

        //if(mapTask)
        switch (originMessage.getCmdID()) {
            case CmdDefine.HANDSHAKE: {
                if (mapTask.containsKey((int) CmdDefine.HANDSHAKE)) {
                    Task currentTask = mapTask.get((int) CmdDefine.HANDSHAKE);
                    currentTask.setDataMessageFromServer(originMessage);
                    currentTask.setStatus(TaskStatus.RECEIVED);

                    //System.out.println("task set status");
                }
            }
        }
    }

}
