package com.perusudroid.synkpro.model.sync.response;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class SyncResponse{

  private Boolean error;
  private String message;
  public void setError(Boolean error){
   this.error=error;
  }
  public Boolean getError(){
   return error;
  }
  public void setMessage(String message){
   this.message=message;
  }
  public String getMessage(){
   return message;
  }
}