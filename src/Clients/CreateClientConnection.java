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
    public HashMap<String,Information> clientList;
    public ConnectionUtillities connection;
    public String username;
    public HashMap<String,ArrayList<FileData>>receiverInfo;
    public HashMap<Integer,FileBundle>docfile;


    public CreateClientConnection(HashMap<String,Information> list, ConnectionUtillities con,String username,HashMap<String,ArrayList<FileData>>receiverInfo,HashMap<Integer,FileBundle>docfile){
        clientList=list;
        connection=con;
        this.username=username;
        this.receiverInfo=receiverInfo;
        this.docfile=docfile;

    }
    
    @Override
    public void run() {
       // clientList.put(username, new Information(connection, username));
        new Thread(new ServerReaderWriter(username,connection, clientList,receiverInfo,docfile)).start();
        
    }
    
    
    
}
