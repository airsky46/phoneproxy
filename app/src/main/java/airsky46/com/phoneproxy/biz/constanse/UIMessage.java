package airsky46.com.phoneproxy.biz.constanse;

/**
 * Created by airsky46 on 2017/7/16.
 */

public enum UIMessage {
    DEFAULT(0),
    MSG_INBOX(1),
    PHONE_IN(2);
    private int messageType;

    UIMessage(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageType() {
        return messageType;
    }

    public static UIMessage valueOf(int messageType) {
        for (UIMessage uiMessage : UIMessage.values()) {
            if(uiMessage.getMessageType()==messageType){
                return uiMessage;
            }
        }
        return DEFAULT;
    }
}
