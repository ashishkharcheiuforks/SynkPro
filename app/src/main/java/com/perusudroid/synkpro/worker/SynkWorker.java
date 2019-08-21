package com.perusudroid.synkpro.worker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.perusudroid.synkpro.database.AppDatabase;
import com.perusudroid.synkpro.common.CodeSnippet;
import com.perusudroid.synkpro.database.NoteListDao;
import com.perusudroid.synkpro.model.view.response.Data;
import com.perusudroid.synkpro.model.sync.request.SyncRequest;
import com.perusudroid.synkpro.model.sync.response.SyncResponse;
import com.perusudroid.synkpro.retrofit.ApiClient;

import java.io.IOException;
import java.util.List;

import androidx.work.Worker;
import retrofit2.Response;

public class SynkWorker extends Worker {


    @Override
    public void onStopped(boolean cancelled) {
        super.onStopped(cancelled);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d("SyncWorker", "doWork: doing some work");


        Context context = getApplicationContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

        NoteListDao noteListDao = appDatabase.getNotesDao();

        List<Data> unSyncedNotes = noteListDao.getUnSyncedNotes();

        SyncRequest syncRequest = new SyncRequest(CodeSnippet.getCreatedOn(), unSyncedNotes);

        try {

           Response<SyncResponse> syncResponseRaw = ApiClient.getInstance().getServicesApi().doSync(syncRequest).execute();
           if(syncResponseRaw.isSuccessful() &&
                   syncResponseRaw.code() == 200 &&
                   syncResponseRaw.body() != null){
               SyncResponse syncResponse = syncResponseRaw.body();

               for(Data mData : unSyncedNotes){
                   noteListDao.updateNotesIfSynced(mData.getNote_id());
               }

               Intent mIntent = new Intent();
               mIntent.setAction("DO_FETCH_API");
               context.sendBroadcast(mIntent);
               Log.d("SyncWorker", "doWork: bb send");

               return Result.SUCCESS;

           }
        } catch (IOException e) {

            for(Data mData : unSyncedNotes){
                noteListDao.updateNotesIfNotSynced(mData.getNote_id());
            }
            e.printStackTrace();
        }

        return Result.RETRY;
    }
}
