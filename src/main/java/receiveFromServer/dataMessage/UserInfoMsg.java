package receiveFromServer.dataMessage;

import utils.ByteArray;

/**
 * Created by Fresher on 29/06/2017.
 */
public class UserInfoMsg extends AbstractDataMsg {
    private int uid;
    private String ign="";
    private String deviceId="";
    public UserInfoMsg(byte[] data) {
        super(data);
        unpackData(data);
    }

    @Override
    public void unpackData(byte[] data) {
        try{
            ByteArray ba= readErrorCode(data);
            uid= ba.readInt();
            ign= ba.readUTF();
            deviceId= ba.readUTF();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
