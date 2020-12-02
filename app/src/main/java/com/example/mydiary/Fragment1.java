package com.example.mydiary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment1 extends Fragment {
    RecyclerView recyclerView;
    NodeAdapter adapter;

    Context context;
    OnRequestListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //activiti_main의 container에 Fragment를 부착시킴
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
        //초기화면 구성
        initUI(rootView);
        //
        loadNoteListData();
        return rootView;
    }

    @Override
    //Fragment 생성시 가장 처음에 호출되는 매소드로 Fragment를 Activity에 부착시킴
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnRequestListener) {
            listener = (OnRequestListener) context;
        }
    }

    @Override
    //Fragment 생성시 가장 마지막에 생성되는 매소드로 Fragment를 Activity에 탈착시킴
    public void onDetach() {
        super.onDetach();
        if (context != null) {
            context = null;
            listener = null;
        }
    }

    public interface OnRequestListener {
        public void onRequest(String command);
    }

    private void initUI(ViewGroup rootView) {
        Button todayWriteButton = rootView.findViewById(R.id.todayWriteButton);
        todayWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onTabSelected(1);
                }
            }
        });

        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnNoteItemClickListener() {
            @Override
            public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position) {
                Note item = adapter.getItem(position);
                //Log.d(TAG, "아이템 선택됨 : " + item.get_id());
                if (listener != null) {
                    listener.showFragment2(item);
                }
            }
        });
    }
}