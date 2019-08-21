package com.perusudroid.synkpro.retrofit;

import com.perusudroid.synkpro.model.create.response.CreateNoteResponse;
import com.perusudroid.synkpro.model.view.response.Data;
import com.perusudroid.synkpro.model.view.request.NotesListRequest;
import com.perusudroid.synkpro.model.view.response.NotesResponse;
import com.perusudroid.synkpro.model.sync.request.SyncRequest;
import com.perusudroid.synkpro.model.sync.response.SyncResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("list.php")
    Call<NotesResponse> getList(@Body NotesListRequest notesListRequest);


    @POST("create.php")
    Call<CreateNoteResponse> createNote(@Body Data mData);


    @POST("sync.php")
    Call<SyncResponse> doSync(@Body SyncRequest syncRequest);

}
