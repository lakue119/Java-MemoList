package com.lakue.linememolist.Model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmObject;

public class MyItemView extends RecyclerView.ViewHolder {
    public MyItemView(@NonNull View itemView) {
        super(itemView);
    }
    public void onBind(RealmObject data){

    }
}
