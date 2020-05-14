package com.example.mvvmarchitecture;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

// *Repository 패턴*
// 1. Repository는 앱의 나머지 부분으로부터 데이터 계층을 추상화하고 웹 서비스나 로컬 캐시와 같은 서로
//    다른 데이터 소스 간에 매개하는 Java 클래스다.
// 2. 다른 데이터베이스 작업(ex: SQLite Query)을 숨기고 ViewModel에 깨끗한 API를 제공한다.
// 3. Room은 메인 스레드에 대한 데이터베이스 Query를 허용하지 않으므로 비동기식으로 실행하기 위해 AsyncTasks를 사용한다.
// 4. LiveData는 Worker Thread에서 자동으로 가져오기 때문에 이것을 건드릴 필요는 없다.
// 5. Repository가 Room Database에서 가져온 데이터를 객체형식으로 보유한다.
public class NoteRepository {
    // Room에 있는 데이터를 삽입, 삭제, 수정하기 위함.
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        // Repository에서 Room에 있는 LiveData를 내림차순으로 가져온다.
        allNotes = noteDao.getAllNotes();
    }
    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // Background Thread에서 비동기적으로 Room에 삽입, 삭제 , 수정을 해주는 부분.
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private DeleteAllNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
