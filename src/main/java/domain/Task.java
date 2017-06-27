package domain;

import config.ServerConfig;
import receiveFromServer.OriginMessage;
import sendToServer.cmd.BaseCmd;
import sendToServer.msg.Message;
import utils.MessageUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Fresher on 22/06/2017.
 */
public class Task {
    int idTask;
    private TaskStatus status;
    private BaseCmd cmd;
    private OriginMessage dataMessageFromServer;


    public Task(BaseCmd cmd) {
        this.status = TaskStatus.JUST_CREATED;
        this.cmd = cmd;
        this.idTask = cmd.getTypeId();
    }

    public OriginMessage executeTask(User u) {
        //todo: send cmd to server, submit task to map, and waiting
        u.getMapTask().put(this.idTask, this);
        sendCmdToServer(u.getSocketChannel());
        long timeStartExecute = System.currentTimeMillis();
        while (true) {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - timeStartExecute;
            //System.out.println("Time elapsed "+ timeElapsed);
            System.out.println("task status "+ getStatus());
            if (getStatus() == TaskStatus.RECEIVED || timeElapsed >= ServerConfig.TIME_OUT_EXECUTE_TASK) {
                if(this.dataMessageFromServer==null){
                    System.out.println("time out and not receive data from server");
                }
                break;
            }
        }
        return this.dataMessageFromServer;

    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setDataMessageFromServer(OriginMessage dataMessageFromServer) {
        this.dataMessageFromServer = dataMessageFromServer;
    }

    private void sendCmdToServer(SocketChannel socketChannel){
        try {
            Message message = MessageUtils.convertBaseCmdToMessage(cmd);
            ByteBuffer buffer = MessageUtils.convertMessageToBuffer(message);
            socketChannel.write(buffer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
