package com.example.minsu1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
//import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
//import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// 수정할수 있도록 값을 받는 페이지/틀
public class updateActivity extends AppCompatActivity {
    EditText product_name_input, product_time_input, product_menu_input,product_address_input;
    //Spinner product_Space_input;
    Button update_button, delete_button;
    String id,name,time,menu,address;
    ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        //spinner();// space칸에 대한 spinner목록 함수

        //Delete_Button();
        Update_Button();
        Cancle_Button(); // 뒤로가기 버튼

        getAndSetIntendData();

        ActionBar ab = getSupportActionBar(); // 페이지의 이름을 name으로 바꿈
        if(ab != null) {
            ab.setTitle(name);
        }
    }

    //데이터 삽입
    void getAndSetIntendData(){
        product_name_input = findViewById(R.id.et_1);
        product_time_input = findViewById(R.id.et_2);
        product_menu_input = findViewById(R.id.et_3);
        product_address_input = findViewById(R.id.et_4);
        //product_Space_input = findViewById(R.id.sapce_spinner);
        // CustomAdapter에서 넘겨받은 데이터를 확인하는 조건문
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("product_name") &&
                getIntent().hasExtra("product_time") &&
                getIntent().hasExtra("product_menu") &&
                getIntent().hasExtra("product_adress")){

            //Intent한 데이터를 변수에 넣는 작업
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("product_name");
            time = getIntent().getStringExtra("product_time");
            menu = getIntent().getStringExtra("product_menu");
            address = getIntent().getStringExtra("product_address");


            //위에 넣은 데이터를 텍스트에 넣는 작업
            product_name_input.setText(name);
            product_time_input.setText(time);
            product_menu_input.setText(menu);
            product_address_input.setText(address);
            //product_Space_input.setSelection(); // 위치 값이 안변함

        }else { // 만일 데이터가 없다면 오류 메세지를 출력함
            Toast.makeText(this,"NO DATA", Toast.LENGTH_SHORT).show();
        }
    }
    // 삭제 버튼 부분
    /*void Delete_Button(){
        delete_button = findViewById(R.id.deleteButton);
        delete_button.setVisibility(View.VISIBLE);
        delete_button.setOnClickListener(new View.OnClickListener() { //삭제버튼 동작
            @Override
            public void onClick(View v) {
                confirmDialog(); // 삭제하기위함 추가 함수
            }
        });
    }*/
    // 완료 버튼 부분
    void Update_Button(){
        update_button = findViewById(R.id.btn_1);
        update_button.setText("수정"); // 버튼의 이름을 바꿈 (추가 -> 수정)
        update_button.setOnClickListener(v -> { //수정 버튼 동작
            if(product_name_input.getText().toString().equals("")) {// 만일 name칸에 아무런 글자가 없으면 오류메세지를 출력한다.
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            }else { // 데이터를 updateData함수로 넘김
                Databasehelper myDB = new Databasehelper(updateActivity.this);
                myDB.updateData(id,
                        product_name_input.getText().toString().trim(),
                        product_time_input.getText().toString().trim(),
                        product_menu_input.getText().toString().trim(),
                        product_address_input.getText().toString().trim());
                finish(); // 페이지를 끝냄
            }
        });
    }

    void Cancle_Button(){
        // 취소 버튼을 누르면 뒤로돌아감
        Button button = (Button)findViewById(R.id.btn_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void confirmDialog(){ // 삭제기능 함수
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //실수인지를 확인하는 텍스트 메세지를 출력
        builder.setTitle("DELETE "+name+"?"); // 오류  구문의 타이틀
        builder.setMessage(name+"을 삭제 하시겠습니까?"); // 오류 구문안의 메세지
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() { //오류 구문에있는 버튼 (예 버튼)
            @Override
            public void onClick(DialogInterface dialog, int which) {// 데이터를 삭제하기위함 동작
                Databasehelper myDB = new Databasehelper(updateActivity.this);// Databasehelper객체 지정
                myDB.deleteOneRow(id); // Databasehelper객체의 deleteOneRow함수 동작
                finish(); // 페이지 종료
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() { //오류 구문에있는 버튼 (아니오 버튼)
            @Override
            public void onClick(DialogInterface dialog, int which) {// 할 동작이 없으므로 내용이 없음
            }
        });
        builder.create().show(); // 위에 버튼을 보이도록 하는 지정함수?
    }

    /*void spinner(){// space 값을 spinner목록으로 만드는 함수
        Spinner spinner = (Spinner) findViewById(R.id.sapce_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.space, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }*/

    // 이미지를 눌렀을시 카메라기능을 킴
    public void image1(View target)
    {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }
    //카메라 기능을 종료하였을때
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView = (ImageView) findViewById(R.id.imageView);
        super.onActivityResult(requestCode, resultCode, data);
        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if(requestCode == 0 && resultCode == RESULT_OK) { // 사진을 찍었을 경우 작동
            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();
            //Bitmap으로 컨버전
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //이미지뷰에 Bitmap으로 이미지를 삽입
            imageView.setImageBitmap(imageBitmap);
        }
    }
}