package com.example.mydiary;

import android.view.View;

public interface OnNodeClickListener {
    void onItemClick(NodeAdapter.ViewHolder holder, View view, int position);
}
