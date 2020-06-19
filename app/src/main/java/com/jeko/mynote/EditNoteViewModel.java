package com.jeko.mynote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditNoteViewModel extends ViewModel {

    MutableLiveData<Note> mNote;

    public LiveData<Note> getNote() {
        if (mNote == null) {
            mNote = new MutableLiveData<>();
            makeNote();
        }
        return mNote;
    }

    public void setNote(Note note) {
        mNote.postValue(note);
    }

    private void  makeNote() {
        mNote = new MutableLiveData<>(new Note("", "", ""));
    }
}
