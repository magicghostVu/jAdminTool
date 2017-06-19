package pack;

import receiveFromServer.OriginMessage;
import receiveFromServer.dataMessage.HandshakeMsg;
import receiveFromServer.dataMessage.LoginMsg;
import sendToServer.cmd.BaseCmd;
import sendToServer.cmd.HandShakeCmd;
import sendToServer.cmd.LoginCmd;
import sendToServer.msg.Message;
import utils.MessageUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CPU10340_LOCAL on 19/05/2017.
 */
public class __Main {

    public static void sendCmdToServer(SocketChannel channel, BaseCmd cmd) {
        try {
            Message message = MessageUtils.convertBaseCmdToMessage(cmd);
            ByteBuffer buffer = MessageUtils.convertMessageToBuffer(message);
            channel.write(buffer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void _main(String[] args) throws Exception {

        Map<Integer, Integer> mapTask = new ConcurrentHashMap<>();

        System.out.println("Hello");
        InetSocketAddress socketAddress = new InetSocketAddress("49.213.81.43", 10002);
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
        socketChannel.configureBlocking(false);
        HandShakeCmd handShakeCmd = new HandShakeCmd("");
        sendCmdToServer(socketChannel, handShakeCmd);
        System.out.println("handShake sent");
        mapTask.put(0, 1);
        //targetBuffer.clear();
        Selector selector = Selector.open();
        int operation = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        socketChannel.register(selector, operation);
        //selector.

        //0 is task handshake
        //1 is task login

        /* status: 1 sent
                   2 received
        * */

        //mapTask.put(1, false);
        Thread threadListenDataFromServer = new Thread(() -> {
            while (true) {
                try {
                    selector.select();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                //selector.select();
                Iterator<SelectionKey> allKeySelected = selector.selectedKeys().iterator();
                allKeySelected.forEachRemaining(keySelected -> {
                    try {
                        allKeySelected.remove();
                        SocketChannel currentChannel = (SocketChannel) keySelected.channel();
                        if (keySelected.isConnectable()) {
                            currentChannel.finishConnect();
                            System.out.println("Finish Connect");
                        }
                        // received package from server, at here, receive handshake sendToServer.msg
                        else if (keySelected.isReadable()) {
                            ByteBuffer tmpByteBuffer = ByteBuffer.allocate(1024);
                            currentChannel.read(tmpByteBuffer);
                            ByteBuffer target = ByteBuffer.wrap(tmpByteBuffer.array());
                            OriginMessage oMessage = new OriginMessage(target);
                            System.out.println("received package with cmdId: " + oMessage.getCmdID());
                            switch (oMessage.getCmdID()) {
                                case 0: {
                                    System.out.println("handShake msg received");
                                    HandshakeMsg msg= new HandshakeMsg(oMessage.getData());
                                    System.out.println("ErrCode :" + msg.getErrorCode() );
                                    System.out.println("handShake session token "+ msg.getSessionToken());
                                    System.out.println("handShake reconnectTime "+ msg.getReconnectTime());
                                    mapTask.put(0, 2);
                                    break;
                                }
                                case 1: {
                                    System.out.println("LoginMsg received");
                                    LoginMsg loginMsg= new LoginMsg(oMessage.getData());
                                    System.out.println("Error Code of login msg " +loginMsg.getErrorCode());
                                    mapTask.put(1, 2);
                                    break;
                                }
                            }
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                });

            }
        });
        threadListenDataFromServer.start();
        Thread threadSendLogin = new Thread(() -> {
            while (true) {
                // if task handShake done and mapTask not contain task login
                if (mapTask.get(0) == 2 && !mapTask.containsKey(1)) {
                    System.out.println("HandShake done and not send login, send login");
                    LoginCmd loginCmd = new LoginCmd("gmtool1");
                    sendCmdToServer(socketChannel,loginCmd);
                    mapTask.put(1, 1);
                    System.out.println("Sent login");
                    break;
                }
            }
        });
        threadSendLogin.start();


    }
}
