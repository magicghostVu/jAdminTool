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
        unpackData(data);
    }


    @Override
    public void unpackData(byte[] data) {
        try {
            ByteArray ba= readErrorCode(data);
            sessionToken = ba.readUTF();
            reconnectTime = ba.readInt();
        } catch (Exception e) {
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
