package SharedFiles;

import java.io.Serializable;

public class FileBundle implements Serializable{
    String pathname;
    String filename;
    long filesize;

    public FileBundle(String pathname, String filename, long filesize) {
        this.pathname = pathname;
        this.filename = filename;
        this.filesize = filesize;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }
}
