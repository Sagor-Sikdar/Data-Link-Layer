package SharedFiles;

import java.io.Serializable;
import java.util.ArrayList;

public class FileData implements Serializable{
    public int fileId;
    public ArrayList<byte[]>chunks;

    public FileData(int fileId, ArrayList<byte[]> chunks) {
        this.fileId = fileId;
        this.chunks = chunks;
    }



    public FileData() {
        chunks=new ArrayList<byte[]>();
    }

    public void addChunks(FileChunk newChunks){
        chunks.add(newChunks.chunk);
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

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public ArrayList<byte[]> getChunks() {
        return chunks;
    }

    public void setChunks(ArrayList<byte[]> chunks) {
        this.chunks = chunks;
    }
}
