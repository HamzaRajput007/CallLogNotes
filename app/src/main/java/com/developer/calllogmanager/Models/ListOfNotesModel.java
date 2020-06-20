package com.developer.calllogmanager.Models;

public class ListOfNotesModel {

    String note , status ;
    int callLogId ;

    public ListOfNotesModel(String note, String status, int callLogId) {
        this.note = note;
        this.status = status;
        this.callLogId = callLogId;
    }

    public int getCallLogId() {
        return callLogId;
    }

    public void setCallLogId(int callLogId) {
        this.callLogId = callLogId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}