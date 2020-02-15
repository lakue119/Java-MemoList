package com.lakue.linememolist.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lakue.linememolist.R;
import com.lakue.linememolist.ViewHolder.ViewHolderGridEditItem;

import java.util.ArrayList;

public class AdapterGridView extends BaseAdapter {

    Context context;
    LayoutInflater inf;

    private ArrayList<String> myItems = new ArrayList<>();


//    OnChangeFragmentListener onChangeFragmentListener;
//    OnAddCartListener onAddCartListener;

    public AdapterGridView(Context context) {
        this.context = context;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myItems.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return myItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String data) {
        // 외부에서 item을 추가시킬 함수입니다.
        myItems.add(data);
        notifyDataSetInvalidated();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(R.layout.item_editmemo_img, null);

        if(position == myItems.size()){
            ViewHolderGridEditItem viewHolderGridEventDetail = new ViewHolderGridEditItem(context,convertView);
        } else{
            ViewHolderGridEditItem viewHolderGridEventDetail = new ViewHolderGridEditItem(context,convertView);
            viewHolderGridEventDetail.onBind(myItems.get(position));
        }


//        viewHolderGridEventDetail.setOnAddCartListener(new OnAddCartListener() {
//            @Override
//            public void onAddCart(Boolean isOption, int idx, int sku_id, Bundle args) {
//                onAddCartListener.onAddCart(isOption,idx,sku_id,args);
//            }
//        });

        return convertView;
    }

    //    public void removeItem(){
//        myItems.clear();
//    }
//
//    public void setOnAddCartListener(OnAddCartListener onAddCartListener) {
//        this.onAddCartListener = onAddCartListener;
//    }
//
//    public void setOnChangeFragmentListener(OnChangeFragmentListener onChangeFragmentListener) {
//        this.onChangeFragmentListener = onChangeFragmentListener;
//    }
}