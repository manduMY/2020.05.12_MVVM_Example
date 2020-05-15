package com.example.mvvmarchitecture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// ViewModel은 UI 컨트롤러와 리포지토리 사이의 게이트웨이 역할을 한다.
// Activity/Fragment 대한 데이터를 저장하고 처리하며, 구성 변경에 구애받지 않으므로, 예를 들어 장치가 회전할 때 가변 상태를 잃지 않는다.
// AndroidViewModel을 확장하면 애플리케이션 Context에 대한 handle을 얻을 수 있으며, 이를 통해 RoomDatabase를 인스턴스화한다.
public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }
    public void insert(Note note) {
        repository.insert(note);
    }
    public void update(Note note) {
        repository.update(note);
    }
    public void delete(Note note) {
        repository.delete(note);
    }
    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}