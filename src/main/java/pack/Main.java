package pack;

import receiveFromServer.OriginMessage;
import receiveFromServer.dataMessage.HandshakeMsg;
import sendToServer.cmd.BaseCmd;
import sendToServer.cmd.HandShakeCmd;
import sendToServer.msg.Message;
import utils.ByteArray;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by CPU10340_LOCAL on 19/05/2017.
 */
public class Main {

    public static void sendCmdToServer(BaseCmd cmd){

    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello");
        InetSocketAddress socketAddress = new InetSocketAddress("49.213.81.43", 10002);
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
        socketChannel.configureBlocking(false);


        HandShakeCmd handShakeCmd = new HandShakeCmd("");

        Message message = new Message();
        message.setTargetController(handShakeCmd.getControllerId());
        message.setId(handShakeCmd.getTypeId());

        if (handShakeCmd.createBody()) {
            message.setContent(handShakeCmd.getBody());
        }

        ByteArray recreateByteArray = new ByteArray();
        recreateByteArray.writeByte(message.getTargetController());
        recreateByteArray.writeShort((short) message.getId());
        recreateByteArray.writeBytes(message.getContent().getBytes());

        message.setContent(recreateByteArray);


        //send to server via bytes
        int packageHeaderEncoded = -128;
        ByteArray binData = message.getContent();

        ByteArray array = new ByteArray();
        array.writeByte(packageHeaderEncoded);

        array.writeShort((short) binData.getLength());
        array.writeBytes(binData.getBytes());

        ByteBuffer targetBuffer = ByteBuffer.wrap(array.getBytes());

        socketChannel.write(targetBuffer);
        targetBuffer.clear();


        Selector selector = Selector.open();
        int operation = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;

        socketChannel.register(selector, operation);

        while (true) {
            int numberKey = selector.select();
            //System.out.println("");
            Iterator<SelectionKey> allKeySelected = selector.selectedKeys().iterator();
            allKeySelected.forEachRemaining( keySelected -> {
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
                        System.out.println("bin data length :" + oMessage.getBinDataLength());


                        HandshakeMsg handshakeMsg = new HandshakeMsg(oMessage.getData());

                        System.out.println(handshakeMsg.getSessionToken());


                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            });

        }


    }
}
