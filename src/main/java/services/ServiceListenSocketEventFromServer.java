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


    private Thread threadListenEventServer;


    private UserManagementService userManagementService;


    synchronized public void registerASocketChannel(SocketChannel socketChannel) {
        try {

            System.out.println("socket channel is registered");

            socketChannel.register(selector, operation);
        } catch (ClosedChannelException closeChannelException) {
            closeChannelException.printStackTrace();
        }
    }

    public ServiceListenSocketEventFromServer() {
        try {
            this.selector = Selector.open();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // so thread listen will be init here or not??
        //todo : implement thread listen event here
        threadListenEventServer = new Thread(() -> {
            while (true) {
                try {
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
    }

    public UserManagementService getUserManagementService() {
        return userManagementService;
    }

    public void setUserManagementService(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }
}
