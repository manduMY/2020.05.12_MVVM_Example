package com.example.mvvmarchitecture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // *Recycler View*
        // 1. RecyclerView는 "많은 수의 데이터 집합을, 제한된 영역 내에서 유연하게(flexible) 표시할 수 있도록 만들어주는 위젯"입니다.
        // 2. Detail하게 말하자면 RecyclerView는 "사용자가 관리하는 많은 수의 데이터 집합(Data Set)을 개별 아이템 단위로 구성하여 화면에
        //    출력하는 뷰그룹(ViewGroup)이며, 한 화면에 표시되기 힘든 많은 수의 데이터를 스크롤 가능한 리스트로 표시해주는 위젯"입니다.
        // 3. 리스트 뷰의 경우 리스트 항목이 갱신될 때마다 매번 item view를 새로 구성해야 하는데 "많은 수의 데이터 집합을 표시"하는데 있어서
        //    성능 저하가 야기될 수 있습니다. 이러한 리스트뷰의 단점을 보완하기 위해 Recycler View는 아이템을 재활용 합니다.
        //    그리고 이를 위해 기본적으로 ViewHolder 패턴을 사용하도록 만들어 놓아야 합니다. ViewHolder는 필수 구현 사항
        // 4. ViewHolder를 통해 RecyclerView가 유연해집니다.
        // 5. DataList(ex. item1,item2,item3 ...) -> Adapter -> Recycler View(뿌려줌)
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // LayoutManager를 통해 DataList(item1, item2, item3 ...)를 수직으로 Layout 배치를 해줍니다.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // ViewHolder를 위해 adapter를 만듬.
        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

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
                adapter.setNotes(notes);
            }
        });
    }
}
