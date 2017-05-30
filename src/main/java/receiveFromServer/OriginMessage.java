package receiveFromServer;

import java.nio.ByteBuffer;

/**
 * Created by Fresher on 30/05/2017.
 */
public class OriginMessage {
    private byte packageHeader;
    private short binDataLength;
    private byte[] data;


    public OriginMessage(ByteBuffer target) {
        packageHeader= target.get();
        binDataLength= target.getShort();
        data= new byte[binDataLength];
        target.get(data);
    }

    public byte getPackageHeader() {
        return packageHeader;
    }

    public short getBinDataLength() {
        return binDataLength;
    }

    public byte[] getData() {
        return data;
    }
}
