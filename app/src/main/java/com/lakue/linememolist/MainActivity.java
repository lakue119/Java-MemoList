package com.lakue.linememolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv_memolist;
    AdapterRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_memolist = findViewById(R.id.rv_memolist);

        init();
        getData();

    }

    private void init(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_memolist.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerView();
        rv_memolist.setAdapter(adapter);
    }

    private void getData(){
        DataMemo data = new DataMemo("아이언맨","dadsaf");
        adapter.addItem(data);
        data = new DataMemo("아이언맨","dadsaf");
        adapter.addItem(data);
        data = new DataMemo("아이언맨","dadsaf");
        adapter.addItem(data);
        data = new DataMemo("아이언맨","dadsaf");
        adapter.addItem(data);
        data = new DataMemo("아이언맨","dadsaf");
        adapter.addItem(data);
        data = new DataMemo("아이언맨","dadsaf");
        adapter.addItem(data);
    }
}
