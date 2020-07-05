package com.example.note;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.Adapter.NoteAdapter;
import com.example.note.Units.UIUtil;
import com.example.note.base.BaseActivity;
import com.example.note.data.Note;
import com.example.note.data.NoteDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private NoteDao mNoteDao;
    private NoteAdapter mNoteAdapter;
    private RecyclerView mRecyclerView;
    private List<Note> mNoteList = new ArrayList<>();

    private Toolbar mToolbar;
    FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        //click go to edit UI
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("mode", 4);
                startActivityForResult(intent, 0);
            }
        });
        //click update to edit UI
        mNoteAdapter.setonItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(long position) {
                mNoteDao = new NoteDao();
                Note curNote = mNoteDao.getNote(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("tag", curNote.getTag());
                intent.putExtra("mode", 3); //MODE 3 CLICK EDIT
                startActivityForResult(intent, 1);


            }
        });
    }

    private void initView() {
        mFab = findViewById(R.id.fab);
        mRecyclerView = findViewById(R.id.recyclerView);
        mToolbar = findViewById(R.id.myToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
        mNoteAdapter = new NoteAdapter(this, mNoteList);
        refresh();
        mRecyclerView.setAdapter(mNoteAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

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
            default:

                break;
        }
        refresh();

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    void refresh() {
        mNoteDao = new NoteDao();
        if (mNoteList.size() > 0) {
            mNoteList.clear();
        }
        mNoteList.addAll(mNoteDao.getAllNotes());
        mNoteAdapter.notifyDataSetChanged();
    }
}
