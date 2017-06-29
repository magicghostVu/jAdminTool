package domain;

import config.ServerConfig;
import config.constant.CmdDefine;
import model.UserModel;
import receiveFromServer.OriginMessage;
import receiveFromServer.dataMessage.HandshakeMsg;
import receiveFromServer.dataMessage.LoginMsg;
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

    private Runnable functionProcessListen;

    private volatile boolean isRemoving= false;


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


            this.functionProcessListen= ()->{
                if(isRemoving) {
                    System.out.println("removing user"+ getModel().getUsername());
                    return;
                }
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
                            if (keySelected.isAcceptable()) {
                                System.out.println("accept key");
                            }
                            if (keySelected.isConnectable()) {
                                System.out.println("Finish connect");
                                currentChannel.finishConnect();
                            }
                            if (keySelected.isReadable()) {
                                if(!mapTask.isEmpty()){
                                    System.out.println("some data from server");
                                    ByteBuffer tmpByteBuffer = ByteBuffer.allocate(1024);
                                    currentChannel.read(tmpByteBuffer);
                                    System.out.println("Position of tmpBuffer " + tmpByteBuffer.position());
                                    if (tmpByteBuffer.position() != 0) {
                                        ByteBuffer target = ByteBuffer.wrap(tmpByteBuffer.array());
                                        OriginMessage oMessage = new OriginMessage(target);
                                        System.out.println("received package with cmdId: " + oMessage.getCmdID());
                                        if (oMessage.getBinDataLength() > 0) {
                                            handleReadData(oMessage);
                                        }
                                    }
                                }else{
                                    //System.out.println("map task is empty and still receive data from server");
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("exception in select key");
                        }
                    });

                }
            };

            /*this.threadSelectAndListenDataFromServer = new Thread(() -> {
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
                            if (keySelected.isAcceptable()) {
                                System.out.println("accept key");
                            }
                            if (keySelected.isConnectable()) {
                                System.out.println("Finish connect");
                                currentChannel.finishConnect();
                            }
                            if (keySelected.isReadable()) {
                                if(!mapTask.isEmpty()){
                                    System.out.println("some data from server");
                                    ByteBuffer tmpByteBuffer = ByteBuffer.allocate(1024);
                                    currentChannel.read(tmpByteBuffer);
                                    System.out.println("Position of tmpBuffer " + tmpByteBuffer.position());
                                    if (tmpByteBuffer.position() != 0) {
                                        ByteBuffer target = ByteBuffer.wrap(tmpByteBuffer.array());
                                        OriginMessage oMessage = new OriginMessage(target);
                                        System.out.println("received package with cmdId: " + oMessage.getCmdID());
                                        if (oMessage.getBinDataLength() > 0) {
                                            handleReadData(oMessage);
                                        }
                                    }
                                }else{
                                    //System.out.println("map task is empty and still receive data from server");
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("exception in select key");
                        }
                    });

                }
            });*/

            this.threadSelectAndListenDataFromServer= new Thread(functionProcessListen);
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


    public void updateInteractTime() {
        setLastTimeInteract(System.currentTimeMillis() / 1000L);
    }


    public OriginMessage executeTask(Task task) {
        updateInteractTime();
        mapTask.put(task.idTask, task);
        return task.executeTask(this);
    }

    public void handShakeAndLogin() {
        HandShakeCmd handShakeCmd = new HandShakeCmd("");
        Task taskHandShake = new Task(handShakeCmd);
        OriginMessage res = executeTask(taskHandShake);
        if (res != null) {
            HandshakeMsg handshakeMsg = new HandshakeMsg(res.getData());
            System.out.println("Session token " + handshakeMsg.getSessionToken());
            if (handshakeMsg.getErrorCode() == 0) {
                LoginCmd loginCmd = new LoginCmd(ServerConfig.USERNAME_BITZERO_PREFIX + getModel().getUsername());
                Task taskLogin = new Task(loginCmd);
                OriginMessage resLogin = executeTask(taskLogin);
                System.out.println("res login " + resLogin.getData());
                LoginMsg loginMsg = new LoginMsg(resLogin.getData());
                if (loginMsg.getErrorCode() != 0) {
                    System.out.println("login loi cmnr");
                }
            } else {
                System.out.println("Loi cmnr " + handshakeMsg.getErrorCode());
            }
        } else {
            System.out.println("Handshake err cmnr");
        }

    }


    private void handleReadData(OriginMessage originMessage) {
        //if(mapTask)
        if (mapTask.containsKey((int) originMessage.getCmdID())) {
            Task task = mapTask.get((int) originMessage.getCmdID());
            task.setDataMessageFromServer(originMessage);
            task.setStatus(TaskStatus.RECEIVED);
            // so should remove it from map task
            mapTask.remove((int) originMessage.getCmdID());
            /*switch (originMessage.getCmdID()) {
                case CmdDefine.HANDSHAKE: {
                    Task handShakeTask = mapTask.get((int) CmdDefine.HANDSHAKE);
                    handShakeTask.setDataMessageFromServer(originMessage);
                    handShakeTask.setStatus(TaskStatus.RECEIVED);
                    break;
                }
                case CmdDefine.LOGIN: {
                    Task loginTask= mapTask.get((int)CmdDefine.LOGIN);
                    loginTask.setDataMessageFromServer(originMessage);
                    loginTask.setStatus(TaskStatus.RECEIVED);
                    break;
                }
            }*/
        }
    }

    public Thread getThreadListen(){
        return threadSelectAndListenDataFromServer;
    }


    public Runnable getFunctionProcessListen() {
        return functionProcessListen;
    }


    public boolean isRemoving() {
        return isRemoving;
    }

    public void setRemoving(boolean removing) {

        System.out.println("set removing for user" + model.getUsername());

        isRemoving = removing;
    }
}
