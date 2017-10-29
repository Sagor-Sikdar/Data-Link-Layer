package SharedFiles;

import java.io.Serializable;

public class FileChunk implements Serializable{
    public int fileId;
    public byte[] chunk;


    public FileChunk(int fileId, byte[] chunk) {
        this.fileId = fileId;
        this.chunk = chunk;
     //   System.out.println(chunk.length+" "+chunk.toString());

    }

    public FileChunk() {
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public byte[] getChunk() {
        return chunk;
    }

    public void setChunk(byte[] chunk) {
        this.chunk = chunk;
    }
}
