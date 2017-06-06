package pack;

import receiveFromServer.OriginMessage;
import receiveFromServer.dataMessage.HandshakeMsg;
import sendToServer.cmd.BaseCmd;
import sendToServer.cmd.HandShakeCmd;
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
public class Main {

    public static void sendCmdToServer(BaseCmd cmd) {

    }

    public static void main(String[] args) throws Exception {

        Map<Integer, Boolean> mapTask= new ConcurrentHashMap<>();

        System.out.println("Hello");
        InetSocketAddress socketAddress = new InetSocketAddress("49.213.81.43", 10002);
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
        socketChannel.configureBlocking(false);
        HandShakeCmd handShakeCmd = new HandShakeCmd("");
        Message message= MessageUtils.convertBaseCmdToMessage(handShakeCmd);
        ByteBuffer targetBuffer= MessageUtils.convertMessageToBuffer(message);
        socketChannel.write(targetBuffer);

        System.out.println("handShake sent");
        targetBuffer.clear();
        Selector selector = Selector.open();
        int operation = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        socketChannel.register(selector, operation);
        //selector.

        //0 is task handshake
        //1 is task login

        mapTask.put(0, false);
        mapTask.put(1, false);

        Thread threadListenDataFromServer= new Thread(()->{
            while (true) {
                try {
                    selector.select();
                }catch (IOException ioe){
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
                            System.out.println("received package with cmdId: "+ oMessage.getCmdID());
                            switch (oMessage.getCmdID()){
                                case 0:{
                                    System.out.println("handShake msg received");
                                    mapTask.put(0, true);
                                    break;
                                }

                                case 1:{
                                    System.out.println("LoginMsg rec");
                                    
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

        Thread threadSendLogin = new Thread(()->{

            while (true){
                if(mapTask.get(1)){

                }
            }


        });



    }
}
