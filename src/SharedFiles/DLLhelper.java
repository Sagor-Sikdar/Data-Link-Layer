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

}
