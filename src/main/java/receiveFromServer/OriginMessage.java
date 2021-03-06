package receiveFromServer;

import java.nio.ByteBuffer;

/**
 * Created by Fresher on 30/05/2017.
 */
public class OriginMessage {
    private byte packageHeader;
    private short binDataLength;
    private byte[] data;
    private short cmdID;
    private byte targetController;

    public OriginMessage(ByteBuffer target) {
        packageHeader = target.get();
        binDataLength = target.getShort();
        if (binDataLength == 0) {
            cmdID = -1;
        } else {
            data = new byte[binDataLength];
            target.get(data);
            ByteBuffer dataBuffer = ByteBuffer.wrap(data);
            try {
                targetController = dataBuffer.get();
                cmdID = dataBuffer.getShort();
            } catch (Exception e) {
                System.out.println("bindata lenght " + binDataLength);
            }
        }
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

    public short getCmdID() {
        return cmdID;
    }

    public byte getTargetController() {
        return targetController;
    }
}
