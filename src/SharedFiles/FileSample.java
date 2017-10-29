package SharedFiles;

import java.io.Serializable;

public class FileSample implements Serializable {
    public String fileName;
    public FileData fileData;

    public FileSample(String fileName, FileData fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }
}
