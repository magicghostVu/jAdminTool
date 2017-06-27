package services;

import domain.User;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by magic_000 on 25/06/2017.
 */
public class ServiceListenSocketEventFromServer {
    // use one or many selector ??
    private Selector selector;
    private int operation = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;


    private boolean selectorIsSelecting;

    private Thread threadListenEventServer;


    private UserManagementService userManagementService;


     public void registerASocketChannel(SocketChannel socketChannel) {
        try {

            this.setSelectorIsSelecting(false);

            //System.out.println("socket channel is registered");
            socketChannel.register(selector, operation);
            //System.out.println();

            this.setSelectorIsSelecting(true);
        } catch (ClosedChannelException closeChannelException) {
            closeChannelException.printStackTrace();
        }
    }

    public ServiceListenSocketEventFromServer() {
        try {
            this.selector = Selector.open();
            System.out.println("selector is opened");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        // so thread listen will be init here or not??
        //todo : implement thread listen event here
        threadListenEventServer = new Thread(() -> {
            while (true) {
                if(!this.isSelectorIsSelecting()) continue;
                try {
                    //System.out.printf("se");
                    this.selector.select();
                    Iterator<SelectionKey> allKeySelected = selector.selectedKeys().iterator();
                    allKeySelected.forEachRemaining(keySelected -> {
                        try {
                            allKeySelected.remove();
                            SocketChannel socketChannel = (SocketChannel) keySelected.channel();
                            User u = this.getUserManagementService().getUserBySocketChannel(socketChannel);
                            if (keySelected.isConnectable()) {
                                System.out.println("connectable");
                                socketChannel.finishConnect();
                            }
                            if (keySelected.isAcceptable()) {
                                System.out.println("Accepted");
                            }

                            if(keySelected.isReadable()){
                                System.out.println("some data from server");
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    void initService() {
        // may be thread start here
        this.threadListenEventServer.start();
    }

    public boolean isSelectorIsSelecting() {
        return selectorIsSelecting;
    }

    public void setSelectorIsSelecting(boolean selectorIsSelecting) {
        this.selectorIsSelecting = selectorIsSelecting;
    }

    public UserManagementService getUserManagementService() {
        return userManagementService;
    }

    public void setUserManagementService(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }
}
