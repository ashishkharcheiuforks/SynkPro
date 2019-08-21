package com.perusudroid.synkpro.view.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.perusudroid.synkpro.R;
import com.perusudroid.synkpro.adapter.NotesAdapter;
import com.perusudroid.synkpro.common.CodeSnippet;
import com.perusudroid.synkpro.database.AppDatabase;
import com.perusudroid.synkpro.model.view.response.Data;
import com.perusudroid.synkpro.viewmodel.ViewNotesViewModel;

import java.util.List;

public class NotesListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private AppDatabase appDatabase;
    private FloatingActionButton fabAdd;
    private BroadcastReceiver mBroadcastReceiver;
    private ViewNotesViewModel viewNotesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        bindViews();
        setAssets();
        registerBroadCast();
        doCheckAndSync();
    }

    private void registerBroadCast() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("DO_FETCH_API");
        registerReceiver(mBroadcastReceiver, mIntentFilter);
    }


    private void bindViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setAssets() {

        viewNotesViewModel = ViewModelProviders.of(this).get(ViewNotesViewModel.class);
        appDatabase = AppDatabase.getAppDatabase(NotesListActivity.this);
        viewNotesViewModel.setAppDatabase(appDatabase);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fabAdd.setOnClickListener(this);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null &&
                        intent.getAction().equals("DO_FETCH_API")) {
                    viewNotesViewModel.refreshNotes();
                }
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK && data.getExtras().getBoolean("do_refresh")) {
                    if(CodeSnippet.isOnline(this)){
                        viewNotesViewModel.refreshNotes();
                    }else{
                        viewNotesViewModel.getLocalNotes();
                    }
                }
                break;
        }
    }

    private void doCheckAndSync() {


        viewNotesViewModel.getNoteList(CodeSnippet.isOnline(this) ? 1 : 0).observe(this, new Observer<List<Data>>() {
            @Override
            public void onChanged(@Nullable List<Data> mListData) {
                if(mListData != null){
                    if(mListData.size()  > 0){
                        setAdapterData(mListData);
                    }else{
                        Toast.makeText(NotesListActivity.this, "No local records found. Connect to internet and try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private void setAdapterData(List<Data> notes) {
        if (notesAdapter == null) {
            notesAdapter = new NotesAdapter(notes);
            recyclerView.setAdapter(notesAdapter);
        } else {
            notesAdapter.refresh(notes);
        }
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(NotesListActivity.this, CreateNoteActivity.class), 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }
}
