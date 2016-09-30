package com.kodonho.android.notepad;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<RecyclerData> datas = new ArrayList<>();
    RecyclerCardAdapter adapter;

    // 플로팅버튼 생성
    FloatingActionButton fab;
    FloatingActionButton fabCancel;

    // 리스트용 레이아웃
    RelativeLayout contentMain;
    // 쓰기용 레이아웃
    RelativeLayout contentWrite;

    Button btnCancel;
    Button btnOk;

    EditText et;

    RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et = (EditText) findViewById(R.id.editText);

        contentMain = (RelativeLayout) findViewById(R.id.layoutMain);
        contentWrite = (RelativeLayout) findViewById(R.id.layoutWrite);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                if(mode == MODE_WRITE) {
                    callWrite(-1);
                }else{
                    saveData();
                }
            }
        });

        fabCancel = (FloatingActionButton) findViewById(R.id.fabCancel);
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
                toggleFab();
            }
        });

        listView = (RecyclerView) findViewById(R.id.recylerCardView);
        adapter = new RecyclerCardAdapter(datas, R.layout.activity_recycler_card_item, this);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    // 쓰기 레이아웃을 호출한다
    public void callWrite(int position){
        contentWrite.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(Main2Activity.this, android.R.anim.fade_in);
        contentWrite.startAnimation(animation);
        et.requestFocus();
        keyBoardOn();

        // 쓰기 레이아웃을 호출하는데 position 값이 있으면
        // 리스트에서 호출한것으로 간주하고 텍스트필드에 해당값을 입력해준다
        // 즉 글 읽기모드를 대신한다
        if(position > -1){
            et.setText(datas.get(position).contents);
        }
    }

    // 데이터를 저장하는 함수
    public void saveData(){
        RecyclerData data = new RecyclerData();
        data.contents = et.getText().toString();
        datas.add(data);
        adapter.notifyDataSetChanged();
        Toast.makeText(Main2Activity.this, "저장하였습니다", Toast.LENGTH_LONG).show();
        goMain();
    }

    static final int MODE_WRITE = 0;
    static final int MODE_LIST = 1;
    static int mode = MODE_LIST;
    public void toggleFab(){
        //@android:drawable/ic_menu_edit
        //@android:drawable/ic_menu_save
        switch(mode){
            case MODE_LIST:
                mode = MODE_WRITE;
                fab.setImageResource(android.R.drawable.ic_menu_save);
                fabCancel.setVisibility(View.VISIBLE);
                break;
            case MODE_WRITE:
                mode = MODE_LIST;
                fab.setImageResource(android.R.drawable.ic_menu_edit);
                fabCancel.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(mode == MODE_WRITE) {
            goMain();
            toggleFab();
        }
    }

    public void goMain(){
        et.setText("");
        keyBoardOff(et);
        fab.setVisibility(View.VISIBLE);
        contentWrite.setVisibility(View.GONE);
    }

    // 키보드를 강제로 내리는 함수
    // EditText 에서 다른 버튼을 클릭해도 focus의 변화가 없으므로
    // 이 함수가 필요하다
    public void keyBoardOff(EditText et){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(et.getWindowToken() != null)
            imm.hideSoftInputFromWindow(et.getWindowToken(),0);
    }

    // 키보드를 강제로 올려주는 함수
    // 쓰기모드로 전환시 호출하면 소프트키보드를 보여준다
    public void keyBoardOn(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
