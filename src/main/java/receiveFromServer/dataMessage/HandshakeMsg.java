package receiveFromServer.dataMessage;

import utils.ByteUtils;

import java.nio.ByteBuffer;

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

    @Override
    protected void unPackData(byte[] data) {
        ByteBuffer buffer= ByteBuffer.wrap(data);
        this.targetController= buffer.get();
        this.cmdId= buffer.getShort();
        this.errCode= buffer.get();
        this.sessionToken= ByteUtils.readString(buffer);
        this.reconnectTime= buffer.getInt();
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public int getReconnectTime() {
        return reconnectTime;
    }
}
