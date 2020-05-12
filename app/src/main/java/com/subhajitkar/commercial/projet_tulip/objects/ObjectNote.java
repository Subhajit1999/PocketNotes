package com.subhajitkar.commercial.projet_tulip.objects;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class ObjectNote {

    public String mNoteId, mNoteTitle, mNoteContent, mDateCreated, mdatedUpdated, mEditorType, mTag, mTableId;
    public boolean isStarred;

    @ParcelConstructor ObjectNote(){}

    public ObjectNote(String noteId, String noteTitle, String noteContent, String dateCreated, String dateUpdated,
               String editorType, boolean star, String tag){
        mNoteId = noteId;
        mNoteTitle = noteTitle;
        mNoteContent = noteContent;
        mDateCreated = dateCreated;
        mdatedUpdated = dateUpdated;
        mEditorType = editorType;
        isStarred = star;
        mTag = tag;
    }
    public ObjectNote(String noteId, String noteTitle, String noteContent, String dateCreated, String dateUpdated,
                      String editorType, String tableId, String tag){
        mNoteId = noteId;
        mNoteTitle = noteTitle;
        mNoteContent = noteContent;
        mDateCreated = dateCreated;
        mdatedUpdated = dateUpdated;
        mEditorType = editorType;
        mTableId = tableId;
        mTag = tag;
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

    public String getmTableId() {
        return mTableId;
    }

    public String getmTag() {
        return mTag;
    }

    public boolean getIsStarred(){
        return isStarred;
    }
}