package SharedFiles;

import java.io.Serializable;

public class SenderInfo implements Serializable{

    public String sender;
    public FileBundle fileBundle;
    public FileInfo fileInfo;

    public SenderInfo(String sender, FileBundle fileBundle, FileInfo fileInfo) {
        this.sender = sender;
        this.fileBundle = fileBundle;
        this.fileInfo = fileInfo;
    }
}

