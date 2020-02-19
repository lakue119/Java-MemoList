package com.lakue.linememolist.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.lakue.linememolist.Adapter.AdapterRecyclerView;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.DataMemoImg;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MemoDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.rv_memo_image)
    RecyclerView rv_memo_image;

    private AdapterRecyclerView adapter;

    Realm realm;

    long memo_idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        ButterKnife.bind(this,this);
        getIntentData();
        init();
    }

    private void getIntentData(){
        if(getIntent() != null){
            this.memo_idx = getIntent().getExtras().getLong("memo_idx");
        }
    }

    private void init(){
        //recyclerview 초기화
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_memo_image.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerView(Common.RECYCLER_TYPE_MEMO_IMGE);
        rv_memo_image.setAdapter(adapter);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        getResultMemoList(memo_idx);
        getResultMemoImgs(memo_idx);
    }

    private void getResultMemoList(long memo_idx){
        RealmResults<DataMemo> realmResults = realm.where(DataMemo.class)
                .equalTo("idx",memo_idx)
                .findAllAsync();

        tv_title.setText(realmResults.get(0).getTitle());
        tv_content.setText(realmResults.get(0).getContent());

    }

    private void getResultMemoImgs(long memo_idx){
        RealmResults<DataMemoImg> realmResultImgs = realm.where(DataMemoImg.class)
                .equalTo("memo_idx",memo_idx)
                .findAllAsync();

        for(DataMemoImg memoImg : realmResultImgs) {
            Log.i("AJKRJK","memoImg" + memoImg.toString());
            DataMemoImg data =new DataMemoImg(memoImg.getImg_idx(),memoImg.getMemo_idx(),memoImg.getImg_file());
            adapter.addItem(data);
        }
    }
}
