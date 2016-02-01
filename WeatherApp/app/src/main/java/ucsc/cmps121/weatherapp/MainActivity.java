package ucsc.cmps121.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private String result;
    private WeatherService service;
    private TextView location;
    private TextView weather;
    private TextView temp;
    private TextView humidity;
    private TextView ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Link UI Elements */
        location = (TextView) findViewById(R.id.location_content);
        weather = (TextView) findViewById(R.id.weather_content);
        temp = (TextView) findViewById(R.id.temp_content);
        humidity = (TextView) findViewById(R.id.humidity_content);
        ws = (TextView) findViewById(R.id.ws_content);

        /* Request Debug Logging */
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); //Set Desired Log Level
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();


        /* Retrofit Initialization */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://luca-teaching.appspot.com/weather/default/get_weather/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        /* Create a service that implements WeatherService API */
       service = retrofit.create(WeatherService.class);

    }

    /* OnClick for Button, makes a GET call and updates corresponding TextViews */
    public void fetchWeather(View v){
        /* Make the GET call */
        Call<WeatherResponse> queryWeatherFetch =
                service.weatherFetch(result);

        /* Enqueue the call on a callback, handle responses */
        queryWeatherFetch.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(retrofit2.Response<WeatherResponse> response) {

                Log.i("WeatherAppLog", "Code: " + response.code());
                // Case: Unknown Server Error Code 500
                if(response.body() == null || (response.code() != 200)){
                    Toast.makeText(MainActivity.this, "Server Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Server request returns code 200, but with error field
                else if(response.body().getResponse().getResult().equals("error")){
                    Toast.makeText(MainActivity.this, "Application Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Successful call and fetch of request
                else if (response.body().getResponse().getResult().equals("ok")){
                    location.setText(response.body().getResponse().getConditions().getObservationLocation().getCity() +
                            "\n" + "Elevation: " + response.body().getResponse().getConditions().getObservationLocation().getElevation());
                    weather.setText(response.body().getResponse().getConditions().getWeather());
                    temp.setText(response.body().getResponse().getConditions().getTempF().toString());
                    humidity.setText(response.body().getResponse().getConditions().getRelativeHumidity());
                    ws.setText("Average: " + response.body().getResponse().getConditions().getWindMph()+
                                "\nGusts: " + response.body().getResponse().getConditions().getWindGustMph());
                    //Log.i("WeatherAppLog", "Code: " + response.code());
                    Log.i("WeatherAppLog", "The result is: " + response.body().response.getResult());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("WeatherAppLog", "Query Failed");
            }
        });


    }


    /* Custom API Service Using Callback */
    public interface WeatherService {
        @GET("default/weather_app")
        Call<WeatherResponse> weatherFetch(@Query("result") String value);
    }
}
