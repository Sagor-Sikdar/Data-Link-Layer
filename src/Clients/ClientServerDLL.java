package Clients;

import SharedFiles.DLLhelper;

public class ClientServerDLL extends DLLhelper{
    int sequenceNo;
    int acknowledgementNo;
    byte[] payload;

    public ClientServerDLL(int sequenceNo, int acknowledgementNo, byte[] payload) {
        this.sequenceNo = sequenceNo;
        this.acknowledgementNo = acknowledgementNo;
        this.payload = payload;
    }
    public ClientServerDLL(){
    }

    public  String stuffedData(String string,String ans){
        String str=new String("");
        int position=string.indexOf("11111");

        if(position==-1){
            return ans+string;
        }
        else if (position+5==string.length()){
            return ans+string+"0";
        }
        str=ans+string.substring(0,position+5)+"0";
        String nextstring=string.substring(position+5);
        return stuffedData(nextstring,str);
    }

    public byte[] getData(int sequenceNo,int acknowledgementNo,byte[] payload){
        int i=3,j=0;
        byte[] frame=new byte[payload.length+4];

        frame[0]=0x00;
        frame[1]=(byte) sequenceNo;
        frame[2]=(byte)acknowledgementNo;

        for (byte b : payload){
            frame[i++]=b;
        }
        frame[i]=calculateChecksum(frame);
        return frame;
    }

    public byte[] getFrame(byte[] data){
        byte[] frame=new byte[data.length+2];
        int i=1;
        frame[0]=(byte)126;

        for (byte b : data){
            frame[i++]=b;
        }
        frame[i++]=(byte)126;

        return frame;
    }

    public byte[] stringtoBytearray(String string){
        byte[] bytes;
        if (string.length()%8==0) bytes=new byte[string.length()/8];
        else bytes=new byte[string.length()/8+1];

        int i=0;
        String str=string;

        while (str.length()>8){
            bytes[i++]=getByte(str.substring(0,8));
            str=str.substring(8);
        }

        if (str.length()!=0)bytes[i]=getByte(str);
        return bytes;
    }

    public byte[] introduceError(byte[] data){
        byte[] bytes=data;
        int  temp=data[4] & 0x80;
        if (temp!=0) bytes[4] &= 0x7f;
        else bytes[4] |= 0x80;
        return bytes;
    }
}