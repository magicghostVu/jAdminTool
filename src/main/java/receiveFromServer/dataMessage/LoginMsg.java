package receiveFromServer.dataMessage;

/**
 * Created by Fresher on 19/06/2017.
 */
public class LoginMsg extends AbstractDataMsg {
    public LoginMsg(byte[] data) {
        super(data);
    }
    @Override
    public void unpackData(byte[] data) {
        readErrorCode(data);
    }
}
