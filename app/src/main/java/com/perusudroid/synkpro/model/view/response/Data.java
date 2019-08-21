package com.perusudroid.synkpro.model.view.response;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Awesome Pojo Generator
 */
@Entity
public class Data {

    private String note_content;
    @PrimaryKey
    @NonNull
    private Long note_id;
    private Float note_rating;
    private String note_title;
    private String note_created_on;
    private Integer note_is_synced;
    private String note_by;
    private String note_last_updated_on;



    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_id(Long note_id) {
        this.note_id = note_id;
    }

    @NonNull
    public Long getNote_id() {
        return note_id;
    }

    public void setNote_rating(Float note_rating) {
        this.note_rating = note_rating;
    }

    public Float getNote_rating() {
        return note_rating;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_created_on(String note_created_on) {
        this.note_created_on = note_created_on;
    }

    public String getNote_created_on() {
        return note_created_on;
    }

    public void setNote_is_synced(Integer note_is_synced) {
        this.note_is_synced = note_is_synced;
    }

    public Integer getNote_is_synced() {
        return note_is_synced;
    }

    public void setNote_by(String note_by) {
        this.note_by = note_by;
    }

    public String getNote_by() {
        return note_by;
    }

    public void setNote_last_updated_on(String note_last_updated_on) {
        this.note_last_updated_on = note_last_updated_on;
    }

    public String getNote_last_updated_on() {
        return note_last_updated_on;
    }
}