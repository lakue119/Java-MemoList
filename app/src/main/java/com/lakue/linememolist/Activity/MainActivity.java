package com.lakue.linememolist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.linememolist.Adapter.AdapterRecyclerView;
import com.lakue.linememolist.Listener.OnItemClickListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.DataMemoImg;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.Module.ModuleActivity;
import com.lakue.linememolist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends ModuleActivity {

    @BindView(R.id.rv_memolist)
    RecyclerView rv_memolist;
    @BindView(R.id.btn_edit_move)
    Button btn_edit_move;

    private AdapterRecyclerView adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this,this);


        init();
        //getData();

        btn_edit_move.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditMemoActivity.class);
                intent.putExtra("type",Common.TYPE_INTENT_INSERT);
                startActivityForResult(intent, 1001);
            }
        });

    }

    private void init(){
        //recyclerview 초기화
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_memolist.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerView(Common.RECYCLER_TYPE_MEMO_LIST);
        rv_memolist.setAdapter(adapter);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        getResultMemoList();
        Log.e("MainActivityLog", "realm address : " + realm.getPath());

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(long memo_idx) {
                Intent intent = new Intent(getBaseContext(),MemoDetailActivity.class);
                intent.putExtra("memo_idx",memo_idx);
                startActivityForResult(intent,Common.REQUEST_REFRESH_MEMO);
            }
        });


    }

    private void getResultMemoList(){
        RealmResults<DataMemo> realmResults = realm.where(DataMemo.class)
                .findAllAsync();

        realmResults = realmResults.sort("idx");

        for(DataMemo memo : realmResults) {
            DataMemo data =new DataMemo(memo.getIdx(),memo.getTitle(),memo.getContent(),memo.getThumbnail());
            Log.i("AJKRJK",data.toString());
            adapter.addItem(data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            adapter.removeItem();
            getResultMemoList();
            adapter.notifyDataSetChanged();
        }
    }
}
