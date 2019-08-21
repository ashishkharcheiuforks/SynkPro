package com.perusudroid.synkpro.model.sync.request;

import com.perusudroid.synkpro.model.view.response.Data;

import java.util.List;

public class SyncRequest {

    private String lastSync;
    private List<Data> data;

    public SyncRequest(String lastSync, List<Data> data) {
        this.lastSync = lastSync;
        this.data = data;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
