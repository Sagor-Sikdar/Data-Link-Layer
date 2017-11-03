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
    public  void formatterdStringPrint(byte[] data){
        int i=0;

        for (byte b:data){
            System.out.print(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
            System.out.print("  ");

            i++;
            if(i==15){
                System.out.println();
                i=0;
            }
        }
        System.out.println();
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

    public void printBytes(byte[] data){
        int i=0;


        for (byte b:data){
            System.out.print(b + "  ");

            i++;
            if (i%40==0) {
                System.out.println();
                i = 0;
            }
        }
        System.out.print(calculateChecksum(data));
        System.out.println();
    }

    public byte[] datawithChecksum(byte[] data){
        byte[] result=new byte[data.length+1];
        int i=0;

        for (byte b : data){
            result[i++]=b;
        }
        result[i]=calculateChecksum(data);
        return result;
    }

}
