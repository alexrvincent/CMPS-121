package ucsc.cmps121.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ChatActivity extends AppCompatActivity {

    private LocationData locationData = LocationData.getLocationData();
    private String result;
    public static String BASE_URL = "https://luca-teaching.appspot.com/localmessages/";
    private static final String STRING_LIST =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 8;
    private MessageService service;
    private String caughtUsername;
    private String caughtUserId;
    public TextView chattingAs;
    public EditText chatInput;
    public Button sendButton;
    public Button refreshButton;

    private MessageAdapter aa;
    private ArrayList<MessageElement> aList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* Initialize all UI inputs */
        chattingAs = (TextView) findViewById(R.id.chattingAs);
        chatInput = (EditText) findViewById(R.id.chatInput);
        sendButton = (Button) findViewById(R.id.send_btn);
        refreshButton = (Button) findViewById(R.id.refresh_btn);

        /* Set up the ListView and Adapters */
        aList = new ArrayList<MessageElement>();
        aa = new MessageAdapter(this, R.layout.message_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();

        /* Get the nickname and user_id from Shared Preferences */
        SharedPreferences sp = getSharedPreferences("appinfo", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        caughtUserId = sp.getString("user_id", null);
        caughtUsername = sp.getString("nickname", null);

        /* If the nickname/user_id were not found, get them from the intent */
        Bundle extras = getIntent().getExtras();
        if(caughtUserId == null) {
            if(extras != null) {
                caughtUsername = extras.getString("USERNAME", "NO_USERNAME");
            }
        }
        if(caughtUsername == null) {
            if(extras != null) {
                caughtUserId = extras.getString("user_id","0");
            }
        }

        Log.i("ChatActivity:", "User_id recieved: "+caughtUserId);
        Log.i("ChatActivity:", "Nickname recieved: "+caughtUsername);
        chattingAs.setText("Chatting as " + caughtUsername + " ...");

        /* Request Debug Logging */
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); //Set Desired Log Level
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();


        /* Retrofit Initialization */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        /* Create a service that implements WeatherService API */
        service = retrofit.create(MessageService.class);
    }

    @Override
    protected void onResume(){
        while(locationData.getLocation() == null){
            Log.i("ChatActivity", "locatationData is null");
        }
        retrieveMessage();
        super.onResume();
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

    public void sendMessage(View v) {

        if(locationData.getLocation() == null) {
            Toast.makeText(ChatActivity.this, "Please enable location permissions in this device's settings", Toast.LENGTH_SHORT).show();
            return;
        }
        String message = chatInput.getText().toString();
        chatInput.setText("");
        postMessage(message, generateId());

    }

    public void retrieveMessage() {
        float lat = (float) locationData.getLocation().getLatitude();
        float lng = (float) locationData.getLocation().getLongitude();

        /* Make the GET call */
        Call<MessageResponse> queryMessageFetch = service.messageFetch(lat, lng, caughtUserId);

        /* Enqueue the call on a callback, handle responses */
        queryMessageFetch.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Response<MessageResponse> response) {
                Log.i("MessageAppLog", "Code: " + response.code());
                // Case: Unknown Server Error Code 500
                if (response.body() == null || (response.code() != 200)) {
                    Toast.makeText(ChatActivity.this, "Server Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Server request returns code 200, but with error field
                else if (response.body().getResult().equals("error")) {
                    Toast.makeText(ChatActivity.this, "Application Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Successful call and fetch of request
                else if (response.body().getResult().equals("ok")) {
                    Log.i("WeatherAppLog", "The result is: " + response.body().getResult());
                    List<ResultList> rL = response.body().getResultList();
                    aList.clear();
                    for (int i = rL.size() - 1; i > -1; --i) {
                        //addNewMessageToChat(rL.get(i).getMessage(), rL.get(i).getNickname(), rL.get(i).getUserId());
                        //Log.i("Chat Activity", caughtUserId);
                        //Log.i("Chat Activity", rL.get(i).userId.toString());
                        if (caughtUserId.equals(rL.get(i).getUserId())) {
                            aList.add(new MessageElement(rL.get(i).getMessage(), rL.get(i).getNickname() + " (You)", rL.get(i).getUserId()));
                        } else
                            aList.add(new MessageElement(rL.get(i).getMessage(), rL.get(i).getNickname(), rL.get(i).getUserId()));
                    }
                    if (rL.size() == 0)
                        Toast.makeText(ChatActivity.this, "No new messages to retrieve", Toast.LENGTH_SHORT).show();
                    aa.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MessageAppLog", "onFailure called");
            }
        });
    }


    public void fetchMessages(View v){

        if(locationData.getLocation() == null) {
            Toast.makeText(ChatActivity.this, "Please enable location permissions in this device's settings", Toast.LENGTH_SHORT).show();
            return;
        }

        retrieveMessage();

        /*float lat = (float) locationData.getLocation().getLatitude();
        float lng = (float) locationData.getLocation().getLongitude();

        /* Make the GET call
        Call<MessageResponse> queryMessageFetch = service.messageFetch(lat,lng,caughtUserId);

        /* Enqueue the call on a callback, handle responses
        queryMessageFetch.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Response<MessageResponse> response) {
                Log.i("MessageAppLog", "Code: " + response.code());
                // Case: Unknown Server Error Code 500
                if (response.body() == null || (response.code() != 200)) {
                    Toast.makeText(ChatActivity.this, "Server Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Server request returns code 200, but with error field
                else if (response.body().getResult().equals("error")) {
                    Toast.makeText(ChatActivity.this, "Application Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Successful call and fetch of request
                else if (response.body().getResult().equals("ok")) {
                    Log.i("WeatherAppLog", "The result is: " + response.body().getResult());
                    List<ResultList> rL = response.body().getResultList();
                    aList.clear();
                    for(int i = rL.size()-1 ; i > -1; --i){
                        //addNewMessageToChat(rL.get(i).getMessage(), rL.get(i).getNickname(), rL.get(i).getUserId());
                            //Log.i("Chat Activity", caughtUserId);
                            //Log.i("Chat Activity", rL.get(i).userId.toString());
                        if(caughtUserId.equals(rL.get(i).getUserId())) {
                            aList.add(new MessageElement(rL.get(i).getMessage(), rL.get(i).getNickname()+ " (You)", rL.get(i).getUserId()));
                        }
                        else aList.add(new MessageElement(rL.get(i).getMessage(), rL.get(i).getNickname(), rL.get(i).getUserId()));
                    }
                    if(rL.size() == 0)
                        Toast.makeText(ChatActivity.this, "No new messages to retrieve", Toast.LENGTH_SHORT).show();
                    aa.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MessageAppLog", "onFailure called");
            }
        }); */
    }

    public void postMessage(final String message, final String message_id){
        float lat = (float) locationData.getLocation().getLatitude();
        float lng = (float) locationData.getLocation().getLongitude();
        String user_id = caughtUserId;
        final String nickname = caughtUsername;

        /* Make the POST call */
        Call<MessageResponse> queryMessagePost = service.messagePost(lat,lng, user_id, nickname, message, message_id);

        queryMessagePost.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Response<MessageResponse> response) {
                Log.i("MessageAppLog", "onResponse called for messagePost");
                aList.add(new MessageElement(message, nickname + " (You)", message_id));
                aa.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MessageAppLog", "onFailure called for MessagePost");
            }
        });

    }


    public interface MessageService {
        @GET("default/get_messages")
        Call<MessageResponse> messageFetch(
                @Query("lat") float lat,
                @Query("lng") float lng,
                @Query("user_id") String user_id);
        @POST("default/post_message")
        Call<MessageResponse> messagePost(
                @Query("lat") float lat,
                @Query("lng") float lng,
                @Query("user_id") String user_id,
                @Query("nickname") String nickname,
                @Query("message") String message,
                @Query("message_id") String message_id);
    }
}
