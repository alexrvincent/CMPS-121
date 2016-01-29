package ucsc.cmps121.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Logging */
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
        WeatherService service = retrofit.create(WeatherService.class);

        Call<WeatherResponse> queryWeatherFetch =
                service.weatherFetch(result);

        queryWeatherFetch.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(retrofit2.Response<WeatherResponse> response) {
                Log.i("WeatherAppLog", "Code: " + response.code());
                Log.i("WeatherAppLog", "The result is: " + response.body().response);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("WeatherAppLog", "Query Failed");
            }
        });

    }

    public void fetchWeather(View v){

    }

    /* Custom API Service Using Callback */
    public interface WeatherService {
        @GET("default/weather_app")
        Call<WeatherResponse> weatherFetch(@Query("result") String value);
    }
}
