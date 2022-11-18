package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    static SQLiteDatabase database;
    static final String GET_MESSAGES = "SELECT KEY_ID, KEY_MESSAGE FROM MESSAGES";
    ArrayList<String> messages = new ArrayList<>();
    FrameLayout frameLayout;
    Cursor cursor;
    ChatAdapter messageAdapter;
    MessageFragment fragment;
    FragmentTransaction fragmentTransaction;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate ");
        ChatDatabaseHelper databaseHelper = new ChatDatabaseHelper(this);
        frameLayout = findViewById(R.id.frameLayout);
        database = databaseHelper.getWritableDatabase();
        cursor = database.rawQuery(GET_MESSAGES,null);
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
        messageAdapter = new ChatAdapter(this);

        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String m = cursor.getString(cursor.getColumnIndex("KEY_MESSAGE"));
                if(frameLayout != null){
                    //FragmentTransaction
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragment = new MessageFragment(ChatWindow.this);
                    Bundle arguments = new Bundle();
                    arguments.putString("message",m);
                    arguments.putLong("id",l);
                    fragment.setArguments(arguments);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null).commit();


                }
                else{
                    Bundle arguments = new Bundle();
                    Log.i("passing message", m);
                    arguments.putString("message",m);
                    arguments.putLong("id",l);
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(arguments);
                    startActivityForResult(intent,10);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = messageBox.getText().toString();
                messages.add(text);
                ContentValues cValues = new ContentValues();
                cValues.put(ChatDatabaseHelper.KEY_MESSAGE, text);
                database.insert(ChatDatabaseHelper.TABLE_NAME,"NullPlaceHolder",cValues);
                messageAdapter.notifyDataSetChanged();
                messageBox.setText("");
            }
        });


        chatView.setAdapter(messageAdapter);
    }

    private class ChatAdapter extends ArrayAdapter<String>{
        @Override
        public long getItemId(int position) {
            super.getItemId(position);
            if (position >= cursor.getCount()){
                cursor = database.rawQuery(GET_MESSAGES,null);
            }

            cursor.moveToPosition(position);
            @SuppressLint("Range") long id = Long.parseLong(cursor.getString(cursor.getColumnIndex("KEY_ID")));
            return id ;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            String row = data.getStringExtra("id");
            Log.i("delete row: ", ""+row);
            database.delete(ChatDatabaseHelper.TABLE_NAME,ChatDatabaseHelper.KEY_ID+ "="+row,null);
            messages.remove(Integer.parseInt(row)-1);
            messageAdapter.notifyDataSetChanged();
        }

    }
    void deleteRow(int row){
        FragmentManager fragmentManager = getSupportFragmentManager();
        database.delete(ChatDatabaseHelper.TABLE_NAME,ChatDatabaseHelper.KEY_ID+ "="+row,null);
        messages.remove(row-1);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
        messageAdapter.notifyDataSetChanged();
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