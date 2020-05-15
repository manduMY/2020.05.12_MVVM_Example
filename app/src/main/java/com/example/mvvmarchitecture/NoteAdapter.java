package com.example.mvvmarchitecture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// 1. Recycler View 아이템 추가, 수정, 삭제하기 위해 Adapter를 만들었습니다.
// 2. 사용자 데이터 리스트로부터 아이템 뷰를 만드는 것이 어댑터가 하는 역할입니다.
// 3. 어댑터를 통해 만들어진 각 아이템 뷰는 "뷰홀더(ViewHolder)"객체에 저장되어 화면에 표시되고, 필요에 따라 생성 또는 재활용(Recycle)됩니다.
// 4. ViewHolder란 화면에 표시될 아이템 뷰를 저장하는 객체입니다.
// 5. 어댑터에 의해 관리되며 필요에 따라 어댑터에서 생성.
//    물론, 미리 생성된 뷰홀더 객체가 있는 경우에는 새로 생성하지 않고 이미 만들어져 있는 뷰홀더를 재활용하는데,
//    이 때는 단순히 데이터가 뷰홀더의 아이템 뷰에 바인딩(Binding) 됩니다.

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        // 생성된 뷰홀더에 데이터를 바인딩 해주는 함수이다.
        Note currentNote = notes.get(position);
        holder.textviewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        // Recycler View 갱신
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textviewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(View itemView) {
            super(itemView);
            textviewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // position이 없으면 -1을 반환함.
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    // 편집기능 추가하기 -Adapter
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}