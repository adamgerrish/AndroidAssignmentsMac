package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
public class ChatDatabaseHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "Messaged.db";
    static int VERSION_NUM = 3;
    final static String KEY_ID = "KEY_ID";
    final static String KEY_MESSAGE = "KEY_MESSAGE";
    final static String TABLE_NAME = "MESSAGES";
    final static String CREATE_DB =
            "create table "+ TABLE_NAME+
                    " ( "+ KEY_ID+ " integer primary key autoincrement, "+
                    KEY_MESSAGE + " text not null);";
    ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        final String DROP = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        db.execSQL(DROP);
        onCreate(db);
    }
}
