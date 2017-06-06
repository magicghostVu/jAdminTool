package sendToServer.cmd;

/**
 * Created by Fresher on 02/06/2017.
 */
public class LoginCmd extends BaseCmd {
    private String sessionKey;
    private String trackingData= "{}";
    private String socialData= "{}";
    public LoginCmd(int typeId) {
        super(1);
    }


    // sessionKey is username (gmtool_xx)
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getTrackingData() {
        return trackingData;
    }

    public void setTrackingData(String trackingData) {
        this.trackingData = trackingData;
    }

    public String getSocialData() {
        return socialData;
    }

    public void setSocialData(String socialData) {
        this.socialData = socialData;
    }

    @Override
    public boolean createBody() {
        try {
            getBody().writeUTF(sessionKey);
            getBody().writeUTF(trackingData);
            getBody().writeUTF(socialData);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
