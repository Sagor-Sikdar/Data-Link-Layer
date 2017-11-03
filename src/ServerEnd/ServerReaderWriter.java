package ServerEnd;


import java.io.IOException;
import java.util.*;
import util.ConnectionUtillities;
import SharedFiles.*;

public class ServerReaderWriter extends DLLhelper implements Runnable {

    private HashMap<String, Information> clientList;
    private ConnectionUtillities connection;
    private String username;
    private HashMap<String, ArrayList<FileData>> receiverInfo;


    public ServerReaderWriter(String username, ConnectionUtillities con, HashMap<String, Information> list, HashMap<String, ArrayList<FileData>> receiverInfo) {
        connection = con;
        clientList = list;
        this.username = username;
        this.receiverInfo = receiverInfo;
    }

    @Override
    public void run() {


        while (true) {
            try {
                //client ki  read korte chay naki write korte chay
                Object o = connection.read();
                String string = o.toString();
                if (string.equals("sender")) {
                    senderPart();
                }
                else if (string.equals("receiver")) {
                    receiverPart();
                }

            }

            catch (IOException e) {
                clientList.remove(username);
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("Class Not Found");
            }
        }

    }

    private int readingFromClients(long size, FileData fileData) throws IOException, ClassNotFoundException {
        decreaseCapacity(size);
        int ackno = 0;
        int frame_no = 1;

        while (true) {

            Object o = connection.read();

            if (o.getClass().equals(String.class)) {
                System.out.println("finished");
                return fileData.totalSize();
            }

            byte[] temp = (byte[]) o;
            ServerClientDLL serverClientDLL = new ServerClientDLL(temp);


            if (serverClientDLL.hasChecksumError(temp)) {
            }

            else if (temp[3]!=ackno){
                temp[3]=(byte)ackno;
                connection.write(temp);
                System.out.println("I got it previously");
            }
            else {
                System.out.println("Raw Data Found After Excluding Headers and Checksum for frame : "+ frame_no);
                if (Constants.PRINT_BYTE_STRING==1) printBytes(serverClientDLL.bytessafterDestuffing(temp));
                else   formatterdStringPrint(serverClientDLL.bytessafterDestuffing(temp));
                frame_no++;

                fileData.addChunks(serverClientDLL.getChunks(serverClientDLL.bytessafterDestuffing(temp)));
                ackno = 1 - ackno;
                temp[3] = (byte) ackno;
                connection.write(temp);
            }
        }
    }
    private void senderPart() throws IOException, ClassNotFoundException {

        Object o;

        o = connection.read();//kak pathabo?
        String receiverId = o.toString();

        if (clientList.containsKey(receiverId)) {//recipient online e ase
            connection.write("yes");
            System.out.println("recipient is in online");

            //1ta sender er theke full file portesi :3
            Object object = connection.read();//FileBundle with filename and size
            FileBundle fileBundle = (FileBundle) object;//filename ar filesize read korlam
            long size= fileBundle.filesize;
            String filename=fileBundle.filename;
            FileInfo fileInfo = getFileInfo(fileBundle);//fileId,filechunk bole dilo

            //validity checking
            if (fileInfo == null) {
                System.out.println("capacity shortage");
                connection.write(null);
                return;
            }
            connection.write(fileInfo);//chunksize,fileid dilam


            receiverInfo.computeIfAbsent(receiverId, k -> new ArrayList<FileData>());//Receive list e receiverID na thake

            FileData fileData = new FileData(filename,new ArrayList<byte[]>());
            receiverInfo.get(receiverId).add(fileData);


            int fileSize = readingFromClients(size, fileData);


            if (successfulUpload(fileBundle, fileSize)) {
                System.out.println("successfully uploaded");
            }
            else {
                System.out.println("unsuccessful");
                receiverInfo.get(receiverId).remove(fileData);
                addCapacity(fileBundle.filesize);
            }
        }
        else {
            connection.write("no");
            System.out.println("recipient is not logged in");
        }
    }

    private boolean successfulUpload(FileBundle fileBundle,int filesize){
        return fileBundle.filesize==filesize;
    }
    private FileInfo getFileInfo(FileBundle fileBundle){
         return (Server.capacity<fileBundle.filesize)? null : new FileInfo(Server.id++,fileBundle.pathname,randomNumber(fileBundle.filesize));
    }
    private void decreaseCapacity(long size){
        Server.capacity-=size;
    }
    private void addCapacity(long size){
        Server.capacity+=size;
    }
    private int randomNumber(long filesize){
        return (filesize>Constants.NO_of_FILE)  ? (int) (filesize/Constants.NO_of_FILE) : 1;
    }


    private void receiverPart() throws IOException, ClassNotFoundException {

        receiverInfo.computeIfAbsent(username, k -> new ArrayList<FileData>());


        //client file ta porte chay kina
        Object object = connection.read();

        if (object.toString().equals("yes")) {
            int size = receiverInfo.get(username).size();

            if (size == 0) {
                System.out.println("nothing to be received");
                connection.write(null);
                return;
            }


            FileData file = receiverInfo.get(username).get(0);
            long filesize=file.totalSize();


            FileSample fileSample = new FileSample(file.filename, file);
            receiverInfo.get(username).remove(0);

            connection.write(fileSample);
            addCapacity(filesize);
        }
    }

}


