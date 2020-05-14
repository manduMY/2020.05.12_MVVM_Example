package com.example.mvvmarchitecture;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // Repository를 정의하고 RoomDatabase에 RoomDatabase.Callback 메서드, PopulateDbAsyncTask 클래스를 정의하고 databaseBuilder에 roomCallback 객체 추가.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        // 1. 빈 테이블로 시작하지 않도록 데이터베이스를 onCreate 방법으로 채워넣어(populate) databaseBuilder로의 콜백을 추가한다.
        // 2. 또한 RoomDatabase가 열릴 때마다 코드를 실행하길 원한다면 onOpen을 override할 수 있다.
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        // 1. 안드로이드에서의 일처리는 메인스레드(UI 스레드)가 담당한다. 특히 UI와 관련된( ex) TextView,ImageView )
        // 2. 일처리는 메인스레드만 담당 하게끔 보통 설계를 한다. 그래서 메인스레드를 UI스레드라고도 불린다.
        // 3. 따라서 복잡한 계산은 백그라운드 스레드( 메인 스레드가 아닌 다른 스레드의 총칭)에 맡긴후
        // 계산된 결과값을 UI스레드에게 일을 시켜야 하므로 AsyncTask 클래스를 만들어 이를 처리한다.

        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // 1. 해당 메서드가 background 스레드로 일처리를 해주는 곳이다.
            // 2. 보통 네트워크, 병행 일처리등을 이 공간에 작성한다.
            // 3. 중요한건 스레드 이므로 UI스레드가 어떤 일을 하고 있는지 상관없이
            //    별개의 일을 진행한다는 점이다. 따라서 AysncTask는 비동기적으로 작동한다.
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));

            return null;
        }
    }
}