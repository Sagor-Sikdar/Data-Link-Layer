package SharedFiles;

import java.io.Serializable;

public class FileSample implements Serializable {
    public String fileName;
    public FileData fileData;

    public FileSample(String fileName, FileData fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }
}
