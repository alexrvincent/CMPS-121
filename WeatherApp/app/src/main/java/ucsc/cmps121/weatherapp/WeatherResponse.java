package ucsc.cmps121.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alex on 1/29/2016.
 */
public class WeatherResponse {

    @SerializedName("response")
    @Expose
    public Response response;


    public Response getResponse(){
        return response;
    }
}
