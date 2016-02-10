package ucsc.cmps121.chatapp;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 2/9/2016.
 */
public class MessageResponse {

    @SerializedName("result_list")
    @Expose
    public List<ResultList> resultList = new ArrayList<ResultList>();

    @SerializedName("result")
    @Expose
    public String result;

    public List<ResultList> getResultList() {
        return resultList;
    }

    public String getResult() {
        return result;
    }
}
