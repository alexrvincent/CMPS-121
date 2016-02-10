package ucsc.cmps121.chatapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chattingAs = (TextView) findViewById(R.id.chattingAs);
        chatInput = (EditText) findViewById(R.id.chatInput);
        sendButton = (Button) findViewById(R.id.send_btn);
        refreshButton = (Button) findViewById(R.id.refresh_btn);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            caughtUsername = extras.getString("USERNAME", "NO_USERNAME");
            caughtUserId = extras.getString("user_id","0");
        }

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
        String message = chatInput.getText().toString();
        Toast.makeText(ChatActivity.this, "Send: "+message+" with user_id "+caughtUserId, Toast.LENGTH_SHORT).show();
        chatInput.setText("");
        addUserMessageToChat(message);
        postMessage(message, generateId());
    }

    public void addNewMessageToChat(String message, String nickname){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.activity_chat, null);
        ScrollView sv = (ScrollView) v.findViewById(R.id.messageList);
        LinearLayout ll = (LinearLayout) findViewById(R.id.messageListContainer);

        TextView tv = new TextView(this);
        tv.setText(nickname+" said: " +message);
        ll.addView(tv);

    }

    public void addUserMessageToChat(String message){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.activity_chat, null);
        ScrollView sv = (ScrollView) v.findViewById(R.id.messageList);
        LinearLayout ll = (LinearLayout) findViewById(R.id.messageListContainer);

        TextView tv = new TextView(this);
        tv.setText(caughtUsername+ " said: "+message);
        ll.addView(tv);
    }

    public void fetchMessages(View v){

        float lat = (float) locationData.getLocation().getLatitude();
        float lng = (float) locationData.getLocation().getLongitude();

        /* Make the GET call */
        Call<MessageResponse> queryMessageFetch = service.messageFetch(lat,lng,caughtUserId);

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
                    for(int i = 0; i < rL.size(); ++i){
                        addNewMessageToChat(rL.get(i).getMessage(), rL.get(i).getNickname());
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MessageAppLog", "onFailure called");
            }
        });
    }

    public void postMessage(String message, String message_id){
        float lat = (float) locationData.getLocation().getLatitude();
        float lng = (float) locationData.getLocation().getLongitude();
        String user_id = caughtUserId;
        String nickname = caughtUsername;

        /* Make the POST call */
        Call<MessageResponse> queryMessagePost = service.messagePost(lat,lng, user_id, nickname, message, message_id);

        queryMessagePost.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Response<MessageResponse> response) {
                Log.i("MessageAppLog", "onResponse called for messagePost");
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
