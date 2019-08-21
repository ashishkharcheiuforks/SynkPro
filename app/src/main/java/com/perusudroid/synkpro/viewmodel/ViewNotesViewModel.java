package com.perusudroid.synkpro.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.perusudroid.synkpro.database.AppDatabase;
import com.perusudroid.synkpro.model.view.request.NotesListRequest;
import com.perusudroid.synkpro.model.view.response.Data;
import com.perusudroid.synkpro.model.view.response.NotesResponse;
import com.perusudroid.synkpro.retrofit.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewNotesViewModel extends ViewModel {

    private MutableLiveData<List<Data>> listResponse;
    private AppDatabase appDatabase;


    public void setAppDatabase(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public void refreshNotes() {
        doFetchApiData();
    }

    public void getLocalNotes(){
        if(appDatabase != null){
            listResponse.postValue(appDatabase.getNotesDao().getAllNotes());
        }
    }

    public MutableLiveData<List<Data>> getNoteList(int i) {


        if (i == 1) {
            //has network. so get results from API
            if (listResponse == null) {

                listResponse = new MutableLiveData<>();

                doFetchApiData();

            }
        } else {
            //publish local db data
            if (appDatabase != null) {

                if(listResponse == null){
                    listResponse = new MutableLiveData<>();
                }

                listResponse.postValue(appDatabase.getNotesDao().getAllNotes());
            }
        }


        return listResponse;
    }

    private void doFetchApiData() {

        //call api and store to DB
        ApiClient.getInstance().getServicesApi().getList(new NotesListRequest("")).enqueue(new Callback<NotesResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotesResponse> call, @NonNull Response<NotesResponse> response) {
                if (response.code() == 200 && response.body() != null && !response.body().getError()) {
                    if (appDatabase != null) {

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            appDatabase.getNotesDao().insertNotes(response.body().getData().get(i));
                        }

                        listResponse.postValue(appDatabase.getNotesDao().getAllNotes());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<NotesResponse> call, @NonNull Throwable t) {
                // listResponse.postValue(new NotesResponse(true, t.getLocalizedMessage()));
            }
        });
    }


}
