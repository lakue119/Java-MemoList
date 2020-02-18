//package com.lakue.linememolist.Adapter;
//
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.lakue.linememolist.Listener.OnImageDeleteListener;
//import com.lakue.linememolist.Listener.OnImageInsertListener;
//import com.lakue.linememolist.R;
//import com.lakue.linememolist.ViewHolder.ViewHolderGridEditItem;
//
//import java.util.ArrayList;
//
//public class AdapterGridView extends BaseAdapter {
//
//    Context context;
//    LayoutInflater inf;
//
//    private ArrayList<Object> myItems = new ArrayList<>();
//
//    OnImageInsertListener onImageInsertListener;
//    OnImageDeleteListener onImageDeleteListener;
//
//    public AdapterGridView(Context context) {
//        this.context = context;
//        inf = (LayoutInflater) context.getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public int getCount() {
//        return myItems.size()+1;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return myItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public void addItem(Object data) {
//        // 외부에서 item을 추가시킬 함수입니다.
//        myItems.add(data);
//        notifyDataSetInvalidated();
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null)
//            convertView = inf.inflate(R.layout.item_editmemo_img, null);
//
//        if(position == myItems.size()){
//            ViewHolderGridEditItem viewHolderGridEventDetail = new ViewHolderGridEditItem(context,convertView);
//            viewHolderGridEventDetail.onBind(position);
//            viewHolderGridEventDetail.setOnImageInsertListener(new OnImageInsertListener() {
//                @Override
//                public void onImageInsert() {
//                    onImageInsertListener.onImageInsert();
//                }
//            });
//        } else{
//            ViewHolderGridEditItem viewHolderGridEventDetail = new ViewHolderGridEditItem(context,convertView);
//            viewHolderGridEventDetail.onBind(myItems.get(position),position);
//            viewHolderGridEventDetail.setOnImageDeleteListener(new OnImageDeleteListener() {
//                @Override
//                public void onImageDelete(int position) {
//
//                }
//            });
//        }
//
//        return convertView;
//    }
//
//
//    public void setOnImageInsertListener(OnImageInsertListener onImageInsertListener) {
//        this.onImageInsertListener = onImageInsertListener;
//    }
//
//    public void setOnImageDeleteListener(OnImageDeleteListener onImageDeleteListener) {
//        this.onImageDeleteListener = onImageDeleteListener;
//    }
//}