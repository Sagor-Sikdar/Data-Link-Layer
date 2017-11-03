package Clients;

import java.io.IOException;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import util.ConnectionUtillities;


public class Client {


    
    public static void main(String[] args) throws ClassNotFoundException {
        try {

            ConnectionUtillities connection = new ConnectionUtillities("127.0.0.1", 22222);

            System.out.println("Enter your username : ");

            Scanner in = new Scanner(System.in);
            String username = in.nextLine();
            connection.write(username);    //username server k pathacchi

            if (connection.read().toString().equals("not_exists")) {
                connection.setUsername(username);
                new Thread(new Writer(connection)).start();
            } else {
                System.out.println("previous connection");
                connection.sc.close();
                return;
            }
        }

        catch (SocketException E){
            System.out.println("No server Found");
            return;
        }

        catch (IOException E){
            System.out.println("No connection Found");
            return;
        }
        while(true);
    }


}
