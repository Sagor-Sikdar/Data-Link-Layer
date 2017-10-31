package ServerEnd;

import SharedFiles.DLLhelper;

public class ServerClientDLL extends DLLhelper{
    public byte[] frame;

    public ServerClientDLL(byte[] frame) {
        this.frame = frame;
    }


    public int hasChecksumError(byte[] data){
        byte checksum=calculateChecksum(data);
        byte ans=(byte)0x45;

        if (ans==checksum){
            System.out.println("matched");
            return 1;
        }
        else{
            System.out.println("Has checksum Error");
            return 0;
        }
    }

    public byte[] bytessafterDestuffing(byte[] data){
        byte[] temp=headerExclusion(data);
        //System.out.println("removing header : "+);
        String string=destuffedData(convertTotalData(temp),"");
        int size=string.length()/8;
        byte[] result;
        if (size>0) result=new byte[size];
        else return null;
        int i=0,j=0;

        while (string.length()>=8){
            result[i++]=(byte)Integer.parseInt(string.substring(0,8),2);
            j++;
            if(j==size) break;
            string=string.substring(8);
        }
        return result;
    }

    public  byte[] headerExclusion(byte[] data){
        byte[] temp=new byte[data.length-2];
        int i=0,j=0;
        for (byte b:data){
            if (j==0 || j==(data.length-1)){
                j++;
                continue;
            }
            temp[i++]=b;
            j++;
        }
        return temp;
    }


    public  String destuffedData(String string,String ans){
        String str=new String("");
        int position=string.indexOf("11111");

        if(position==-1){
            return ans+string;
        }
        else if (position+6==string.length()){
            return ans+string.substring(0,position+5);
        }

        str=ans+string.substring(0,position+5);
        String nextstring=string.substring(position+6);
        return destuffedData(nextstring,str);
    }

    public byte[] getChunk(byte[] bytes){
        byte[] result=new byte[bytes.length-4];

        for (int i=4;i<bytes.length-1;i++){
            result[i-4]=bytes[i];
        }

        return result;
    }

}
