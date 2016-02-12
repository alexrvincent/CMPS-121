package ucsc.cmps121.chatapp;

/**
 * Created by Alex on 2/10/2016.
 */
public class MessageElement {

    public String message;
    public String nickname;
    public String user_id;
    public Boolean isMe;

    MessageElement() {};

    MessageElement(String msg, String nn, String id, Boolean isUser){
        message = msg;
        nickname = nn;
        user_id = id;
        isMe = isUser;
    }
}
