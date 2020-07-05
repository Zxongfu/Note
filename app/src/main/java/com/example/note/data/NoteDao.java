package com.example.note.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.note.Units.Constants;
import com.example.note.base.BaseApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteDao implements INoteDao {

    private final NoteDatabaseHelper mDbHelper;
    private final SQLiteDatabase db;

    public NoteDao() {
        mDbHelper = new NoteDatabaseHelper(BaseApplication.getContext());
        db = mDbHelper.getReadableDatabase();
    }


    /**
     * add note
     *
     * @param note
     */
    @Override
    public void addNote(Note note) {
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(Constants.NoteContent, note.getContent());
            values.put(Constants.NoteMode, note.getTag());
            values.put(Constants.NoteTime, note.getTime());
            long insert = db.insert(Constants.NOTE_TB_NAME, null, values);
            note.setId(insert);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

    }

    @Override
    public Note getNote(long id) {
        SQLiteDatabase db = null;
        Note note = null;
        try {
            db = mDbHelper.getReadableDatabase();
            db.beginTransaction();

            Cursor query = db.query(Constants.NOTE_TB_NAME, null, Constants.NoteId + "=" + id, null, null, null, null);
            while (query.moveToNext()) {
                int Id = query.getInt(query.getColumnIndex(Constants.NoteId));
                String context = query.getString(query.getColumnIndex(Constants.NoteContent));
                int tag = query.getInt(query.getColumnIndex(Constants.NoteMode));
                String time = query.getString(query.getColumnIndex(Constants.NoteTime));
                note = new Note(context, time, tag);
                note.setId(Id);
            }

            query.close();
            db.setTransactionSuccessful();
            return note;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return note;
    }

    @Override
    public List<Note> getAllNotes() {

        SQLiteDatabase db = null;
        List<Note> notes = new ArrayList<>();
        try {
            db = mDbHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor query = db.query(Constants.NOTE_TB_NAME, null, null, null, null, null, null);
            while (query.moveToNext()) {
                Note note = new Note();
                note.setId(query.getLong(query.getColumnIndex(Constants.NoteId)));
                note.setContent(query.getString(query.getColumnIndex(Constants.NoteContent)));
                note.setTag(query.getInt(query.getColumnIndex(Constants.NoteMode)));
                note.setTime(query.getString(query.getColumnIndex(Constants.NoteTime)));
                notes.add(note);
            }
            query.close();
            db.setTransactionSuccessful();
            return notes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return notes;
    }

    @Override
    public int update(Note note) {
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(Constants.NoteContent, note.getContent());
            values.put(Constants.NoteTime, note.getTime());
            values.put(Constants.NoteMode, note.getTag());
            int update = db.update(Constants.NOTE_TB_NAME, values, Constants.NoteId + "=" + note.getId(), null);
            db.setTransactionSuccessful();
            return update;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }


    @Override
    public void delNote(Note note) {
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(Constants.NOTE_TB_NAME, Constants.NoteId + "=" + note.getId(), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


}
