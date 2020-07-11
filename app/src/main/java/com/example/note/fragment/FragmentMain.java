package com.example.note.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.Adapter.NoteAdapter;
import com.example.note.EditActivity;
import com.example.note.R;
import com.example.note.data.Note;
import com.example.note.data.NoteDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment {
    private static final String TAG = "FragmentMain";
    private RecyclerView mRecyclerView;
    FloatingActionButton mFab;
    private NoteAdapter mNoteAdapter;
    private List<Note> mNoteList = new ArrayList<>();
    private NoteDao mNoteDao;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);//set menu in fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        initRecycleView(view);
        initEvent();

        return view;
    }

    private void initView(View view) {
        mFab = view.findViewById(R.id.fab);

    }

    private void initEvent() {
        //click go to edit UI
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("mode", 4);
                startActivityForResult(intent, 0);
            }
        });
        // click update to edit UI
        mNoteAdapter.setonItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(long position) {
                mNoteDao = new NoteDao();
                Note curNote = mNoteDao.getNote(position);
                Log.d(TAG, "OnItemClick: "+position);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("tag", curNote.getTag());
                Log.d(TAG, "OnItemClick: "+curNote.getTag());
                intent.putExtra("mode", 3); //MODE 3 CLICK EDIT
                startActivityForResult(intent, 1);


            }
        });
    }

    private void initRecycleView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView_allTag);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        getNote();
        mNoteAdapter = new NoteAdapter(getActivity(), mNoteList);
        mRecyclerView.setAdapter(mNoteAdapter);
        refresh();
    }

    //get All note
    public void getNote() {
        mNoteDao = new NoteDao();
        if (mNoteList.size() > 0) {
            mNoteList.clear();
        }
        mNoteList.addAll(mNoteDao.getAllNotes());
    }

    //refresh note
    private void refresh() {
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        int returnMode;
        long noteId;
        returnMode = data.getExtras().getInt("mode", -1);
        noteId = data.getExtras().getLong("id", 0);
        Log.d(TAG, "id: " + noteId);
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
        getNote();
        refresh();

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        //search setting
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearch.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNoteAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // menu confirm delete
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                new AlertDialog.Builder(getContext())
                        .setMessage("刪除全部?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // delete noteAll
                                mNoteDao = new NoteDao();
                                mNoteDao.delNoteAll();
                                getNote();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
