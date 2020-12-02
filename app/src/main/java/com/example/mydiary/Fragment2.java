package com.example.mydiary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {
    Context context;
    OnItemSelectedListener listener;
    OnRequestListener requestListener;
    TextView dateTextView;

    EditText titleInput;
    EditText contentsInput;

    int mMode = Format.MODE_INSERT;

    Node item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        }
        
        //새로운 Node 작성 시 기존에 작성한 Node 유무 검사
        if (context instanceof OnRequestListener) {
            requestListener = (OnRequestListener) context;
        }
    }

    @Override
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

}