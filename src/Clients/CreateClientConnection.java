
package Clients;

import java.util.ArrayList;
import java.util.HashMap;
import util.ConnectionUtillities;
import SharedFiles.*;
import ServerEnd.*;

public class CreateClientConnection implements Runnable{
    private HashMap<String,Information> clientList;
    private ConnectionUtillities connection;
    private String username;
    private HashMap<String,ArrayList<FileData>>receiverInfo;
    private HashMap<String,ArrayList<SenderInfo>>senderDetails;



    public CreateClientConnection(HashMap<String,Information> list, ConnectionUtillities con,String username,HashMap<String,ArrayList<FileData>>receiverInfo,HashMap<String,ArrayList<SenderInfo>>senderDetails){
        clientList=list;
        connection=con;
        this.username=username;
        this.receiverInfo=receiverInfo;
        this.senderDetails=senderDetails;
    }
    
    @Override
    public void run() {
       // clientList.put(username, new Information(connection, username));
        new Thread(new ServerReaderWriter(username,connection, clientList,receiverInfo,senderDetails)).start();
        
    }

}
