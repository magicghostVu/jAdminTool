package receiveFromServer.dataMessage;

/**
 * Created by Fresher on 30/05/2017.
 */
public abstract class AbstractDataMsg {
    protected byte targetController;
    protected short cmdId;
    protected byte errCode;
    public AbstractDataMsg(byte[] data) {
        //unPackData(data);
    }

    protected abstract void unPackData(byte[] data);

    public byte getTargetController() {
        return targetController;
    }

    public short getCmdId() {
        return cmdId;
    }

    public byte getErrCode() {
        return errCode;
    }
}
