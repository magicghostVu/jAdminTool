package sendToServer.cmd;

/**
 * Created by CPU10340_LOCAL on 24/05/2017.
 */
public class HandShakeCmd extends BaseCmd {

    private String token="";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HandShakeCmd(String token) {
        super(0);
        this.token = token;
        this.setControllerId(0);
    }

    public boolean createBody(){
        try {
            this.getBody().writeUTF(this.getToken());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
