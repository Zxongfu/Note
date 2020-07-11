package com.example.note.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.Adapter.NoteAdapter;
import com.example.note.EditActivity;
import com.example.note.R;
import com.example.note.data.Note;
import com.example.note.data.NoteDao;

import java.util.ArrayList;
import java.util.List;

public class FragmentWork extends Fragment {
    private List<Note> mNoteList =new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NoteDao mNoteDao;
    private NoteAdapter mNoteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_work, container, false);
        initRecyclerView(view);
        initEvent();
        return view;
    }

    private void initEvent() {
        mNoteAdapter.setonItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(long position) {
                mNoteDao = new NoteDao();
                Note curNote = mNoteDao.getNote(position);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("tag", curNote.getTag());
                intent.putExtra("mode", 3); //MODE 3 CLICK EDIT
                startActivityForResult(intent, 1);


            }
        });
    }

    private void initRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView_workTag);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getWorkNote();
        mNoteAdapter = new NoteAdapter(getActivity(),mNoteList);
        mRecyclerView.setAdapter(mNoteAdapter);
    }

    private void getWorkNote() {
        mNoteDao = new NoteDao();
        if (mNoteList.size() > 0) {
            mNoteList.clear();
        }
        mNoteList.addAll(mNoteDao.getNoteTag("3"));
    }

    private void refresh() {
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int returnMode;
        long noteId;
        returnMode = data.getExtras().getInt("mode", -1);
        noteId = data.getExtras().getLong("id", 0);
        switch (returnMode) {
            //update note
            case 1:
                String updateContent = data.getStringExtra("content");
                String updateTime = data.getStringExtra("time");
                int updateTag = data.getExtras().getInt("tag", 1);
                Note updateNote = new Note(updateContent, updateTime, updateTag);
                updateNote.setId(noteId);
                mNoteDao = new NoteDao();
                mNoteDao.update(updateNote);
                break;
            //add new note
            case 0:
                String newContent = data.getStringExtra("content");
                String newTime = data.getStringExtra("time");
                int newTag = data.getExtras().getInt("tag", 1);
                Note newNote = new Note(newContent, newTime, newTag);
                mNoteDao = new NoteDao();
                mNoteDao.addNote(newNote);
                break;
            //delete one note
            case 2:
                Note note = new Note();
                mNoteDao = new NoteDao();
                note.setId(noteId);
                mNoteDao.delNote(note);
                break;
        }
        getWorkNote();
        refresh();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
