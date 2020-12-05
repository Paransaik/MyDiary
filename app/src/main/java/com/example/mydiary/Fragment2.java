package com.example.mydiary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Date;

public class Fragment2 extends Fragment {
    //이벤트 발생 시 instance, listener 선언
    Context context;
    OnSelectedListener listener;
    OnRequestListener requestListener;

    //Node구성에 필요한 dateTextView, titleInput, contentsInput 선언
    TextView dateTextView;
    EditText titleInput;
    EditText contentsInput;

    //저장 버튼을 누를 때 상태를 나타내는 플레그 값
    int mMode = Format.MODE_INSERT;

    //item 선언
    Node item;

    @Override
    //Fragement 사용
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //activiti_main의 container에 Fragment를 부착시킴
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_2, container, false);
        //초기화면 구성
        initUI(rootView);
        //Node(Item)을 적용시킴
        applyItem();
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
        //context가 OnRequestListener 속하는지 검사함
        if (context instanceof OnRequestListener) {
            //속할 경우 context값을 listener에 넣음
            requestListener = (OnRequestListener) context;
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

    //OnRequestListener interface
    public interface OnRequestListener {
        void onRequest(String command);
    }

    //저장, 삭제, 닫기 버튼 구현
    private void initUI(ViewGroup rootView) {
        //NEW 버튼으로 새로운 Node를 구현할 때 필요한 날짜, 제목, 내용 선언
        dateTextView = rootView.findViewById(R.id.dateTextView);
        titleInput = rootView.findViewById(R.id.titleInput);
        contentsInput = rootView.findViewById(R.id.contentsInput);

        //저장 버튼
        Button saveButton = rootView.findViewById(R.id.saveButton);
        //저장 버튼을 눌렀을 때
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 mMode 값이 MODE_INSERT 이면
                if(mMode == Format.MODE_INSERT) {
                    //node 저장
                    saveNode();
                //현재 mMode 값이 MODE_MODIFY 이면
                } else if(mMode == Format.MODE_MODIFY) {
                    //node 수정
                    modifyNode();
                }
                //listener가 null이 아니면 onTabSelected 호출
                if (listener != null) {
                    listener.onTabSelected(0);
                }
            }
        });

        //삭제 버튼
        Button deleteButton = rootView.findViewById(R.id.deleteButton);
        //삭제 버튼을 눌렀을 때
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteNode() 호출
                deleteNode();
                //listener가 null이 아니면 onTabSelected 호출
                if (listener != null) {
                    listener.onTabSelected(0);
                }
            }
        });

        //닫기 버튼
        Button closeButton = rootView.findViewById(R.id.closeButton);
        //닫기 버튼 눌렀을 때
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener가 null이 아니면 onTabSelected 호출
                if (listener != null) {
                    listener.onTabSelected(0);
                }
            }
        });
    }

    //Node를 저장하는 매소드
    public void applyItem() {
        //아이템이 NULL이 아니면
        if (item != null) {
            //mMode값 MODE_MODIFY로 설정
            mMode = Format.MODE_MODIFY;

            //현재 날짜 저장하는 매소드
            setDateString(item.getCreateDateStr());
            //제목으로 저장하는 매소드
            setTitle(item.getTitle());
            //내용으로 저장하는 매소드
            setContents(item.getContents());

        } else {
            //아이템이 NULL이면
            //mMode값 MODE_INSERT로 설정
            mMode = Format.MODE_INSERT;

            //날짜 Date 생성
            Date currentDate = new Date();
            //Format1로 날짜 Format 설정
            String currentDateString = Format.dateFormat1.format(currentDate);
            //날짜값을 오늘 날짜로 변경
            setDateString(currentDateString);

            //title, contents 값은 공백으로 설정
            titleInput.setText("");
            contentsInput.setText("");
        }
    }

    //생성자 설정
    public void setItem(Node item) {
        this.item = item;
    }
    public void setDateString(String dateString) {
        dateTextView.setText(dateString);
    }
    public void setTitle(String data) { titleInput.setText(data); }
    public void setContents(String data) {
        contentsInput.setText(data);
    }

    /**
     * 데이터베이스
     */
    //DB 레코드 추가
    private void saveNode() {
        //title, contents 선언과 초기화
        String title = titleInput.getText().toString();
        String contents = contentsInput.getText().toString();
        //inset node query
        String sql = "insert into " + NodeDatabase.TABLE_NODE +
                "(TITLE, CONTENTS) values(" +
                "'"+ title + "', " +
                "'"+ contents + "')";

        //현재 database instance에 적용
        NodeDatabase database = NodeDatabase.getInstance(context);
        //DB 종료
        database.execSQL(sql);
    }

    //DB 레코드 수정
    private void modifyNode() {
        if (item != null) {
            //title, contents 선언과 초기화
            String title = titleInput.getText().toString();
            String contents = contentsInput.getText().toString();
            //update node query
            String sql = "update " + NodeDatabase.TABLE_NODE +
                    " set " +
                    " TITLE = '" + title + "'" +
                    " ,CONTENTS = '" + contents + "'" +
                    " where " +
                    " _id = " + item._id;

            //현재 database instance에 적용
            NodeDatabase database = NodeDatabase.getInstance(context);
            //DB 종료
            database.execSQL(sql);
        }
    }

    //DB 레코드 삭제
    private void deleteNode() {
        if (item != null) {
            //delete node query
            String sql = "delete from " + NodeDatabase.TABLE_NODE +
                    " where " +
                    " _id = " + item._id;

            //현재 database instance에 적용
            NodeDatabase database = NodeDatabase.getInstance(context);
            //DB 종료
            database.execSQL(sql);
        }
    }
}