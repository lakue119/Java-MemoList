package com.lakue.linememolist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.linememolist.Listener.OnImageDeleteListener;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.R;
import com.lakue.linememolist.ViewHolder.ViewHolderGridEditItem;
import com.lakue.linememolist.ViewHolder.ViewHolderMemo;

import java.util.ArrayList;

public class AdapterGrid extends RecyclerView.Adapter<MyItemView> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Object> listData = new ArrayList<>();
    OnImageInsertListener onImageInsertListener;
    OnImageDeleteListener onImageDeleteListener;
    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editmemo_img, parent, false);
        return new ViewHolderGridEditItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        ViewHolderGridEditItem viewHolderGridEditItem = (ViewHolderGridEditItem) holder;

        if (position == listData.size()) {
            viewHolderGridEditItem.onBind(position);
            viewHolderGridEditItem.setOnImageInsertListener(new OnImageInsertListener() {
                @Override
                public void onImageInsert() {
                    onImageInsertListener.onImageInsert();
                }
            });
        } else {
            viewHolderGridEditItem.onBind(listData.get(position), position);
            viewHolderGridEditItem.setOnImageDeleteListener(new OnImageDeleteListener() {
                @Override
                public void onImageDelete(int position) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size()+1;
    }

    public void removeItem(){
        listData.clear();
    }

    public void addItem(Object data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
        notifyItemChanged(listData.size()-1);
    }

    public void setOnImageInsertListener(OnImageInsertListener onImageInsertListener) {
        this.onImageInsertListener = onImageInsertListener;
    }

    public void setOnImageDeleteListener(OnImageDeleteListener onImageDeleteListener) {
        this.onImageDeleteListener = onImageDeleteListener;
    }
}