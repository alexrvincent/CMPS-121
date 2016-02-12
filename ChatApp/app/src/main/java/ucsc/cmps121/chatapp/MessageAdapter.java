package ucsc.cmps121.chatapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Alex on 2/10/2016.
 */
public class MessageAdapter extends ArrayAdapter<MessageElement>{

    int resource;
    Context context;

    /* Constructor */
    public MessageAdapter(Context _context, int _resource, List<MessageElement> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;

        MessageElement w = getItem(position);

        /* Inflate the view if necessary */
        if (convertView == null) {
            newView = new LinearLayout((getContext()));
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService((inflater));
            vi.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }


        /*Fill in the View*/
        TextView msg_tv = (TextView) newView.findViewById(R.id.new_message);
        TextView nn_tv = (TextView) newView.findViewById(R.id.new_nickname);
        RelativeLayout cont_rl = (RelativeLayout) newView.findViewById(R.id.message_container);

        msg_tv.setText(w.message);
        nn_tv.setText(w.nickname);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (w.isMe) {
            lp.gravity = Gravity.RIGHT;
            cont_rl.setBackgroundColor(Color.parseColor("#C8E6C9"));
            cont_rl.setLayoutParams(lp);
        }
        else {
            lp.gravity = Gravity.LEFT;
            cont_rl.setBackgroundColor(Color.parseColor("#F0F4C3"));
            cont_rl.setLayoutParams(lp);
        }



        return newView;
    }


}
