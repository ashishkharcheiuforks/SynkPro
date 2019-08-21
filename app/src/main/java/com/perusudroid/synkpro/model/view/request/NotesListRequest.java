package com.perusudroid.synkpro.model.view.request;
/**
 * Awesome Pojo Generator
 * */
public class NotesListRequest{
  private String lastSyncTime;
  public NotesListRequest(){
  }
  public NotesListRequest(String lastSyncTime){
   this.lastSyncTime=lastSyncTime;
  }
  public void setLastSyncTime(String lastSyncTime){
   this.lastSyncTime=lastSyncTime;
  }
  public String getLastSyncTime(){
   return lastSyncTime;
  }
}