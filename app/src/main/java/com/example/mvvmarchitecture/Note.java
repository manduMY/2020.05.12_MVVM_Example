package com.example.mvvmarchitecture;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Room(Note.class)
// SQLite(데이터 베이스)를 사용하기 위해 Room을 추가.
@Entity(tableName = "note_table")
public class Note {
    // Room에서 id에 자동 ID를 할당하기 위해 autoGenerate를 true로 설정.
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
