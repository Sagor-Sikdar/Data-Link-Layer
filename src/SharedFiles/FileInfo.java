package SharedFiles;

import java.io.Serializable;

public class FileInfo implements Serializable{
    public int fileId;
    public String filepath;
    public int maxSize;


    public FileInfo(int fileId, String filepath, int maxSize) {
        this.fileId = fileId;
        this.filepath = filepath;
        this.maxSize = maxSize;
    }
}
