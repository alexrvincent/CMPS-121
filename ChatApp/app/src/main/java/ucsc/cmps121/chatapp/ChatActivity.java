package ucsc.cmps121.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    private String caughtUsername;
    public TextView chattingAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chattingAs = (TextView) findViewById(R.id.chattingAs);

        Bundle extras = getIntent().getExtras();
        if(extras != null) { caughtUsername = extras.getString("USERNAME", "NO_USERNAME"); }

        chattingAs.setText("Chatting as " + caughtUsername + " ...");
    }
}
