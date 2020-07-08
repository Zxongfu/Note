package com.example.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.note.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private EditText mEditText;
    private long id = 0;
    private String mOldContent = "";
    private String mOldTime = "";
    private int mOldTag = 1;
    private int mMode;
    private int tag = 1;
    private Intent intent = new Intent();
    private boolean tagChange = false;
    private Toolbar mToolbar;
    private Spinner mSpinner;
    private int mCurrentPosition;
    private int mPos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();

    }

    private void initView() {
        mEditText = findViewById(R.id.edit);
        mEditText.setBackgroundResource(android.R.color.transparent);
        mSpinner =findViewById(R.id.spinner);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.type));
        mSpinner.setOnItemSelectedListener(this);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(arrayAdapter);

        mToolbar = findViewById(R.id.myToolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoMessage();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Intent getIntent = getIntent();
        mMode = getIntent.getIntExtra("mode", 0);

        if (mMode == 3) {
            id = getIntent().getLongExtra("id", 0);
            mOldContent = getIntent().getStringExtra("content");
            mOldTime = getIntent().getStringExtra("time");
            mOldTag = getIntent().getIntExtra("tag", 1);
            mEditText.setText(mOldContent);
            mEditText.setSelection(mOldContent.length());
            mPos = mOldTag-1;
            mSpinner.setSelection(mPos);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
            case KeyEvent.KEYCODE_BACK:
                autoMessage();
                setResult(RESULT_OK, intent);
                finish();
                return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private void autoMessage() {
        //mode 4 create note
        if (mMode == 4) {
            // mode -1 nothing happen
            if (mEditText.getText().toString().length() == 0) {
                intent.putExtra("mode", -1);
            } else {
                //add new note
                tag = mCurrentPosition;
                intent.putExtra("mode", 0);
                intent.putExtra("content", mEditText.getText().toString());
                intent.putExtra("time", dateToString());
                intent.putExtra("tag", tag);
            }
        } else {
            //mode 3 edit note
            if (mEditText.getText().toString().equals(mOldContent) && !tagChange && mSpinner.getSelectedItem().equals(mPos) ) {
                intent.putExtra("mode", -1); //mode -1 nothing happen
            } else {
                mPos=mCurrentPosition;
                intent.putExtra("mode", 1); //mode 1 update note
                intent.putExtra("content", mEditText.getText().toString());
                intent.putExtra("time", dateToString());
                intent.putExtra("id", id);
                intent.putExtra("tag", mPos);

            }

        }

    }

    // create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    // menu confirm delete
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                new AlertDialog.Builder(EditActivity.this)
                        .setMessage("刪除?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (mEditText.getText().toString().length() != 0) {
                                    if (mMode == 4) {
                                        intent.putExtra("mode", -1);
                                        setResult(RESULT_OK, intent);
                                    } else {
                                        //existing note Mode 2
                                        intent.putExtra("mode", 2);
                                        intent.putExtra("id", id);
                                        setResult(RESULT_OK, intent);
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(EditActivity.this, "沒筆記", Toast.LENGTH_SHORT).show();
                                }

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

    private String dateToString() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN);
        return simpleDateFormat.format(date);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.mCurrentPosition = i+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
