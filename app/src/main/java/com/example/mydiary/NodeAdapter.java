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
    ArrayList<Node> items = new ArrayList<Node>();
    OnNodeClickListener listener;

    static int position = 0;

    @NonNull

    //viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성.
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.node_item, viewGroup, false);

        return new ViewHolder(itemView, this, position);
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Node item = items.get(position);
        viewHolder.setItem(item);
    }

    //전체 아이템 갯수 리턴.
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Node item) {
        items.add(item);
    }

    public void setItems(ArrayList<Node> items) {
        this.items = items;
    }

    public Node getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnNodeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView contentsTextView;
        TextView dateTextView;

        public ViewHolder(View itemView, final OnNodeClickListener listener, int layoutType) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            container.setVisibility(View.VISIBLE);
            contentsTextView = itemView.findViewById(R.id.contentsTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NodeAdapter.position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Node item) {
            //노트를 저장할 때 보여지는 부분을 수정
            contentsTextView.setText(item.getTitle());
            dateTextView.setText(item.getCreateDateStr());
        }

    }
}