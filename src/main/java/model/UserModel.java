package model;

import config.ServerConfig;
import config.constant.AccountType;
import globalAppContext.GlobalApplicationContextWrap;
import org.json.JSONObject;

/**
 * Created by Fresher on 20/06/2017.
 */
public class UserModel extends AbstractModel  implements UserRepository{
    public UserModel(String username, AccountType accountType){
            setUsername(username);
            setAccountType(accountType);
            setPasswordHash("");
            setLastTimeUpdate(System.currentTimeMillis()/1000L);
            setPasswordChanged(false);
    }
    //properties
    private String username;
    private String passwordHash;
    private AccountType accountType;
    private long lastTimeUpdate;
    private boolean passwordChanged;

    @Override
    public boolean save() {
       // implement more here
        setLastTimeUpdate(System.currentTimeMillis()/1000L);
        //todo: implement save to db here
        UserDAOImpl userDAO= GlobalApplicationContextWrap.getInstance().
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

    @Override
    public String toJsonString() {
        String gsonString= ServerConfig.globalGson.toJson(this);
        JSONObject jo= new JSONObject(gsonString);
        jo.remove("username");
        jo.put("_id", username);
        return jo.toString();
    }
}
