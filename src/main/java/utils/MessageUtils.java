package utils;

import sendToServer.cmd.BaseCmd;
import sendToServer.msg.Message;

import java.nio.ByteBuffer;

/**
 * Created by magic_000 on 31/05/2017.
 */
public class MessageUtils {


    static int packageHeaderEncoded = -128;

    public static ByteBuffer convertMessageToBuffer(Message message) {
        try {
            ByteArray tmpByteArray = new ByteArray();
            tmpByteArray.writeByte(message.getTargetController());
            tmpByteArray.writeShort((short) message.getId());
            tmpByteArray.writeBytes(message.getContent().getBytes());
            //message.setContent(tmpByteArray);
            ByteArray targetByteArr = new ByteArray();
            targetByteArr.writeByte(packageHeaderEncoded);
            targetByteArr.writeShort((short) tmpByteArray.getLength());
            targetByteArr.writeBytes(tmpByteArray.getBytes());
            ByteBuffer result = ByteBuffer.wrap(targetByteArr.getBytes());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Message convertBaseCmdToMessage(BaseCmd cmd) {
        Message result= new Message();
        result.setTargetController(cmd.getControllerId());
        result.setId(cmd.getTypeId());
        if(cmd.createBody()){
            result.setContent(cmd.getBody());
            return result;
        }else{
            throw new IllegalArgumentException("can not create body "+ cmd);
            //return null;
        }

    }
}
