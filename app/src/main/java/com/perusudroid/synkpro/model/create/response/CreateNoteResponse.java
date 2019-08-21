package com.perusudroid.synkpro.model.create.response;

/**
 * Awesome Pojo Generator
 */
public class CreateNoteResponse {
    private Integer note_id;
    private Boolean error;
    private String message;

    public CreateNoteResponse() {
    }

    public CreateNoteResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public void setNote_id(Integer note_id) {
        this.note_id = note_id;
    }

    public Integer getNote_id() {
        return note_id;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Boolean getError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}