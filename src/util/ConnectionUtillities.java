/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uesr
 */
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
        } catch (Exception e){
            System.out.println("connection_gone in write");
            return false;
        }
    }
    
    public Object read(){
        try {
            Object o=ois.readObject();
            return o;
        }
        catch (Exception e){
            return "con_gone";
        }
    }
}
