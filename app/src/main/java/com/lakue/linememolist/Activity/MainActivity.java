package com.lakue.linememolist.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lakue.linememolist.Adapter.AdapterRecyclerView;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_memolist;
    private AdapterRecyclerView adapter;
    private Realm realm;
    Button btn_edit_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rv_memolist = findViewById(R.id.rv_memolist);
        btn_edit_move = findViewById(R.id.btn_edit_move);
        init();
        //getData();

        btn_edit_move.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ActivityEditMemo.class);
                startActivity(intent);
            }
        });

    }

    private void init(){
        //recyclerview 초기화
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_memolist.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerView();
        rv_memolist.setAdapter(adapter);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        RealmResults<DataMemo> realmResults = realm.where(DataMemo.class)
                .findAllAsync();

        for(DataMemo memo : realmResults) {
            DataMemo data =new DataMemo(memo.getIdx(),memo.getTitle(),memo.getContent());
            adapter.addItem(data);
        }
    }

//    private void getData(){
//        DataMemo data = new DataMemo("아이언맨","dadsaf");
//        adapter.addItem(data);
//        data = new DataMemo("아이언맨","dadsaf");
//        adapter.addItem(data);
//        data = new DataMemo("아이언맨","dadsaf");
//        adapter.addItem(data);
//        data = new DataMemo("아이언맨","dadsaf");
//        adapter.addItem(data);
//        data = new DataMemo("아이언맨","dadsaf");
//        adapter.addItem(data);
//        data = new DataMemo("아이언맨","dadsaf");
//        adapter.addItem(data);
//    }
}
