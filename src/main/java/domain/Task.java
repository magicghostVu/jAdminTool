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
    Integer idTask;
    TaskStatus status;
    BaseCmd cmd;
    OriginMessage dataMessageFromServer;


    public Task(BaseCmd cmd) {
        this.status = TaskStatus.JUST_CREATED;
        this.cmd = cmd;
        this.idTask = cmd.getTypeId();
    }

    public OriginMessage executeTask(User u) {
        //todo: send cmd to server, submit task mo map, and waiting
        u.getMapTask().put(this.idTask, this);
        sendCmdToServer(u.getSocketChannel());
        long timeStartExecute = System.currentTimeMillis();
        while (true) {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - timeStartExecute;
            if (status == TaskStatus.RECEIVED || timeElapsed >= ServerConfig.TIME_OUT_EXECUTE_TASK) {
                if(this.dataMessageFromServer==null){
                    System.out.println("time out and not receive data from server");
                }
                return this.dataMessageFromServer;
            }
        }
    }

    private void setStatus(TaskStatus status) {
        this.status = status;
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
