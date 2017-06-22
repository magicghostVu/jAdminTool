package model;

import config.ServerConfig;
import config.constant.AccountType;
import globalAppContext.GlobalApplicationContextWrap;
import org.bson.Document;
import org.json.JSONObject;
import utils.MD5Utils;
import utils.MyPair;

/**
 * Created by Fresher on 20/06/2017.
 */
public class UserModel extends AbstractModel implements UserRepository {
    public UserModel(String username, AccountType accountType) {
        setUsername(username);
        setAccountType(accountType);
        setPasswordHash("");
        setLastTimeUpdate(System.currentTimeMillis() / 1000L);
        setPasswordChanged(false);
        setTimeCreateAccessToken(0L);
        setAccessToken("");
    }

    //properties
    private String username;
    private String passwordHash;
    private AccountType accountType;
    private long lastTimeUpdate;
    private boolean passwordChanged;
    private long timeCreateAccessToken;
    private String accessToken;

    @Override
    public boolean save() {
        setLastTimeUpdate(System.currentTimeMillis() / 1000L);
        UserDAOImpl userDAO = GlobalApplicationContextWrap.getInstance().
                getApplicationContext().getBean("user_DAO_impl", UserDAOImpl.class);
        return userDAO.saveUserModel(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public long getLastTimeUpdate() {
        return lastTimeUpdate;
    }

    public void setLastTimeUpdate(long lastTimeUpdate) {
        this.lastTimeUpdate = lastTimeUpdate;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public long getTimeCreateAccessToken() {
        return timeCreateAccessToken;
    }

    public void setTimeCreateAccessToken(long timeCreateAccessToken) {
        this.timeCreateAccessToken = timeCreateAccessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toJsonString() {
        String gsonString = ServerConfig.globalGson.toJson(this);
        JSONObject jo = new JSONObject(gsonString);
        jo.remove("username");
        jo.put("_id", username);
        return jo.toString();
    }

    @Override
    public Document toDocument() {
        Document res = new Document();
        JSONObject tmpJo = new JSONObject(toJsonString());
        tmpJo.keySet().forEach(key -> {
            res.put(key, tmpJo.get(key));
        });
        res.put("_id", username);
        res.remove("username");
        return res;
    }

    public String updateAccessToken() {
        Long timeStamp = System.currentTimeMillis() / 1000L;
        String source = timeStamp.toString() + getUsername();
        String accToken= MD5Utils.hashStringToMD5(source);
        setAccessToken(accToken);
        setTimeCreateAccessToken(timeStamp);
        save();
        return accToken;
    }


    public void clearAccessToken(){
        setAccessToken("_");
        setTimeCreateAccessToken(0L);
        save();
    }

}
