
package Clients;

import SharedFiles.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.*;


import util.ConnectionUtillities;


public class Writer extends DLLhelper implements Runnable{
    public ConnectionUtillities connection;
    FileInputStream fileInputStream;

    
    public Writer(ConnectionUtillities con){
        connection=con;
    }

    @Override
    public void run() {
        
        Scanner in=new Scanner(System.in);
        String condition="null";
        int flag=1;


        while(true){
            if (condition.equals("null")) System.out.println("sender / receiver ? : ");
            condition=in.nextLine();
            //server k boltesi ami ekhn sender naki ami ekhn receiver
            if (condition.equals("r") ) connection.write("receiver");
            else if (condition.equals("s")) connection.write("sender");
            else {
                condition="null";
                System.out.println("Wrong key pressed");
                continue;
            }

            if (condition.equals("s")){
                String username=getReceiver(in);
                connection.write(username);//recipient k server er kase pathailam


                Object o=connection.read();
                if (o.toString().equals("yes")) {
                    String pathname = "C:\\Users\\User\\Desktop\\Server\\";
                    System.out.println("Choose Filename : ");
                    Scanner scanner=new Scanner(System.in);

                    String filenameInput=scanner.nextLine();
                    String path=pathname+filenameInput;

                    File file = new File(path);

                    connection.write(new FileBundle(path, file.getName(), file.length()));//filename ar size pathalam

                    Object o1=connection.read();

                    if (o1==null) {
                        System.out.println("overflows capacity");
                        continue;
                    }
                    FileInfo fileInfo = (FileInfo) o1;//chunksize ar fileID koto hobe eta pathailo
                    int fileId = fileInfo.fileId;

                    try {
                        fileDescriptor(fileInfo);//full file chunk by chunk porlam ei function e
                        condition="null";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("offline");
                    condition="null";
                    continue;
                }
            }

            else if(condition.equals("r")){
                Scanner scanner=new Scanner(System.in);
                String directory="C:\\Users\\User\\Desktop\\Client\\";

                System.out.println("Do you want to receive a file");
                String input = scanner.nextLine();
               // if (connection.read().toString().equals("done")) break;

                if (input.equals("y")) {
                    connection.write("yes");
                    Object o=connection.read();
                    if (o==null){
                        System.out.println("nothing to receive");
                        condition="null";
                        continue;
                    }
                    else {
                        FileSample fileSample=(FileSample) o;
                        String name=fileSample.fileName;
                        FileData fileData=fileSample.fileData;
                        String path=directory+name;
                        File file=new File(path);

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            addbytes(fileOutputStream, fileData);
                            System.out.println("Received");
                        }
                        catch (Exception e){
                            System.out.println("Exception");
                        }

                    }

                } else {
                    connection.write("no");
                    continue;
                }
                condition = "null";

            }
        }
    }



    public String getReceiver(Scanner in){
        System.out.println("To whom do you want to send the files : ");
        String string=in.nextLine();
        return string;
    }

    public boolean isConnected(Object o){
        if (o.equals("con_gone") || o.equals(null)){
            return false;
        }
        return true;
    }

    public void fileDescriptor(FileInfo fileInfo) throws Exception{
        File file=new File(fileInfo.filepath);
        long size=file.length();
        int length;
        if (size>0) length=fileInfo.maxSize%(int)size;
        else length=1;

        byte[] chunk=new byte[length];

        int fileId=fileInfo.fileId;
        fileInputStream=new FileInputStream(file);
        ClientServerDLL clientServerDLL;

        int chunklen=0,i=0;
        while ((chunklen = fileInputStream.read(chunk)) != -1) {

            clientServerDLL=new ClientServerDLL(0,0,chunk);
            byte[] result=(clientServerDLL.getData(2,4,chunk));
            String t=clientServerDLL.stuffedData(clientServerDLL.convertTotalData(result),"");
            FileChunk fileChunk = new FileChunk(fileInfo.fileId, clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t)));
            connection.write(fileChunk);//file er chunk portesi


            size-=chunklen;
            if (size==0){
                connection.write("size=0");break;
            }
            else if (size<chunklen) chunklen=(int)size;
            chunk = new byte[chunklen];


            String acknowledgement = (String) connection.read();
            if (acknowledgement.equals("yes")) {
                System.out.println((++i) + "th chunk received");
                continue;
            }

            else if (acknowledgement.equals("timeout")){
                connection.write("timeout");
                System.out.println("Timeout Found");
                continue;
            }

            else {
                System.out.println("Connection Ended");
                break;
            }

        }

        String acknowledgement = (String) connection.read();//acknowledgement for the last chunk
        if (acknowledgement.equals("yes")) {
            connection.write("successful");
            System.out.println((++i) + "th chunk received");
        }
    }

    public void addbytes(FileOutputStream fileOutputStream,FileData fileData) throws Exception {
        int loop=0;
        for (int i=0;i<fileData.size();i++){
            try {
                fileOutputStream.write(fileData.chunks.get(i));
                fileOutputStream.flush();
            }
            catch (Exception e){
                System.out.println("Addbytes e dhora khaisi in serverReaderWriter");
            }
        }
        fileOutputStream.close();
    }
}
