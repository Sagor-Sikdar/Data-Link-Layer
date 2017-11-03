package SharedFiles;

import java.io.Serializable;
import java.util.ArrayList;

public class FileData implements Serializable{
    public String filename;

    public FileData(String filename, ArrayList<byte[]> chunks) {
        this.filename = filename;
        this.chunks = chunks;
    }

    public ArrayList<byte[]>chunks;

    public FileData( ArrayList<byte[]> chunks) {
        this.chunks = chunks;
    }



    public FileData() {
        chunks=new ArrayList<byte[]>();
    }

    public void addChunks(byte[] data){
        chunks.add(data);
    }

    public int size(){
        return chunks.size();
    }

    public int check(int index){
        return chunks.get(index).length;
    }

    public int totalSize(){
        int sum=0;
        for (int i=0;i<chunks.size();i++){
            sum+=check(i);
        }
        return sum;
    }
}
