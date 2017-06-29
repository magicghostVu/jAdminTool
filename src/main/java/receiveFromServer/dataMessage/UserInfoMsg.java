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
    }

    @Override
    public void unpackData(byte[] data) {
        try{
            ByteArray ba= readErrorCode(data);

            

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
