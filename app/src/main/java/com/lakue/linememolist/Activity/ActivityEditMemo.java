package com.lakue.linememolist.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lakue.linememolist.Adapter.AdapterGrid;
import com.lakue.linememolist.Adapter.AdapterRecyclerView;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.PopupActivity;
import com.lakue.linememolist.R;
import com.lakue.linememolist.View.ExpandableHeightGridView;

import java.io.Serializable;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ActivityEditMemo extends AppCompatActivity {

//    @BindView(R.id.gv_memo_item)
//    ExpandableHeightGridView gv_memo_item;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.btn_memo_insert)
    Button btn_memo_insert;
    @BindView(R.id.rv_memo_item)
    RecyclerView rv_memo_item;

    AdapterGrid adapter;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        ButterKnife.bind(this,this);

        init();

        setData();

        btn_memo_insert.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //Realm에 작성한 메모 저장
                addMemo();
            }
        });

    }

    private void init(){
        //recyclerview 초기화
        GridLayoutManager mGridLayoutManager;

        int numberOfColumns = 3; // 한줄에 5개의 컬럼을 추가합니다.
        mGridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        rv_memo_item.setLayoutManager(mGridLayoutManager);

        adapter = new AdapterGrid();
        rv_memo_item.setAdapter(adapter);


    }



    //이미지 저장
    private void setData() {
//        String item;
//        for (int i = 0; i < 1; i++) {
//
//            if (i % 2 == 0) {
//                item = "https://cdn.crewbi.com/images/goods_img/20180906/301986/301986_a_500.jpg?v=1536196415";
//            } else {
//                item = "https://seoul-p-studio.bunjang.net/product/60478633_1_1479473294_w640.jpg";
//            }
//
//            adapter.addItem(item);
//        }

        adapter.setOnImageInsertListener(new OnImageInsertListener() {
            @Override
            public void onImageInsert() {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                startActivityForResult(intent,1);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
            return;
        }

        if(data.getExtras().getInt("result",0) == Common.TYPE_ALBUM ||
                data.getExtras().getInt("result",0) == Common.TYPE_PHOTO);{
            byte[] img = data.getExtras().getByteArray("data");
            adapter.addItem(img);
        }
    }
}
