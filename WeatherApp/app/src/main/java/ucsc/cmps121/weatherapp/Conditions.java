package ucsc.cmps121.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alex on 1/29/2016.
 */
public class Conditions {

    @SerializedName("wind_gust_mph")
    @Expose
    public Integer windGustMph;
    @SerializedName("temp_f")
    @Expose
    public Double tempF;
    @SerializedName("observation_location")
    @Expose
    public ObservationLocation observationLocation;
    @SerializedName("temp_c")
    @Expose
    public Double tempC;
    @SerializedName("relative_humidity")
    @Expose
    public String relativeHumidity;
    @SerializedName("weather")
    @Expose
    public String weather;
    @SerializedName("dewpoint_c")
    @Expose
    public Integer dewpointC;
    @SerializedName("windchill_c")
    @Expose
    public String windchillC;
    @SerializedName("pressure_mb")
    @Expose
    public String pressureMb;
    @SerializedName("windchill_f")
    @Expose
    public String windchillF;
    @SerializedName("dewpoint_f")
    @Expose
    public Integer dewpointF;
    @SerializedName("wind_mph")
    @Expose
    public Double windMph;

    public Integer getWindGustMph() { return windGustMph;}

    public Double getTempF() { return tempF; }

    public ObservationLocation getObservationLocation() { return observationLocation; }

    public Double getTempC() { return tempC; }

    public String getRelativeHumidity() { return relativeHumidity; }

    public String getWeather() { return weather; }

    public Integer getDewpointC() { return dewpointC; }

    public String getWindchillC() { return windchillC; }

    public String getPressureMb() { return pressureMb; }

    public String getWindchillF() { return windchillF; }

    public Integer getDewpointF() { return dewpointF; }

    public Double getWindMph() { return windMph; }
}
