package com.example.note.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.note.Units.Constants;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "NoteDatabaseHelper";

    public NoteDatabaseHelper(@Nullable Context context) {
        super(context, Constants.NOTE_DB_NAME, null, Constants.DB_VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate...");
        String noteSql = "CREATE TABLE " + Constants.NOTE_TB_NAME + "(" +
                Constants.NoteId + " integer primary key autoincrement, " +
                Constants.NoteContent + " text not null, " +
                Constants.NoteTime + " text not null, " +
                Constants.NoteMode + " integer default 1 " +
                ") ";
        sqLiteDatabase.execSQL(noteSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
