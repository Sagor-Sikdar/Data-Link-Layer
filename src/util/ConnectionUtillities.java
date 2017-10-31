package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionUtillities {
    public Socket sc;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;
    public String username;



    public void setUsername(String username) {
        this.username = username;
    }

    public ConnectionUtillities(String host, int port){
        try {
            sc=new Socket(host,port);
            oos=new ObjectOutputStream(sc.getOutputStream());
            ois=new ObjectInputStream(sc.getInputStream());
            sc.setSoTimeout(1000);
        } 
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public ConnectionUtillities(Socket socket){
        try {
            sc=socket;
            oos=new ObjectOutputStream(sc.getOutputStream());
            ois=new ObjectInputStream(sc.getInputStream());
        } 
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean write(Object o){
        try {
            oos.writeObject(o);
            oos.flush();
            return true;
        }
        catch (IOException e){
            System.out.println("connection_gone in write");
            return false;
        }
    }
    
    public Object read(){
        try {
            Object o=ois.readObject();
            return o;
        }
        catch (SocketTimeoutException e){
            return "timeout";
        }

        catch (IOException e){
            return "con_gone";
        }
        catch (ClassNotFoundException e){
            return "con_gone";
        }


    }
}
