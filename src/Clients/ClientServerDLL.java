package Clients;

public class ClientServerDLL {
    int sequenceNo;
    int acknowledgementNo;
    byte[] payload;


    public ClientServerDLL(int sequenceNo, int acknowledgementNo, byte[] payload) {
        this.sequenceNo = sequenceNo;
        this.acknowledgementNo = acknowledgementNo;
        this.payload = payload;
    }

    public static byte calculateChecksum(byte[] data){
        byte checksum=0x00;
        int size=data.length;

        for (int i=0;i<8;i++){
            int sum=0,mask=0x80;
            for (int j=0;j<size;j++){

                if((data[j] & mask>>i)>0) sum+=1;
                else sum+=0;
            }
            if (sum%2>0) checksum|=(mask>>i);
        }
        return checksum;
    }

    public static byte[] totalData(byte data[]){
        byte checksum=calculateChecksum(data);
        int size=data.length;
        byte[] bytes=new byte[size+1];

        for (int i=0;i<size;i++){
            bytes[i]=data[i];
        }
        bytes[size]=checksum;
        return bytes;
    }

    public static String convertTotalData(byte data[]){
        byte[] b=totalData(data);
        String str=new String();

        for (byte bytes:b){
            str+=String.format("%8s", Integer.toBinaryString(bytes & 0xFF)).replace(' ', '0');
           // str+=" ";
        }
        int pos=str.indexOf("1010");
        System.out.println(pos);

        return str;

    }


    public static String stuffedData(String string,String ans){

        String str=new String("");
        int position=string.indexOf("11111");


        if(position==-1){
            ans+=string;
            return ans;
        }
        else if (position+5==string.length()){
            ans+=string+"0";
            return ans;
        }


        str=ans+string.substring(0,position+5)+"0";
        String nextstring=string.substring(position+5);
        return stuffedData(nextstring,str);
    }



    public static String destuffedData(String string,String ans){

        String str=new String("");
        int position=string.indexOf("11111");


        if(position==-1){
            ans+=string;
            return ans;
        }
        else if (position+6==string.length()){
            ans+=string;
            return ans;
        }


        str=ans+string.substring(0,position+5);
        String nextstring=string.substring(position+6);
        return destuffedData(nextstring,str);
    }

    public static void formatterdStringPrint(String string){
        if(string.length()<9) {
            System.out.println(string);
            return;
        }
        System.out.print(string.substring(0,8)+" ");

        if (string.length()!=8)formatterdStringPrint(string.substring(8));
    }

    public static int hasChecksumError(byte[] data){
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



    public static void main(String args[]){
        byte[] bytes={(byte)0xff,(byte) 0xff,(byte) 0xff,(byte)0x45,(byte)0xff,(byte) 0xff,(byte) 0xff,(byte)0x00};
        String ans=new String("");

        String str=convertTotalData(bytes);

        formatterdStringPrint(str);

        formatterdStringPrint(stuffedData(str,ans));

        formatterdStringPrint(destuffedData(stuffedData(str,ans),ans));

        hasChecksumError(bytes);
    }
}
