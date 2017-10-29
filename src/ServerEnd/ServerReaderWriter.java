/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerEnd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import javafx.beans.binding.ObjectExpression;
import util.ConnectionUtillities;
import SharedFiles.*;
import Clients.*;

/**
 *
 * @author uesr
 */
public class ServerReaderWriter implements Runnable{

    public HashMap<String,Information> clientList;
    public ConnectionUtillities connection;
    public String username;
    public HashMap<String,ArrayList<FileData>>receiverInfo;
    public HashMap<Integer,FileBundle>docfile;

    
    public ServerReaderWriter(String username,ConnectionUtillities con, HashMap<String,Information> list,HashMap<String,ArrayList<FileData>>receiverInfo,HashMap<Integer,FileBundle>docfile){
        connection=con;
        clientList=list;
        this.username=username;
        this.receiverInfo=receiverInfo;
        this.docfile=docfile;

    }
    
    @Override
    public void run() {


        while (true) {

            //client ki  read korte chay naki write korte chay
            Object o = connection.read();
            if (isConnected(o)) {
                String string = o.toString();
                if (string.equals("sender")) {

                    o = connection.read();//kak pathabo?
                    String receiverId = o.toString();

                    if (clientList.containsKey(receiverId)) {//recipient online e ase
                        connection.write("yes");
                        System.out.println("recipient is in online");

                        //1ta sender er theke full file portesi :3

                        Object object = connection.read();//FileBundle with filename and size

                        if (isConnected(object)) {
                            FileBundle fileBundle = (FileBundle) object;//filename ar filesize read korlam

                            FileInfo fileInfo = getFileInfo(fileBundle);//fileId,filechunk bole dilo

                            //validity checking
                            if (fileInfo == null) {
                                System.out.println("capacity shortage");
                                connection.write(null);
                                continue;
                            }
                            docfile.put(fileInfo.fileId,fileBundle);
                            connection.write(fileInfo);//chunksize,fileid dilam

                            int fileId = fileInfo.fileId;

                            if (receiverInfo.get(receiverId)==null){
                                receiverInfo.put(receiverId,new ArrayList<FileData>());
                            }
                            FileData fileData = new FileData(fileId, new ArrayList<byte[]>());
                            receiverInfo.get(receiverId).add(fileData);

                            int fileSize = readingFromClients(fileBundle.getFilesize(), fileData);

                            if (fileSize != -1) {
                                if (fileSize==0){//timeout
                                    addCapacity(docfile.get(fileData.fileId).getFilesize());
                                    receiverInfo.get(receiverId).remove(fileData);
                                    continue;
                                }
                                string = connection.read().toString();//interrupted hoile dhora khabo ekhane
                                if (string.equals("successful")) {
                                    if (successfulUpload(fileBundle, fileSize)) {
                                        System.out.println("successfully uploaded");

                                    } else {
                                        System.out.println("unsuccessful");
                                        receiverInfo.get(receiverId).remove(fileData);
                                        addCapacity(fileBundle.getFilesize());
                                    }
                                }
                            }

                            else {
                                clientList.remove(username);
                                receiverInfo.get(receiverId).remove(fileData);
                                addCapacity(fileBundle.getFilesize());
                                break;
                            }
                            continue;
                        }
                        else if (!isConnected(object)) {
                            clientList.remove(username);
                            break;
                        }

                    }
                    else {
                        connection.write("no");
                        System.out.println("recipient is not logged in");
                        continue;
                    }
                }
                else if (string.equals("receiver")){

                    if (receiverInfo.get(username)==null){
                        receiverInfo.put(username,new ArrayList<FileData>());
                    }

                     //client file ta porte chay kina
                    Object object=connection.read();
                    if (!isConnected(object)){
                        clientList.remove(username);
                        break;
                    }


                    if (object.toString().equals("no")) {
                        continue;
                    }

                    else if (object.toString().equals("yes")) {
                        int size = receiverInfo.get(username).size();
                        System.out.println("size :" + size);
                        if (size == 0) {
                            System.out.println("nothing to be received");
                            connection.write(null);
                            continue;
                        }
                        FileData file = receiverInfo.get(username).get(0);
                        int fileId=file.fileId;
                        String name=docfile.get(fileId).getFilename();
                        long filesize=docfile.get(fileId).getFilesize();
                        FileSample fileSample=new FileSample(name,file);
                        connection.write(fileSample);
                        receiverInfo.get(username).remove(0);
                        addCapacity(filesize);

                    }
                }
            }
            else {
                clientList.remove(username);
                break;
            }
        }

    }

    public boolean successfulUpload(FileBundle fileBundle,int filesize){
        if (fileBundle.getFilesize()==filesize){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isConnected(Object o){
        if (o.equals(null) || o.equals("con_gone")){
            return false;
        }
        return true;
    }

    public FileInfo getFileInfo(FileBundle fileBundle){
        if (Server.capacity<fileBundle.getFilesize()){
            return null;
        }
        else{
            return new FileInfo(Server.id++,fileBundle.getPathname(),randomNumber());
        }
    }

    public int readingFromClients(long size,FileData fileData){

        FileChunk fileChunk=new FileChunk();
        int i=0;

        decreaseCapacity(size);

        int p=0;

        while(true){
            try {
                Object o=connection.read();
                if (!isConnected(o)){
                    clientList.remove(username);
                    return -1;
                }

                else if(o.equals("size=0")){
                    fileChunk=null;
                }

                else if (o.equals("timeout")){
                    return 0;
                }

                else  {
                    fileChunk = (FileChunk) o;
                }

                if (isReceived(fileChunk, fileData)) {
                    connection.write("yes");
                }

                else{
                    return totalFilesize(fileData);
                }

            }
            catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }

    public boolean isReceived(FileChunk fileChunk,FileData fileData){


        if (fileChunk != null) {
            fileData.addChunks(fileChunk);
            return true;
        }
        return false;
    }

    public int totalFilesize(FileData fileData){
        int p=0;
        for(int m=0;m<fileData.size();m++){
            p+=fileData.check(m);
        }
        return p;
    }

    public void decreaseCapacity(long size){
        Server.capacity-=size;
    }


    public void addCapacity(long size){
        Server.capacity+=size;
    }

    public int randomNumber(){
        Random random=new Random();

        return random.nextInt(1024*20)+1024;
    }



}
