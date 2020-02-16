package com.lakue.linememolist.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lakue.linememolist.Adapter.AdapterRecyclerView;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.DataMemoImg;
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
                startActivityForResult(intent, 1001);
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

        getResultMemoList();
        getResultMemoImgs();
        Log.e("MainActivityLog", "realm address : " + realm.getPath());


    }

    private void getResultMemoList(){
        RealmResults<DataMemo> realmResults = realm.where(DataMemo.class)
                .findAllAsync();

        for(DataMemo memo : realmResults) {
            DataMemo data =new DataMemo(memo.getIdx(),memo.getTitle(),memo.getContent());
            Log.i("AJKRJK",data.toString());
            adapter.addItem(data);
        }
    }

    private void getResultMemoImgs(){
        RealmResults<DataMemoImg> realmResultImgs = realm.where(DataMemoImg.class)
                .findAllAsync();

        for(DataMemoImg memoImg : realmResultImgs) {
            DataMemoImg data =new DataMemoImg(memoImg.getImg_idx(),memoImg.getMemo_idx(),memoImg.getImg_file());
            //adapter.addItem(data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Toast.makeText(this, "등록완", Toast.LENGTH_SHORT).show();

            adapter.removeItem();
            getResultMemoList();
            adapter.notifyDataSetChanged();
        }
    }
}
