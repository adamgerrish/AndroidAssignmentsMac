package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    ArrayList<String> chatMessages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        ListView chatView =  (ListView) findViewById(R.id.chatView);
        EditText messageBox = (EditText) findViewById(R.id.editText3);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        //in this case, “this” is the ChatWindow, which is-A Context object
        ChatAdapter messageAdapter =new ChatAdapter( this );

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatMessages.add(messageBox.getText().toString());
                messageAdapter.notifyDataSetChanged();
                messageBox.setText("");
            }
        });
        chatView.setAdapter (messageAdapter);
    }

    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        //This returns the number of rows that will be in your listView.
        // In your case, it should be the number of strings in the array
        // list object ( return list.size() ).
        public int getCount(){
            return chatMessages.size();
        }
        //This returns the item to show in the list at the specified
        // position: ( return list.get(position) )
        public String getItem(int position){
            return chatMessages.get(position);
        }
        //this returns the layout that will be positioned at the
        // specified row in the list. Do this in step 9.
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }
    }
}