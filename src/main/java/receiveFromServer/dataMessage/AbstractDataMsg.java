package receiveFromServer.dataMessage;

import utils.ByteArray;

/**
 * Created by Fresher on 30/05/2017.
 */
public  class AbstractDataMsg {

    protected byte errorCode;

    public AbstractDataMsg(byte[] data) {

    }

    public byte getErrorCode() {
        return errorCode;
    }

    public void unpackData(byte[] data){

    }
    public ByteArray readErrorCode(byte[] data){
        try{
            ByteArray byteArray= new ByteArray(data);
            byteArray.readByte();// targetController
            byteArray.readShort();// cmdId
            this.errorCode = byteArray.readByte();// errorCode
            return byteArray;
        }catch (Exception e){
            return null;
        }
    }
}
