package com.subhajitkar.commercial.projet_tulip.utils;

public class ObjectNote {

    private String mNoteId, mNoteTitle, mNoteContent, mDateCreated, mdatedUpdated, mEditorType;

    public ObjectNote(String noteId, String noteTitle, String noteContent, String dateCreated, String dateUpdated, String editorType){
        mNoteId = noteId;
        mNoteTitle = noteTitle;
        mNoteContent = noteContent;
        mDateCreated = dateCreated;
        mdatedUpdated = dateUpdated;
        mEditorType = editorType;
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

    public String getmEditorType() {
        return mEditorType;
    }
}