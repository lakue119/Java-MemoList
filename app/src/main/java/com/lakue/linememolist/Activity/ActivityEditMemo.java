package com.lakue.linememolist.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lakue.linememolist.Adapter.AdapterGridView;
import com.lakue.linememolist.R;
import com.lakue.linememolist.View.ExpandableHeightGridView;

public class ActivityEditMemo extends AppCompatActivity {

    ExpandableHeightGridView gv_memo_item;

    AdapterGridView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        gv_memo_item = findViewById(R.id.gv_memo_item);

        adapter = new AdapterGridView(this);

        gv_memo_item.setExpanded(true);
        gv_memo_item.setAdapter(adapter);

        setData();

    }

    private void setData() {
        String item;
        for (int i = 0; i < 11; i++) {

            if (i % 2 == 0) {
                item = "https://cdn.crewbi.com/images/goods_img/20180906/301986/301986_a_500.jpg?v=1536196415";
            } else {
                item = "https://seoul-p-studio.bunjang.net/product/60478633_1_1479473294_w640.jpg";
            }

            adapter.addItem(item);
        }
        adapter.notifyDataSetChanged();
    }
}
