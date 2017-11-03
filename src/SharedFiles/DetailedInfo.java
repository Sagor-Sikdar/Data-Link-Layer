package SharedFiles;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailedInfo implements Serializable{
    public ArrayList<SenderInfo> senderInfos;

    public DetailedInfo(ArrayList<SenderInfo> senderInfos) {
        this.senderInfos = senderInfos;
    }
}
