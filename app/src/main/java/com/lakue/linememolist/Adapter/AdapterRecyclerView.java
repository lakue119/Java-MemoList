package com.lakue.linememolist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.linememolist.Listener.OnItemClickListener;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.R;
import com.lakue.linememolist.ViewHolder.ViewHolderMemo;
import com.lakue.linememolist.ViewHolder.ViewHolderMemoImage;

import java.util.ArrayList;

import io.realm.RealmObject;

public class AdapterRecyclerView extends RecyclerView.Adapter<MyItemView> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<RealmObject> listData = new ArrayList<>();
    int SEL_TYPE;

    Context context;

    OnItemClickListener onItemClickListener;

    public AdapterRecyclerView(int objtype){
        this.SEL_TYPE = objtype;
    }

    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        MyItemView holder = null;

        View view;

        switch (SEL_TYPE){
            case Common.RECYCLER_TYPE_MEMO_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
                holder = new ViewHolderMemo(view);
                break;
            case Common.RECYCLER_TYPE_MEMO_IMGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
                holder = new ViewHolderMemoImage(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        if(holder instanceof ViewHolderMemo){
            ViewHolderMemo viewHolderMemo = (ViewHolderMemo) holder;
            viewHolderMemo.onBind(listData.get(position));
            viewHolderMemo.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(long memo_idx) {
                    onItemClickListener.onItemClick(memo_idx);
                }
            });
        } else if(holder instanceof ViewHolderMemoImage){
            ViewHolderMemoImage viewHolderMemoImage = (ViewHolderMemoImage) holder;
            viewHolderMemoImage.onBind(listData.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void removeItem(){
        listData.clear();
    }

    public void addItem(RealmObject data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}