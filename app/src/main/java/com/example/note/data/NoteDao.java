package com.example.note.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.note.Units.Constants;
import com.example.note.base.BaseApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteDao implements INoteDao {

    private final NoteDatabaseHelper mDbHelper;
    private SQLiteDatabase dbWrite, dbRead;

    public NoteDao() {
        mDbHelper = new NoteDatabaseHelper(BaseApplication.getContext());
        dbWrite = mDbHelper.getWritableDatabase();
        dbRead = mDbHelper.getReadableDatabase();
    }


    /**
     * add note
     *
     * @param note
     */
    @Override
    public void addNote(Note note) {

        try {
            dbWrite = mDbHelper.getWritableDatabase();
            dbWrite.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(Constants.NoteContent, note.getContent());
            values.put(Constants.NoteMode, note.getTag());
            values.put(Constants.NoteTime, note.getTime());
            long insert = dbWrite.insert(Constants.NOTE_TB_NAME, null, values);
            note.setId(insert);
            dbWrite.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbWrite != null) {
                dbWrite.endTransaction();
                dbWrite.close();
                dbRead.close();
            }
        }

    }

    @Override
    public Note getNote(long id) {

        Note note = null;
        try {
            dbRead = mDbHelper.getReadableDatabase();
            dbRead.beginTransaction();

            Cursor query = dbRead.query(Constants.NOTE_TB_NAME, null, Constants.NoteId + "=" + id, null, null, null, null);
            while (query.moveToNext()) {
                int Id = query.getInt(query.getColumnIndex(Constants.NoteId));
                String context = query.getString(query.getColumnIndex(Constants.NoteContent));
                int tag = query.getInt(query.getColumnIndex(Constants.NoteMode));
                String time = query.getString(query.getColumnIndex(Constants.NoteTime));
                note = new Note(context, time, tag);
                note.setId(Id);
            }

            query.close();
            dbRead.setTransactionSuccessful();
            return note;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbRead != null) {
                dbRead.endTransaction();
                dbRead.close();
                dbWrite.close();
            }
        }

        return note;
    }

    @Override
    public List<Note> getAllNotes() {


        List<Note> notes = new ArrayList<>();
        try {
            dbRead = mDbHelper.getReadableDatabase();
            dbRead.beginTransaction();
            Cursor query = dbRead.query(Constants.NOTE_TB_NAME, null, null, null, null, null, null);
            while (query.moveToNext()) {
                Note note = new Note();
                note.setId(query.getLong(query.getColumnIndex(Constants.NoteId)));
                note.setContent(query.getString(query.getColumnIndex(Constants.NoteContent)));
                note.setTag(query.getInt(query.getColumnIndex(Constants.NoteMode)));
                note.setTime(query.getString(query.getColumnIndex(Constants.NoteTime)));
                notes.add(note);
            }
            query.close();
            dbRead.setTransactionSuccessful();
            return notes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbRead != null) {
                dbRead.endTransaction();
                dbRead.close();
                dbWrite.close();
            }
        }
        return notes;
    }

    @Override
    public int update(Note note) {

        try {
            dbWrite = mDbHelper.getWritableDatabase();
            dbWrite.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(Constants.NoteContent, note.getContent());
            values.put(Constants.NoteTime, note.getTime());
            values.put(Constants.NoteMode, note.getTag());
            int update = dbWrite.update(Constants.NOTE_TB_NAME, values, Constants.NoteId + "=" + note.getId(), null);
            dbWrite.setTransactionSuccessful();
            return update;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbWrite != null) {
                dbWrite.endTransaction();
                dbWrite.close();
                dbRead.close();
            }
        }
        return 0;
    }

    @Override
    public void delNoteAll() {
        try {
            dbWrite = mDbHelper.getWritableDatabase();
            dbWrite.beginTransaction();
            dbWrite.delete(Constants.NOTE_TB_NAME, null, null);
            String update="update sqlite_sequence set seq=0 where name='note'";
            dbWrite.execSQL(update);
            dbWrite.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbWrite != null) {
                dbWrite.endTransaction();
                dbWrite.close();
                dbRead.close();
            }
        }
    }


    @Override
    public void delNote(Note note) {

        try {
            dbWrite = mDbHelper.getWritableDatabase();
            dbWrite.beginTransaction();
            dbWrite.delete(Constants.NOTE_TB_NAME, Constants.NoteId + "=" + note.getId(), null);
            dbWrite.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbWrite != null) {
                dbWrite.endTransaction();
                dbWrite.close();
                dbRead.close();
            }
        }
    }


}
