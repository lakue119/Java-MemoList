package com.lakue.linememolist.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lakue.linememolist.Adapter.AdapterGridView;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.R;
import com.lakue.linememolist.View.ExpandableHeightGridView;

import java.io.Serializable;
import java.util.Date;

import io.realm.Realm;

public class ActivityEditMemo extends AppCompatActivity {

    ExpandableHeightGridView gv_memo_item;

    AdapterGridView adapter;

    Realm realm;

    EditText et_title, et_content;
    Button btn_memo_insert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        gv_memo_item = findViewById(R.id.gv_memo_item);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        btn_memo_insert = findViewById(R.id.btn_memo_insert);

        initRealm();

        adapter = new AdapterGridView(this);

        gv_memo_item.setExpanded(true);
        gv_memo_item.setAdapter(adapter);

        setData();

        btn_memo_insert.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //Realm에 작성한 메모 저장
                addMemo();
            }
        });

    }

    //이미지 저장
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

    //Realm에 객체(데이터) 저장
    private void addMemo(){

        //기본키인 idx를 고유값으로 지정
        Number number = realm.where(DataMemo.class).max("idx");
        long memoId = number == null ? 0 : number.longValue() + 1;

        final DataMemo memo = new DataMemo(memoId, et_title.getText().toString(), et_content.getText().toString());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Realm에 생성한 메모를 저장
                realm.copyToRealm(memo);

                //데이터 저장 후 MainActivity에 완료를 알림
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }

    //Realm 초기화
    private void initRealm(){
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }
}
