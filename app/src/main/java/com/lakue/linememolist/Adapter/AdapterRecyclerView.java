package com.lakue.linememolist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.R;
import com.lakue.linememolist.ViewHolder.ViewHolderMemo;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<MyItemView> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<DataMemo> listData = new ArrayList<>();

    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
        return new ViewHolderMemo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        ((ViewHolderMemo)holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void removeItem(){
        listData.clear();
    }

    public void addItem(DataMemo data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
}