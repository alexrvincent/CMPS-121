package ucsc.cmps121.weatherapp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alex on 1/29/2016.
 */
public class ObservationLocation {

    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("full")
    @Expose
    public String full;
    @SerializedName("elevation")
    @Expose
    public String elevation;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("country_iso3166")
    @Expose
    public String countryIso3166;
    @SerializedName("latitude")
    @Expose
    public String latitude;

    public String getLatitude() { return latitude; }

    public String getCity() { return city; }

    public String getFull() { return full; }

    public String getElevation() { return elevation; }

    public String getCountry() { return country; }

    public String getLongitude() { return longitude; }

    public String getState() { return state; }

    public String getCountryIso3166() { return countryIso3166; }

}
