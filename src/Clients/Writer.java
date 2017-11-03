
package Clients;

import SharedFiles.*;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;
import util.ConnectionUtillities;


public class Writer extends DLLhelper implements Runnable {
    private ConnectionUtillities connection;
    private FileInputStream fileInputStream;


    public Writer(ConnectionUtillities con) throws SocketException {
        connection = con;
        connection.sc.setSoTimeout(45000);
    }

    @Override
    public void run() {

        Scanner in = new Scanner(System.in);
        String condition;

        while (true) {
            try {
                System.out.println("sender / receiver ? : ");
                switch (condition = in.nextLine()) {

                    case "s":
                        connection.write("sender");
                        senderPart();
                        break;


                    case "r":
                        connection.write("receiver");
                        receiverPart();
                        break;


                    default:
                        System.out.println("Wrong Key Pressed");
                        break;
                }
            }



            catch (IOException e){
                System.out.println("Connected Ended");
                return;
            }
            catch (ClassNotFoundException e){
                System.out.println("Class Not found exception");
            }
        }
    }


    private void fileDescriptor(FileInfo fileInfo) throws ClassNotFoundException {

        File file = new File(fileInfo.filepath);
        long size = file.length();
        int chunklen, i = 1, j = 1, next_frame_to_send = 0, ackno = 0;
        Object o;

        byte[] chunk = new byte[(size > 0) ? (fileInfo.maxSize % (int) size) : 1];


        try {

            fileInputStream = new FileInputStream(file);
            ClientServerDLL clientServerDLL = new ClientServerDLL(next_frame_to_send, ackno, chunk);
            byte[] result;
            String t = new String();

            while ((chunklen = fileInputStream.read(chunk)) > 0) {

                System.out.println("Raw Data with Checksum for Frame no : "+i);
                if (Constants.PRINT_BYTE_STRING==1) printBytes(datawithChecksum(chunk));
                else formatterdStringPrint(datawithChecksum(chunk));

                System.out.println("Number of 11111 pattern found : "+getSubstrCount(convertTotalData(datawithChecksum(chunk)),0));

                result = (clientServerDLL.getData(next_frame_to_send, ackno, chunk));
                t = clientServerDLL.stuffedData(clientServerDLL.convertTotalData(result), "");

                if (i==5){
                    connection.write(clientServerDLL.introduceError(clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t))));
                }
                else connection.write(clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t)));//file er chunk portesi

                next_frame_to_send = 1 - next_frame_to_send;

                while (next_frame_to_send != ackno) {

                    try {
                        connection.sc.setSoTimeout(Constants.TIME_OUT_TIME);
                        o = connection.read();

                        byte[] acknowledgement = (byte[]) o;
                        ackno = (int) acknowledgement[3];
                        System.out.println("Frame for Frame no : "+ i);

                        if (Constants.PRINT_BYTE_STRING==1) printBytes(clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t)));
                        else formatterdStringPrint(clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t)));

                        System.out.println("frame no  : " + i + " is acknowledged and ackno is : " + ackno);
                    }

                    catch (SocketTimeoutException e){
                        result = (clientServerDLL.getData(1 - next_frame_to_send, ackno, chunk));
                        t = clientServerDLL.stuffedData(clientServerDLL.convertTotalData(result), "");
                        connection.write(clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t)));//file er chunk portesi
                        System.out.println("Frame(indroduced error) for Frame no : "+ i);

                        if (Constants.PRINT_BYTE_STRING==1) printBytes(clientServerDLL.introduceError(clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t))));
                        else formatterdStringPrint(clientServerDLL.introduceError(clientServerDLL.getFrame(clientServerDLL.stringtoBytearray(t))));

                        System.out.println("didn't get acknowledgement for frame no : " + i);
                    }
                }

                i++;
                size -= chunklen;
                chunk = new byte[chunklen = (size < chunklen) ? (int) size : chunklen];
            }
            connection.write("successful");
            System.out.println("All chunks are successfully received");
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found Exception");
        } catch (IOException e) {
            System.out.println("Input Output Exception is found here");
        }
    }

    private void addbytes(FileOutputStream fileOutputStream, FileData fileData) {

        try {
            for (int i = 0; i < fileData.size(); i++) {
                fileOutputStream.write(fileData.chunks.get(i));
                fileOutputStream.flush();
            }
            fileOutputStream.close();
        }
        catch (IOException e) {
            System.out.println("IOException is found in addbytes function");
        }
    }

    private void senderPart() throws ClassNotFoundException, IOException {

        System.out.println("To whom do you want to send the files : ");
        String username = new Scanner(System.in).nextLine();
        connection.write(username);//recipient k server er kase pathailam

        Object o = connection.read();
        if (o.toString().equals("yes")) {
            System.out.println("Choose Filename : ");
            String filenameInput = new Scanner(System.in).nextLine();
            String path = Constants.PATHNAME_SERVER + filenameInput;
            File file = new File(path);

            connection.write(new FileBundle(path, file.getName(), file.length()));//filename ar size pathalam

            Object o1 = connection.read();

            if (o1 == null) {
                System.out.println("overflows capacity");
                return;
            }
            FileInfo fileInfo = (FileInfo) o1;//chunksize ar fileID koto hobe eta pathailo

            fileDescriptor(fileInfo);//full file chunk by chunk porlam ei function e
            return;

        } else {
            System.out.println("offline");
            return;
        }
    }


    private void receiverPart() throws IOException, ClassNotFoundException {
        Object o=new Object();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to receive a file");
        String input = scanner.nextLine();
        // if (connection.read().toString().equals("done")) break;


        if (input.equals("y")) {

            connection.write("yes");
             o = connection.read();
            if (o == null) System.out.println("nothing to receive");
            else {
                FileSample fileSample = (FileSample) o;
                String name = fileSample.fileName;
                FileData fileData = fileSample.fileData;
                String path = Constants.PATHNAME_CLIENT + name;
                File file = new File(path);

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                addbytes(fileOutputStream, fileData);
                System.out.println("Received");
            }

        }
        else {
            connection.write("no");
        }
    }

}
