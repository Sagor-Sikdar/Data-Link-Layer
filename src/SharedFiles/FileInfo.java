package SharedFiles;

import java.io.File;
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

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
