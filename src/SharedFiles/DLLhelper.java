package SharedFiles;

public class DLLhelper {

    public byte calculateChecksum(byte[] data){
        byte checksum=0x00;

        for (byte b:data){
            checksum^=b;
        }
        return checksum;
    }

    //byte array k string e nay
    public String convertTotalData(byte[] data){
        String str=new String();
        for (byte bytes:data){
            str+=String.format("%8s", Integer.toBinaryString(bytes & 0xFF)).replace(' ', '0');
        }
        return str;
    }

    //formatted kore string print kore
    public  void formatterdStringPrint(String string){
        if(string.length()<9) {
            System.out.println(string);
            return;
        }
        System.out.print(string.substring(0,8)+" ");

        formatterdStringPrint(string.substring(8));
    }

    public int getSubstrCount(String str,int count){
        int position=str.indexOf("11111");
        if (position==-1){
            return count;
        }
        else if (position+5==str.length()){
            return count+1;
        }
        count++;
        return  getSubstrCount(str.substring(position+5),count);
    }

    public byte getByte(String str){
        byte result=0x00;
        for (int i=0;i<str.length();i++){
            if (str.charAt(i)=='1') result |=(1<<(7-i));
        }
        return result;
    }

}
