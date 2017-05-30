package sendToServer.cmd;

import utils.ByteArray;

/**
 * Created by CPU10340_LOCAL on 24/05/2017.
 */
//baseCmd will contain info to be send to server
public class BaseCmd {
    private int controllerId;
    private int typeId;
    private ByteArray body;


    public BaseCmd(int typeId) {
        this.typeId = typeId;
        setControllerId(1);
        body= new ByteArray();
    }

    public int getControllerId() {
        return controllerId;
    }

    public void setControllerId(int controllerId) {
        this.controllerId = controllerId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public ByteArray getBody() {
        return body;
    }

    public void setBody(ByteArray body) {
        this.body = body;
    }
    public boolean createBody(){
        return false;
    }
}
