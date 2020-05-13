package com.example.mvvmarchitecture;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// RoomDatabase는 모든 조각을 하나로 묶고 Entity들을 해당 DAO에 연결하는 추상 클래스이다.
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if(instance == null) {
            // databaseBuilder를 사용하여 정적 싱글톤 형태로 database를 생성하며, 여기서 database 클래스와 파일 이름을 전달 해야함.
            // 버전을 올린다면 fallbackToDestructiveMigration를 통해 database를 재생성할 수 있음.
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}