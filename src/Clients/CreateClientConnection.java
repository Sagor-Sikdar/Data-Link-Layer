/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clients;

import java.util.ArrayList;
import java.util.HashMap;
import util.ConnectionUtillities;
import SharedFiles.*;
import ServerEnd.*;
/**
 *
 * @author uesr
 */
public class CreateClientConnection implements Runnable{
    private HashMap<String,Information> clientList;
    private ConnectionUtillities connection;
    private String username;
    private HashMap<String,ArrayList<FileData>>receiverInfo;



    public CreateClientConnection(HashMap<String,Information> list, ConnectionUtillities con,String username,HashMap<String,ArrayList<FileData>>receiverInfo){
        clientList=list;
        connection=con;
        this.username=username;
        this.receiverInfo=receiverInfo;
    }
    
    @Override
    public void run() {
       // clientList.put(username, new Information(connection, username));
        new Thread(new ServerReaderWriter(username,connection, clientList,receiverInfo)).start();
        
    }

}
