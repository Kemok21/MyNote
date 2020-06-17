package com.jeko.mynote;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long mId;
    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "content")
    private String mContent;
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;

    public Note(long id, String title, String content, String date) {
        this.mId = id;
        this.mTitle = title;
        this.mContent = content;
        this.mDate = date;
    }
    @Ignore
    public Note(String title, String content, String date) {
        this.mTitle = title;
        this.mContent = content;
        this.mDate = date;
    }

    @Ignore
    public Note(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        this.mId = Long.parseLong(data[0]);
        this.mTitle = data[1];
        this.mContent = data[2];
        this.mDate = data[3];
    }

    public long getId() { return this.mId; }

    public String getTitle() { return this.mTitle; }

    public String getContent() { return this.mContent; }

    public String getDate() { return this.mDate; }

    public void setTitle(@NonNull String title) {
        this.mTitle = title;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public void setDate(@NonNull String date) {
        this.mDate = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.mId), this.mTitle, this.mContent, this.mDate});
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[0];
        }
    };
}
