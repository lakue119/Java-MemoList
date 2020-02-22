package com.lakue.linememolist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.linememolist.Listener.OnImageDeleteListener;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.R;
import com.lakue.linememolist.ViewHolder.ViewHolderGridEditItem;

import java.util.ArrayList;

public class AdapterGrid extends RecyclerView.Adapter<MyItemView> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<byte[]> listData = new ArrayList<>();

    private OnImageInsertListener onImageInsertListener;
    private OnImageDeleteListener onImageDeleteListener;

    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editmemo_img, parent, false);
        return new ViewHolderGridEditItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        ViewHolderGridEditItem viewHolderGridEditItem = (ViewHolderGridEditItem) holder;

        //마지막번째 뷰홀더는 이미지 추가버튼으로 생성
        if (position == listData.size()) {
            viewHolderGridEditItem.onBind();
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
                    onImageDeleteListener.onImageDelete(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    public void removeItem() {
        listData.clear();
    }

    //realm데이터에 넣을 메모리스트 이미지의 position번쨰 이미지 가져오기
    public byte[] getImage(int position) {
        if (listData.size() == 0) {
            return null;
        } else {
            return listData.get(position);
        }
    }

    // position번째 이미지 제거
    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    //이미지 추가
    public void addItem(byte[] data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
        notifyItemChanged(listData.size() - 1);
    }

    public void setOnImageInsertListener(OnImageInsertListener onImageInsertListener) {
        this.onImageInsertListener = onImageInsertListener;
    }

    public void setOnImageDeleteListener(OnImageDeleteListener onImageDeleteListener) {
        this.onImageDeleteListener = onImageDeleteListener;
    }
}