package ServerEnd;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.*;
import javax.print.attribute.HashAttributeSet;
import util.ConnectionUtillities;
import SharedFiles.*;
import Clients.*;

public class Server {
    
    public ServerSocket servSocket;
    public  HashMap<String,Information> clientList;
    public HashMap<String,ArrayList<FileData>>receiverInfo;
    public HashMap<Integer,FileBundle>docfile;
    public static long capacity;
    public String username;
    public static int id;
    
    public Server(int port){
        
        clientList=new HashMap<String, Information>();
        receiverInfo=new HashMap<String, ArrayList<FileData>>();
        capacity=327680009;
        id=1;
        docfile=new HashMap<Integer, FileBundle>();
        
        try {
            servSocket=new ServerSocket(port);
            
            while(true) {
                Socket clientSocket = servSocket.accept();
                ConnectionUtillities connection = new ConnectionUtillities(clientSocket);


                printHashMap();
                System.out.println("capacity : "+capacity);
                username=readingUsername(connection);//client er deya username porlam
                if (!isExist(username)) {//online e user absent
                    connection.write("not_exists");
                    clientList.put(username,new Information(connection,username));
                    new Thread(new CreateClientConnection(clientList, connection,username,receiverInfo,docfile)).start();
                }
                else {
                    connection.write("exists");//client er thread run korabo kina bujhar jonno
                    clientSocket.close();
                }

            }
            
             
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
    }
    


    public boolean isExist(String string){
        if (clientList.containsKey(string)){
            return true;
        }
        else{
            return false;
        }
    }
    public String readingUsername(ConnectionUtillities connection){
        return connection.read().toString();
    }
    
    
    public static void main(String[] args) {
        new Server(22222);
    }

    public void printHashMap(){

        Set set = clientList.entrySet();

        Iterator i = set.iterator();
        System.out.println("Current User--");
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.println(me.getKey() + " : ");
        }
    }
}
