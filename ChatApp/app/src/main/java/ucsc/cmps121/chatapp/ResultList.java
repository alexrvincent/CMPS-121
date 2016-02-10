package ucsc.cmps121.chatapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 2/9/2016.
 */
public class ResultList {

    @SerializedName("timestamp")
    @Expose
    public String timestamp;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("nickname")
    @Expose
    public String nickname;

    @SerializedName("message_id")
    @Expose
    public String message_id;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMessageId() {
        return message_id;
    }

    public String getUserId() {
        return user_id;
    }

}
