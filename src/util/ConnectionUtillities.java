package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
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

    public ConnectionUtillities(String host, int port) throws IOException {
        sc=new Socket(host,port);
        oos=new ObjectOutputStream(sc.getOutputStream());
        ois=new ObjectInputStream(sc.getInputStream());
    }
    public ConnectionUtillities(Socket socket) throws IOException {
            sc=socket;
            oos=new ObjectOutputStream(sc.getOutputStream());
            ois=new ObjectInputStream(sc.getInputStream());
    }
    
    public void write(Object o) throws IOException {
        oos.writeObject(o);
        oos.flush();
    }
    
    public Object read() throws IOException,ClassNotFoundException{
        Object o=ois.readObject();
        return o;
    }
}
