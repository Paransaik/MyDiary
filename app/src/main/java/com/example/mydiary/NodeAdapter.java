package com.example.mydiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> implements OnNodeClickListener {
    //Node items 제너럴 배열 생성
    ArrayList<Node> items = new ArrayList<Node>();
    OnNodeClickListener listener;

    //아이템 개수 리턴
    static int position = 0;

    @NonNull
    //null을 허용하지 않을
    @Override
    //viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //XML에 미리 정의해둔 틀을 실제 메모리에 올려주는 줌
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //node_item을 itemView로 지정
        View itemView = inflater.inflate(R.layout.node_item, viewGroup, false);
        return new ViewHolder(itemView, this, position);
    }

    @Override
    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //item의 현재 포지션을 반환
        Node item = items.get(position);
        //viewHolder에 item을 등록시킴
        viewHolder.setItem(item);
    }

    //접근자, 생성자 선언
    @Override
    //전체 아이템 개수 리턴
    public int getItemCount() {
        return items.size();
    }

    //현재 position 리턴
    public Node getItem(int position) {
        return items.get(position);
    }

    //Node arrayList의 item을 리턴
    public void setItems(ArrayList<Node> items) {
        this.items = items;
    }

    public void setOnNodeClickListener(OnNodeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        //listener가 null이 아니면 onItemClick 호출
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //container, contentsTextView, dateTextView 선언
        LinearLayout container;
        TextView contentsTextView;
        TextView dateTextView;

        public ViewHolder(View itemView, final OnNodeClickListener listener, int layoutType) {
            super(itemView);
            //container을 xml에 매핑시킴
            container = itemView.findViewById(R.id.container);
            //container가 화면에 뿌려짐
            container.setVisibility(View.VISIBLE);
            //contentsTextView을 xml에 매핑시킴
            contentsTextView = itemView.findViewById(R.id.contentsTextView);
            //dateTextView을 xml에 매핑시킴
            dateTextView = itemView.findViewById(R.id.dateTextView);

            //itemView가 클릭시 이벤트 발생
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //NodeAdapter의 position값을 getAdapterPosition로 저장
                    NodeAdapter.position = getAdapterPosition();
                    //listener가 null이 아니면 onItemClick 호출
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Node item) {
            //노드를 저장할 때 보여지는 부분을 수정
            contentsTextView.setText(item.getTitle());
            dateTextView.setText(item.getCreateDateStr());
        }

    }
}