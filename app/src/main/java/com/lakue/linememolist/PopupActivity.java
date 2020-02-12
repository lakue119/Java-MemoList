//package com.lakue.linememolist;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class PopupActivity extends AppCompatActivity {
//
//    TextView tv_title;
//    TextView tv_content;
//    Button btn_left;
//    Button btn_right;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_popup);
////타이틀바 없애기
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //데이터 가져오기
//        Intent intent = getIntent();
//        int type = intent.getIntExtra("type",0);
//
//        String title = "";
//        String content = "";
//        String buttonLeft = "";
//        String buttonRight = "";
//
//        title = intent.getStringExtra("title");
//        content = intent.getStringExtra("content");
//        buttonLeft = intent.getStringExtra("buttonLeft");
//        buttonRight = intent.getStringExtra("buttonRight");
//
//        //UI 객체생성
//        tv_title = (TextView)findViewById(R.id.tv_title);
//        tv_content = (TextView)findViewById(R.id.tv_content);
//        btn_left = (Button)findViewById(R.id.btn_left);
//        btn_right = (Button)findViewById(R.id.btn_right);
//
//        tv_title.setText(title);
//        tv_content.setText(content);
//        btn_left.setText(buttonLeft);
//        btn_right.setText(buttonRight);
//
//        btn_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //데이터 전달하기
//                Intent intent = new Intent();
//                intent.putExtra("result", "Right Popup Click");
//                setResult(RESULT_OK, intent);
//
//                //액티비티(팝업) 닫기
//                finish();
//            }
//        });
//
//        btn_left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //데이터 전달하기
//                Intent intent = new Intent();
//                intent.putExtra("result", "Left Popup Click");
//                setResult(RESULT_OK, intent);
//
//                //액티비티(팝업) 닫기
//                finish();
//            }
//        });
//    }
//}
