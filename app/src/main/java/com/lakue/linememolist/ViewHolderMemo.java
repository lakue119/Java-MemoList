package com.lakue.linememolist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderMemo extends RecyclerView.ViewHolder {

    TextView tv_title, tv_content;
    ImageView iv_img;


    public ViewHolderMemo(@NonNull View itemView) {
        super(itemView);

        tv_title = itemView.findViewById(R.id.tv_title);
        tv_content = itemView.findViewById(R.id.tv_content);
        iv_img = itemView.findViewById(R.id.iv_img);

    }

    public void onBind(DataMemo data){
        tv_title.setText(data.getTitle());
        tv_content.setText(data.getContent());
        //iv_movie.setImageResource(data.getImgs().get(0));
    }
}