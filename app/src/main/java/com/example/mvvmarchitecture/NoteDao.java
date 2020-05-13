package com.example.mvvmarchitecture;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// DAO(Data Access Object)
// Database의 data에 접근하기 위한 객체, Database에 접근을 하기위한 로직과 비즈니스 로직을 분리하기 위해 사용
// 여기서는 SQLite와 데이터를 서로 접근하기 위해 사용. 그리고 우리가 하고 싶은 모든 데이터베이스 운영을 정의하는 인터페이스.
// NoteDao는 데이터베이스 테이블의 행 or row or 레코드가 변경되는 즉시 (View)Activity나 Fragment에 알려준다.

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}