package ucsc.cmps121.chatapp;

/**
 * Created by Alex on 2/10/2016.
 */
public class MessageElement {

    public String message;
    public String nickname;
    public String user_id;

    MessageElement() {};

    MessageElement(String msg, String nn, String id){
        message = msg;
        nickname = nn;
        user_id = id;

    }
}
