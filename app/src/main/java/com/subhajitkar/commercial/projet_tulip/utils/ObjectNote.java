package com.subhajitkar.commercial.projet_tulip.utils;

public class ObjectNote {

    private String mNoteId, mNoteTitle, mNoteContent, mDateCreated, mdatedUpdated;

    public ObjectNote(String noteId, String noteTitle, String noteContent, String dateCreated, String dateUpdated){
     mNoteId = noteId;
     mNoteTitle = noteTitle;
     mNoteContent = noteContent;
     mDateCreated = dateCreated;
     mdatedUpdated = dateUpdated;
    }

    public String getmNoteId() {
        return mNoteId;
    }

    public String getmNoteTitle() {
        return mNoteTitle;
    }

    public String getmNoteContent() {
        return mNoteContent;
    }

    public String getmDateCreated() {
        return mDateCreated;
    }

    public String getMdatedUpdated() {
        return mdatedUpdated;
    }
}
