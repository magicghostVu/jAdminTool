package sendToServer.cmd;

import config.constant.CmdDefine;

/**
 * Created by Fresher on 29/06/2017.
 */
public class GetUserInfoCmd extends BaseCmd {

    int uId;

    public GetUserInfoCmd(int uId) {
        super(CmdDefine.ADMIN_GET_INFO);
        this.uId= uId;
    }

    @Override
    public boolean createBody() {
        try {
            this.getBody().writeInt(uId);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
