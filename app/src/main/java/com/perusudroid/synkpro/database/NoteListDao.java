package com.perusudroid.synkpro.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.perusudroid.synkpro.model.view.response.Data;

import java.util.List;

@Dao
public interface NoteListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(Data data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertNoteAndGetId(Data data);

    @Query("SELECT * FROM Data ORDER BY note_created_on DESC")
    List<Data>  getAllNotes();

    @Query("SELECT count(*) FROM Data ORDER BY note_created_on DESC")
    Integer getNotesCount();

    @Query("SELECT * FROM Data WHERE note_is_synced = 0")
    List<Data> getUnSyncedNotes();

    @Query("DELETE FROM Data WHERE note_id=:id")
    void deleteSamplingTransactionById(Integer id);

    @Query("DELETE FROM Data")
    void deleteAllSamplingTransaction();

    @Query("SELECT * FROM Data WHERE note_id=:id")
    Data getSamplingTransactionById(Integer id);

    @Query("UPDATE Data SET note_is_synced = 1 WHERE note_id =:id")
    void updateNotesIfSynced(Long id);

    @Query("UPDATE Data SET note_is_synced = 0 WHERE note_id =:id")
    void updateNotesIfNotSynced(Long id);

}
