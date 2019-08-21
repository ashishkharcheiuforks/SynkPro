package com.perusudroid.synkpro.model.view.response;

import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class NotesResponse {
    private List<Data> data;
    private Boolean error;
    private String msg;

    public NotesResponse(Boolean error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Boolean getError() {
        return error;
    }
}