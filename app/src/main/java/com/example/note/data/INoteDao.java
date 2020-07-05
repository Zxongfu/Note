package com.example.note.data;


import java.util.List;

public interface INoteDao {
    /**
     * add note
     * @param note
     */
    void addNote(Note note);

    /**
     * delete note
     * @param note
     */
    void delNote(Note note);

    /**
     * get note data
     */
    Note getNote(long id);

    /**
     * get All  note data
     * @return
     */
    List<Note> getAllNotes();

    /**
     * update note
     * @param note
     */
    int update(Note note);
}
