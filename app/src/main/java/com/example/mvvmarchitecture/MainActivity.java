package com.example.mvvmarchitecture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Activity에서는 시스템이 ViewModelProviders를 호출하여 올바른 ViewModel 인스턴스를 제공하도록 하고, 여기서 우리는 이 ViewModel의 라이프사이클의 범위를 지정해야 하는 Activity 또는 Fragment를 통과시킨다.
        // Activity/Fragment가 파괴되면, ViewModel은 OnCleared 방법을 통해 메모리에서 제거될 것이다.
        if(viewModelFactory == null) {
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        }
        noteViewModel = new ViewModelProvider(this, viewModelFactory).get(NoteViewModel.class);

        // onCreate 메서드에서 ViewModel에 저장된 LiveData를 검색하고 관찰을 호출하여 LifecycleOwner 및 Observer를 통과해야한다.
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // onChange 콜백에서는 해당 데이터베이스 테이블의 무언가가 변경될 때마다 데이터에 대한 업데이트를 받는다.
                // LiveData는 라이프사이클에서 적절한 시점에 자동으로 업데이트 전송을 시작 및 중지하고 사용되지 않는 참조를 정리한다.
                // update RecyclerView
                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
