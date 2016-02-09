package ucsc.cmps121.chatapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {

    private String caughtUsername;
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
        if(extras != null) { caughtUsername = extras.getString("USERNAME", "NO_USERNAME"); }

        chattingAs.setText("Chatting as " + caughtUsername + " ...");
    }

    public void sendMessage(View v) {
        String message = chatInput.getText().toString();
        //Toast.makeText(ChatActivity.this, "Send: "+message, Toast.LENGTH_SHORT).show();
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


}
