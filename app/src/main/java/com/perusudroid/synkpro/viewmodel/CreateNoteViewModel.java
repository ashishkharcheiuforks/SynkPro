package com.perusudroid.synkpro.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.perusudroid.synkpro.database.AppDatabase;
import com.perusudroid.synkpro.model.create.response.CreateNoteResponse;
import com.perusudroid.synkpro.model.view.response.Data;
import com.perusudroid.synkpro.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNoteViewModel extends ViewModel {

    private MutableLiveData<CreateNoteResponse> createNoteResponse = new MutableLiveData<>();
    private AppDatabase appDatabase;


    public void setAppDatabase(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public LiveData<CreateNoteResponse> createNote(Data mData, final Long insertId) {



        ApiClient.getInstance().getServicesApi().createNote(mData).enqueue(
                new Callback<CreateNoteResponse>() {
                    @Override
                    public void onResponse(Call<CreateNoteResponse> call, Response<CreateNoteResponse> response) {
                        if (response.code() == 200 && response.body() != null) {
                            if(!response.body().getError()){
                               if(appDatabase != null){
                                   appDatabase.getNotesDao().updateNotesIfSynced(insertId);
                               }
                                createNoteResponse.postValue(response.body());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<CreateNoteResponse> call, Throwable t) {
                        Log.e("CreateNote", "onFailure: " + t.getLocalizedMessage());
                        createNoteResponse.postValue(new CreateNoteResponse(true, t.getLocalizedMessage()));
                    }
                }
        );

        return createNoteResponse;
    }
}
