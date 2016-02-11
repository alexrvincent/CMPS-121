package ucsc.cmps121.chatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    public EditText username;
    public Button start_chat;
    public Button my_location;
    public String user_id;
    public String nickname;
    public static String BASE_URL = "https://luca-teaching.appspot.com/localmessages/default/";
    private static final String STRING_LIST =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 8;
    public LocationData locationData = LocationData.getLocationData();
    //public Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        start_chat = (Button) findViewById(R.id.button);

        SharedPreferences sp = getSharedPreferences("appinfo", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();

        /*Get the user id from Shared Preferences*/
        user_id = sp.getString("user_id", null);

        /* If it's not available, make a new one and store it */
        if (user_id == null) {
            user_id = generateId();
            editor.putString("user_id", user_id);
            Log.i("MainActivity", "First time load: Made a new user_id!");
            editor.commit();
        }

        /* Get the nickname from Shared Preferences */
        nickname = sp.getString("nickname", null);

        /* If it's not available, set an empty one */
        if(nickname == null) {
            nickname = "";
            editor.putString("nickname", nickname);
            Log.i("MainActivity", "First time load: Made a new nickname!");
            editor.commit();
        }

        /* Set the last used nickname (or first used) to the EditText */
        username.setText(nickname);

        /* Continously check for input on the EditText to enable the Chat Button*/
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkForUsername();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkForUsername();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkForUsername();
            }
        });
        checkForUsername();

    }

    @Override
    public void onResume(){
        Boolean locationAllowed = checkLocationAllowed();
        if(locationAllowed){
            requestLocationUpdate();
            Log.i("MainActivity", "Location was allowed and request location update ran");
        }
        else {
            start_chat.setEnabled(false);
            checkForUsername();
            Log.i("MainActivity", "location allowed was false, disable start_chat");
        }
        render();
        super.onResume();
    }

    @Override
    public void onPause(){
        if(checkLocationAllowed())
            removeLocationUpdate();//if the user has allowed location sharing we must disable location updates now
        super.onPause();
    }

    /* Check users location sharing setting */
    private boolean checkLocationAllowed(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("location_allowed", false);
    }

    /* Persist users location sharing setting */
    private void setLocationAllowed(boolean allowed){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("location_allowed", allowed);
        editor.commit();
    }

    /* Set the button text between "Enable Location" or "Disable Location" */
    private void render(){
        Boolean locationAllowed = checkLocationAllowed();
        Button button = (Button) findViewById(R.id.location_button);

        if(locationAllowed) {
            button.setText("Disable Location");
        }
        else {
            button.setText("Enable Location");
        }
    }


    /* Request location update. This must be called in onResume if the user has allowed location sharing */
    private void requestLocationUpdate(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Log.i("MainActivity", "requesting location update");
            }
        }
    }

    /* Remove location update. This must be called in onPause if the user has allowed location sharing */
    private void removeLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.removeUpdates(locationListener);
                Log.i("MainActivity", "removing location update");
            }
        }
    }

    public void toggleLocation(View v) {

        Boolean locationAllowed = checkLocationAllowed();
        Log.i("MainActivity", "Toggled Location");


        if(locationAllowed){
            //disable it
            removeLocationUpdate();
            setLocationAllowed(false);//persist this setting
            checkForUsername();

        } else {
            //enable it
            requestLocationUpdate();
            setLocationAllowed(true);//persist this setting
            checkForUsername();
        }
        render();
    }

    public void checkForUsername(){
        if(!(username.getText().toString().equals("")) && checkLocationAllowed() != false ) {
            start_chat.setEnabled(true);
        }
        else start_chat.setEnabled(false);

    }

    public void startChat(View v) {

        /* Check to make sure we have the location */
        if(locationData.getLocation() == null) {
            Toast.makeText(MainActivity.this, "Please enable location permissions in this device's settings", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ChatActivity.class);

        /* Store the current nickname in SharedPrefences */
        SharedPreferences sp = getSharedPreferences("appinfo", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        String newNickname = username.getText().toString();
        editor.putString("nickname", newNickname);
        Log.i("MainActivity", "Put new nickname: "+ newNickname + " into SharedPrefences");
        editor.commit();

        //Put the user_id in the extra
        intent.putExtra("user_id", user_id);

        //Grab username and put it in an extra
        intent.putExtra("USERNAME", username.getText().toString());
        startActivity(intent);
    }

    private String generateId(){
        StringBuffer randomString = new StringBuffer();
        for(int i =0; i<RANDOM_STRING_LENGTH; ++i){
            int number = getRandomNumber();
            char ch = STRING_LIST.charAt(number);
            randomString.append(ch);
        }
        return randomString.toString();
    }

    private int getRandomNumber(){
        int randomInt;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(STRING_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }


    /* Listens to the location, and gets the most precise recent location */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Location lastLocation = locationData.getLocation();

            // Do something with the location you receive.
            double newAccuracy = location.getAccuracy();

            long newTime = location.getTime();
            // Is this better than what we had?  We allow a bit of degradation in time.
            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (isBetter) {
                // We replace the old estimate by this one.
                locationData.setLocation(location);

                //Now we have the location.
                if(checkLocationAllowed())
                    start_chat.setEnabled(true);//We must enable search button
                    checkForUsername();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };
}
