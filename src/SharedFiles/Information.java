/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedFiles;

import util.ConnectionUtillities;

/**
 *
 * @author uesr
 */
public class Information {
    public ConnectionUtillities connection;
    public String username;
    
    public Information(ConnectionUtillities con,String User){
        username=User;
        connection=con;
    }

    public ConnectionUtillities getConnection() {
        return connection;
    }

    public void setConnection(ConnectionUtillities connection) {
        this.connection = connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
