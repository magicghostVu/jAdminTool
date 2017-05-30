package sendToServer.msg;

import utils.ByteArray;

/**
 * Created by CPU10340_LOCAL on 24/05/2017.
 */
public class Message {
    private boolean isEncrypted;
    private int id;
    private int targetController;
    private ByteArray content;

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTargetController() {
        return targetController;
    }

    public void setTargetController(int targetController) {
        this.targetController = targetController;
    }

    public ByteArray getContent() {
        return content;
    }

    public void setContent(ByteArray content) {
        this.content = content;
    }
}
