package com.example.mydiary;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class Fragment1 extends Fragment {
    //여러개의 Node를 표시할 recyclerView 선언
    RecyclerView recyclerView;
    //recyclerView에 Node를 붙이는데 도움을 주는 adapter 선언
    NodeAdapter adapter;

    //이벤트 발생 시 instance, listener 선언
    Context context;
    OnSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //activiti_main의 container에 Fragment를 부착시킴
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
        //초기화면 구성
        initUI(rootView);
        //데이터로딩
        loadNoteListData();
        return rootView;
    }

    @Override
    //Fragment 생성시 가장 처음에 호출되는 매소드로 Fragment를 Activity에 부착시킴
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        //context가 OnSelectedListener 속하는지 검사함
        if (context instanceof OnSelectedListener) {
            //속할 경우 context값을 listener에 넣음
            listener = (OnSelectedListener) context;
        }
    }

    @Override
    //Fragment 생성시 가장 마지막에 생성되는 매소드로 Fragment를 Activity에 탈착시킴
    public void onDetach() {
        super.onDetach();
        if (context != null) {
            //context, listener 초기화
            context = null;
            listener = null;
        }
    }

    //초기회면 구성(+ New button과 cycle item)
    private void initUI(ViewGroup rootView) {
        //New button xml과 매핑
        Button newButton = rootView.findViewById(R.id.newButton);
        //New button 클릭 했을 때 이벤트 발생
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener가 null이 아니면 onTabSelected 호출
                if (listener != null) {
                    listener.onTabSelected(1);
                }
            }
        });

        //recycler xml과 매핑
        recyclerView = rootView.findViewById(R.id.recyclerView);

        //recycler과 함께 사용할 lLinearLayoutManager 선언
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //recycler과 매핑
        recyclerView.setLayoutManager(layoutManager);

        //recycler에 item을 추가할 NodeAdapter 생성
        adapter = new NodeAdapter();
        //recycler에 adapter 부착
        recyclerView.setAdapter(adapter);
        //adapter를 클릭했을 때 이벤트 발생
        adapter.setOnNodeClickListener(new OnNodeClickListener() {
            @Override
            public void onItemClick(NodeAdapter.ViewHolder holder, View view, int position) {
                //Node 생성, position은 Node의 위치
                Node item = adapter.getItem(position);
                //listener가 null이 아니면 onTabSelected 호출
                if (listener != null) {
                    listener.showFragment2(item);
                }
            }
        });
    }

    //node 데이터를 DB에서 불러옴
    public int loadNoteListData() {
        String sql = "select _id, TITLE, CONTENTS, CREATE_DATE, MODIFY_DATE from " + NodeDatabase.TABLE_NODE + " order by CREATE_DATE desc";

        int recordCount = -1;
        //context instance 호출
        NodeDatabase database = NodeDatabase.getInstance(context);
        if (database != null) {
            //DB select SQL문 실행
            Cursor outCursor = database.rawQuery(sql);
            //recordCount= 저장한 데이터의 개수
            recordCount = outCursor.getCount();

            //Node items 제너럴 배열 생성
            ArrayList<Node> items = new ArrayList<Node>();

            //저장한 데이터 개수까지 반복문
            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();
                //id, titile, contents, dateStr 값을 호출
                int _id = outCursor.getInt(0);
                String title = outCursor.getString(1);
                String contents = outCursor.getString(2);
                String dateStr = outCursor.getString(3);
                String createDateStr = null;
                if (dateStr != null && dateStr.length() > 5) {
                    try {
                        //Date 날짜 Format 2번 사용
                        Date inDate = Format.dateFormat2.parse(dateStr);
                        //createDateStr 날짜 Format 1번 사용
                        createDateStr = Format.dateFormat1.format(inDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //날짜 값 "" 변경
                    createDateStr = "";
                }
                Format.println("#" + i + " -> " + _id + ", " + title + ", " + contents + ", " + createDateStr);
                //Node에 번호, 제목, 내용, 날짜를 붙여 넣음
                items.add(new Node(_id, title, contents, createDateStr));
            }
            //outCursor object 닫음
            outCursor.close();
            //adapter에 item추가
            adapter.setItems(items);
            //adapter에 날짜 변경
            adapter.notifyDataSetChanged();
        }
        return recordCount;
    }
}