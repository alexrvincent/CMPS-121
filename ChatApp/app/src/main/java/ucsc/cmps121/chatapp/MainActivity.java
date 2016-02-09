package ucsc.cmps121.chatapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    public EditText username;
    public Button start_chat;

    public static String BASE_URL = "https://luca-teaching.appspot.com/localmessages/default/";

    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        start_chat = (Button) findViewById(R.id.button);

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

    public void checkForUsername(){
        if(username.getText().toString().equals("")) start_chat.setEnabled(false);
        else start_chat.setEnabled(true);
    }

    // PRE-CONDITION: HAVE LATITUDE, LONGITUDE, NON-EMPTY STRING
    public void startChat(View v) {

        Intent intent = new Intent(this, ChatActivity.class);

        //Grab Latitude/Longitude and put into extra

        //Grab username and put it in an extra
        intent.putExtra("USERNAME", username.getText().toString());
        startActivity(intent);
    }
}
