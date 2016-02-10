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

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ChatActivity extends AppCompatActivity {

    private String result;
    public static String BASE_URL = "https://luca-teaching.appspot.com/";
    private MessageService service;


    private String caughtUsername;
    private String caughtUserId;
    public TextView chattingAs;
    public EditText chatInput;
    public Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chattingAs = (TextView) findViewById(R.id.chattingAs);
        chatInput = (EditText) findViewById(R.id.chatInput);
        sendButton = (Button) findViewById(R.id.send_btn);

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

    public void sendMessage(View v) {
        String message = chatInput.getText().toString();
        Toast.makeText(ChatActivity.this, "Send: "+message+" with user_id "+caughtUserId, Toast.LENGTH_SHORT).show();
        chatInput.setText("");
        addNewMessageToChat(message);
    }

    public void addNewMessageToChat(String message){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.activity_chat, null);
        ScrollView sv = (ScrollView) v.findViewById(R.id.messageList);
        LinearLayout ll = (LinearLayout) findViewById(R.id.messageListContainer);

        TextView tv = new TextView(this);
        tv.setText(message);
        ll.addView(tv);

    }

    public void fetchMessages(View v){
        /* Make the GET call */
        Call<MessageResponse> queryMessageFetch = service.messageFetch(result);

        /* Enqueue the call on a callback, handle responses */
        queryMessageFetch.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Response<MessageResponse> response) {
                Log.i("MessageAppLog", "Code: " + response.code());
                // Case: Unknown Server Error Code 500
                if(response.body() == null || (response.code() != 200)){
                    Toast.makeText(ChatActivity.this, "Server Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Server request returns code 200, but with error field
                else if(response.body().getResult().equals("error")){
                    Toast.makeText(ChatActivity.this, "Application Error. Please try again.", Toast.LENGTH_SHORT).show();
                }
                // Case: Successful call and fetch of request
                else if (response.body().getResult().equals("ok")){
                    Log.i("WeatherAppLog", "The result is: " + response.body().getResult());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MessageAppLog", "onFailure called");
            }
        });
    }


    public interface MessageService {
        @GET("/localmessages/default/get_messages")
        Call<MessageResponse> messageFetch(
                @Query("result") String result);
    }
}
