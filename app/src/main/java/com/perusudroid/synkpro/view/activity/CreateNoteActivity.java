package com.perusudroid.synkpro.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.perusudroid.synkpro.R;
import com.perusudroid.synkpro.common.CodeSnippet;
import com.perusudroid.synkpro.database.AppDatabase;
import com.perusudroid.synkpro.model.create.response.CreateNoteResponse;
import com.perusudroid.synkpro.model.view.response.Data;
import com.perusudroid.synkpro.viewmodel.CreateNoteViewModel;
import com.perusudroid.synkpro.worker.SynkWorker;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etTitle, etContent, etCreatedUser;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private TextInputLayout title_input_lay, content_input_lay, user_input_lay;
    private AppDatabase appDatabase;
    private ProgressDialog progressDialog;
    private CreateNoteViewModel createNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        bindViews();
        setAssets();
    }

    private void bindViews() {
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        etCreatedUser = findViewById(R.id.etCreatedUser);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        title_input_lay = findViewById(R.id.title_input_lay);
        content_input_lay = findViewById(R.id.content_input_lay);
        user_input_lay = findViewById(R.id.user_input_lay);
    }

    private void setAssets() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Syncing");
        btnSubmit.setOnClickListener(this);
        createNoteViewModel = ViewModelProviders.of(this).get(CreateNoteViewModel.class);
        appDatabase = AppDatabase.getAppDatabase(CreateNoteActivity.this);
        createNoteViewModel.setAppDatabase(appDatabase);
    }

    @Override
    public void onClick(View v) {

        if (isValidated()) {

            Data mData = new Data();
            mData.setNote_by(etCreatedUser.getText().toString().trim());
            mData.setNote_content(etContent.getText().toString().trim());
            mData.setNote_title(etTitle.getText().toString().trim());
            mData.setNote_rating(ratingBar.getRating());
            mData.setNote_created_on(CodeSnippet.getCreatedOn());
            mData.setNote_is_synced(0);

            final Long insertId = appDatabase.getNotesDao().insertNoteAndGetId(mData);

            Toast.makeText(this, "Inserted " + insertId, Toast.LENGTH_SHORT).show();

            //doApiCreateNote(mData, insertId);

            if (CodeSnippet.isOnline(this)) {
                doApiCreateNote(mData, insertId);
            } else {
                startWorkManager("1");
                doFinish();
            }
        }

    }


    private void doApiCreateNote(Data mData, final Long insertId) {

        progressDialog.show();

        mData.setNote_is_synced(1);

        createNoteViewModel.createNote(mData, insertId).observe(this, new Observer<CreateNoteResponse>() {
            @Override
            public void onChanged(@Nullable CreateNoteResponse createNoteResponse) {

                if (createNoteResponse != null && !createNoteResponse.getError()) {

                    Toast.makeText(CreateNoteActivity.this, createNoteResponse.getMessage(), Toast.LENGTH_LONG).show();
                    doFinish();
                }
            }
        });
    }


    private void startWorkManager(String unique_id) {

        WorkManager workManager = WorkManager.getInstance();
        Constraints.Builder constraints = new Constraints.Builder();
        constraints.setRequiredNetworkType(NetworkType.CONNECTED);

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SynkWorker.class)
                .setConstraints(constraints.build())
                .addTag("clean")
                .setInputData(createInputDataForUri("10")).build();
        workManager.beginUniqueWork(unique_id,
                ExistingWorkPolicy.REPLACE,
                oneTimeWorkRequest).enqueue();
    }

    private androidx.work.Data createInputDataForUri(String requestData) {
        androidx.work.Data.Builder builder = new androidx.work.Data.Builder();
        if (requestData != null) {
            builder.putString("data_id", requestData);
        }
        return builder.build();

    }


    private void doFinish() {

        progressDialog.dismiss();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("do_refresh", true);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private boolean isValidated() {


        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            title_input_lay.setError("Note Title Required");
            return false;
        } else {
            title_input_lay.setError(null);
        }

        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            content_input_lay.setError("Note Content Required");
            return false;
        } else {
            content_input_lay.setError(null);
        }

        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            user_input_lay.setError("Created User Required");
            return false;
        } else {
            user_input_lay.setError(null);
        }

        if (!(ratingBar.getNumStars() > 0)) {
            Toast.makeText(this, "Rating Required", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}
