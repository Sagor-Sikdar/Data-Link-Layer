package ServerEnd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.*;
import javax.print.attribute.HashAttributeSet;
import util.ConnectionUtillities;
import SharedFiles.*;
import Clients.*;

public class Server {
    
    private ServerSocket servSocket;
    private HashMap<String,Information> clientList;
    private HashMap<String,ArrayList<FileData>>receiverInfo;
    private HashMap<String,ArrayList<SenderInfo>>senderDetails;
    private String username;

    public static long capacity;
    public static int id;
    
    public Server(int port){
        
        clientList=new HashMap<String, Information>();
        receiverInfo=new HashMap<String, ArrayList<FileData>>();
        capacity=327680009;
        id=1;
        receiverInfo = new HashMap<>();
        senderDetails=new HashMap<>();


        try {
            servSocket=new ServerSocket(port);

            while(true) {
                Socket clientSocket = servSocket.accept();
                ConnectionUtillities connection = new ConnectionUtillities(clientSocket);

                System.out.println("capacity : "+capacity);
                username=readingUsername(connection);//client er deya username porlam
                if (!isExist(username)) {//online e user absent
                    connection.write("not_exists");
                    clientList.put(username,new Information(connection,username));


                    receiverInfo.put(username,new ArrayList<FileData>());
                    senderDetails.put(username,new ArrayList<SenderInfo>());

                    new Thread(new CreateClientConnection(clientList, connection,username,receiverInfo,senderDetails)).start();
                }
                else {
                    connection.write("exists");//client er thread run korabo kina bujhar jonno
                    clientSocket.close();
                }
                printHashMap();
            }
        }
        catch (Exception ex) {
            System.out.println("Error Happened in Server");
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

    public String readingUsername(ConnectionUtillities connection) throws IOException, ClassNotFoundException {
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
