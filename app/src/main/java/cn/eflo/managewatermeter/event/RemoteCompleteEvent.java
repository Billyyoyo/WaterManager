package cn.eflo.managewatermeter.event;

public class RemoteCompleteEvent {
    public final static int UPLOAD = 0;
    public final static int DOWNLOAD = 1;

    public int action;

    public RemoteCompleteEvent(int act) {
        action = act;
    }
}
