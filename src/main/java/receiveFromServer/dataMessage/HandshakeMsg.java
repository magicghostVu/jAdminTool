package receiveFromServer.dataMessage;

import utils.ByteArray;

/**
 * Created by Fresher on 30/05/2017.
 */
public class HandshakeMsg extends AbstractDataMsg {
    private String sessionToken;
    private int reconnectTime;
    public HandshakeMsg(byte[] data) {
        super(data);
        unPackData(data);
    }


    public void unPackData(byte[] data) {
        ByteArray byteArray= new ByteArray(data);
        try {

            byteArray.readByte();
            byteArray.readShort();
            byteArray.readByte();


            sessionToken= byteArray.readUTF();
            reconnectTime= byteArray.readInt();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public int getReconnectTime() {
        return reconnectTime;
    }
}
