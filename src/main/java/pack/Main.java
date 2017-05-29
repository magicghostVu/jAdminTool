package pack;

import cmd.HandShakeCmd;
import msg.Message;
import utils.ByteArray;
import utils.ByteUtils;

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
    public static void main(String[] args) throws Exception {
        System.out.println("Hello");
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1101);
        SocketChannel socketChannel = SocketChannel.open(socketAddress);

        /*String msg = "This is message";
        byte[] data= msg.getBytes();

        ByteBuffer buffer= ByteBuffer.wrap(data);

        socketChannel.write(buffer);
        buffer.clear();

        socketChannel.close();*/

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
        int packageHeaderEncoded = 128;
        ByteArray bindata = message.getContent();

        ByteArray array = new ByteArray();
        array.writeByte(packageHeaderEncoded);
        array.writeShort((short) bindata.getLength());
        array.writeBytes(bindata.getBytes());

        ByteBuffer targetBuffer = ByteBuffer.wrap(array.getBytes());

        socketChannel.write(targetBuffer);
        targetBuffer.clear();

        /*long time = System.currentTimeMillis();
        while (true) {
            long currentTime= System.currentTimeMillis();
            if(currentTime-time>= 5000L){
                break;
            }
        }
        socketChannel.close();*/

        Selector selector= Selector.open();
        int operation= SelectionKey.OP_CONNECT|SelectionKey.OP_READ|SelectionKey.OP_WRITE;

        socketChannel.register(selector, operation);

        while (true){
            int numberKey=selector.select();
            //System.out.println("");
            Iterator<SelectionKey> allKeySelected= selector.selectedKeys().iterator();
            allKeySelected.forEachRemaining(keySelected->{
                try {
                    allKeySelected.remove();
                    SocketChannel currentChannel= (SocketChannel)keySelected.channel();
                    if(keySelected.isConnectable()){
                        currentChannel.finishConnect();
                        System.out.println("Finish Connect");
                    }

                    // received package from server, at here, receive handshake msg
                    else if(keySelected.isReadable()){
                        ByteBuffer tmpByteBuffer= ByteBuffer.allocate(1024);
                        currentChannel.read(tmpByteBuffer);
                        ByteBuffer target= ByteBuffer.wrap(tmpByteBuffer.array());

                        byte packageHeader= target.get();

                        System.out.println("Package Header:" + packageHeader);
                        short cmdId= targetBuffer.getShort();

                        byte errorCode= targetBuffer.get();

                        //String sessionToken= ByteUtils.





                    }
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            });

        }



    }
}
