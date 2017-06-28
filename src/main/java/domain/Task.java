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
    private volatile TaskStatus status;
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

            //System.out.println("Task "+ idTask+"status is "+ getStatus());

            if (getStatus() == TaskStatus.RECEIVED || timeElapsed >= ServerConfig.TIME_OUT_EXECUTE_TASK || dataMessageFromServer != null) {

                if (this.dataMessageFromServer == null) {
                    System.out.println("time out and not receive data from server");
                }
                System.out.println("task " + idTask + " elapses " + timeElapsed + "ms");

                break;
            }
        }
        return this.dataMessageFromServer;

    }

    public void setStatus(TaskStatus status) {

        if (status == TaskStatus.RECEIVED) {
            System.out.println("task" + idTask + "set status to received");
        }

        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setDataMessageFromServer(OriginMessage dataMessageFromServer) {
        this.dataMessageFromServer = dataMessageFromServer;
    }

    private void sendCmdToServer(SocketChannel socketChannel) {
        try {
            Message message = MessageUtils.convertBaseCmdToMessage(cmd);
            ByteBuffer buffer = MessageUtils.convertMessageToBuffer(message);
            socketChannel.write(buffer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
