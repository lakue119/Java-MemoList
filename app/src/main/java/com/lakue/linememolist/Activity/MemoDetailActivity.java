package com.lakue.linememolist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.linememolist.Adapter.AdapterRecyclerView;
import com.lakue.linememolist.Listener.OnSingleClickListener;
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
    @BindView(R.id.btn_update)
    Button btn_update;
    @BindView(R.id.btn_delete)
    Button btn_delete;

    private AdapterRecyclerView adapter;

    Realm realm;
    Common common;

    long memo_idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        ButterKnife.bind(this, this);
        getIntentData();
        init();

        btn_update.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditMemoActivity.class);
                intent.putExtra("type",Common.TYPE_INTENT_UPDATE);
                intent.putExtra("memo_idx",memo_idx);
                startActivityForResult(intent,Common.REQUEST_UPDATE_MEMO);
            }
        });

        btn_delete.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<DataMemo> result = realm.where(DataMemo.class).equalTo("idx", memo_idx).findAll();
                        RealmResults<DataMemoImg> realmResultImgs = realm.where(DataMemoImg.class).equalTo("memo_idx", memo_idx).findAll();
                        result.deleteAllFromRealm();
                        realmResultImgs.deleteAllFromRealm();

                        common.showToast("메모가 삭제되었습니다.");

                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
            }
        });
    }

    private void getIntentData() {
        if (getIntent() != null) {
            this.memo_idx = getIntent().getExtras().getLong("memo_idx");
        }
    }

    private void init() {
        common = new Common(this);
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

    private void getResultMemoList(long memo_idx) {
        RealmResults<DataMemo> realmResults = realm.where(DataMemo.class)
                .equalTo("idx", memo_idx)
                .findAllAsync();

        tv_title.setText(realmResults.get(0).getTitle());
        tv_content.setText(realmResults.get(0).getContent());

    }

    private void getResultMemoImgs(long memo_idx) {
        RealmResults<DataMemoImg> realmResultImgs = realm.where(DataMemoImg.class)
                .equalTo("memo_idx", memo_idx)
                .findAllAsync();

        for (DataMemoImg memoImg : realmResultImgs) {
            Log.i("AJKRJK", "memoImg" + memoImg.toString());
            DataMemoImg data = new DataMemoImg(memoImg.getImg_idx(), memoImg.getMemo_idx(), memoImg.getImg_file());
            adapter.addItem(data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
