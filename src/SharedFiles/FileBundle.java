package SharedFiles;

import java.io.Serializable;

public class FileBundle implements Serializable{
    public String pathname;
    public String filename;
    public long filesize;

    public FileBundle(String pathname, String filename, long filesize) {
        this.pathname = pathname;
        this.filename = filename;
        this.filesize = filesize;
    }



}
