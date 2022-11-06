package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    static SQLiteDatabase database;
    static final String GET_MESSAGES = "SELECT KEY_MESSAGE FROM MESSAGES";
    ArrayList<String> messages = new ArrayList<>();
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate ");
        ChatDatabaseHelper databaseHelper = new ChatDatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        final Cursor cursor = database.rawQuery(GET_MESSAGES,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
            messages.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );
        for (int i = 0; i <cursor.getColumnCount();i++){
            Log.i(ACTIVITY_NAME, "Column Name: "+ cursor.getColumnName(i));
        }

        ListView chatView =  (ListView) findViewById(R.id.chatView);
        EditText messageBox = (EditText) findViewById(R.id.editText3);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        //in this case, “this” is the ChatWindow, which is-A Context object
        ChatAdapter messageAdapter = new ChatAdapter( this );

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cm = messageBox.getText().toString();
                messages.add(cm);
                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, cm);
                database.insert(ChatDatabaseHelper.TABLE_NAME,"NullPlaceHolder",contentValues);
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
            return messages.size();
        }
        //This returns the item to show in the list at the specified
        // position: ( return list.get(position) )
        public String getItem(int position){
            return messages.get(position);
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
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
}