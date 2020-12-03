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
    RecyclerView recyclerView;
    NodeAdapter adapter;

    Context context;
    OnSelectedListener listener;

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
        if (context instanceof OnSelectedListener) {
            listener = (OnSelectedListener) context;
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

    public interface OnSelectedListener {
        void onTabSelected(int position);
        void showFragment2(Node item);
    }

    private void initUI(ViewGroup rootView) {
        Button todayWriteButton = rootView.findViewById(R.id.newButton);
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

        adapter = new NodeAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnNodeClickListener() {
            @Override
            public void onItemClick(NodeAdapter.ViewHolder holder, View view, int position) {
                Node item = adapter.getItem(position);
                //Log.d(TAG, "아이템 선택됨 : " + item.get_id());
                if (listener != null) {
                    listener.showFragment2(item);
                }
            }
        });
    }

    public int loadNoteListData() {
        String sql = "select _id, TITLE, CONTENTS, CREATE_DATE, MODIFY_DATE from " + NodeDatabase.TABLE_NODE + " order by CREATE_DATE desc";

        int recordCount = -1;
        NodeDatabase database = NodeDatabase.getInstance(context);
        if (database != null) {
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();

            ArrayList<Node> items = new ArrayList<Node>();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String title = outCursor.getString(1);
                String contents = outCursor.getString(2);
                String dateStr = outCursor.getString(3);
                String createDateStr = null;
                if (dateStr != null && dateStr.length() > 5) {
                    try {
                        Date inDate = Format.dateFormat2.parse(dateStr);
                        createDateStr = Format.dateFormat1.format(inDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    createDateStr = "";
                }
                Format.println("#" + i + " -> " + _id + ", " + title + ", " + contents + ", " + createDateStr);
                items.add(new Node(_id, title, contents, createDateStr));
            }
            outCursor.close();
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
        return recordCount;
    }
}