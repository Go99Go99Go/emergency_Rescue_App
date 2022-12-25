package com.example.gyproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddressPage extends AppCompatActivity {

    private TextView txtAddress;
    private EditText editAddress, editAddressDetail;
    private Button btnAddress;

    // 주소 요청코드 상수 requestCode
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    //실시간 DB 관리 객체얻어오기
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //저장시킬 노드 참조객체 가져오기
    DatabaseReference rootRef = firebaseDatabase.getReference();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresspage);
        //타이틀 변경
        getSupportActionBar().setTitle("주소 등록하기");

        txtAddress = (TextView) findViewById(R.id.txtAddress);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editAddressDetail = (EditText) findViewById(R.id.editAddressDetail);
        btnAddress = (Button) findViewById(R.id.btnAddress);

        //입력창 터치차단
        editAddress.setFocusable(false);
        //주소창입력
        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressAPI.class);
                getSearchResult.launch(intent);
            }
        });


        //주소 입력버튼을 누를시 주소 업데이트
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editAddress.length() == 0 || editAddressDetail.length() == 0 ){
                        Toast.makeText(getApplicationContext(),"주소를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
                }else {
                    //버튼을 누르면 db에 주소저장
                    rootRef.child("address").setValue(editAddress.getText() + " " + editAddressDetail.getText());
                    Toast.makeText(getApplicationContext(), "주소를 등록(변경)했습니다", Toast.LENGTH_SHORT).show();
                    editAddress.setText("");
                    editAddressDetail.setText("");
                }

            }
        });
    }

    //주소 결과값 입력창에 넣어주기
    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //AddressAPI 로부터 결과값이 전달됨
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");
                        editAddress.setText(data);
                    }
                }
            }
    );

    @Override
    protected void onStart() {
        super.onStart();
        rootRef.child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //등록된 주소읽어오기
                String address = datasnapshot.getValue().toString();
                txtAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //액션메뉴바 객채설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //메뉴이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.menu_tel:
                Intent intent2 = new Intent(getApplicationContext(), TelPage.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menu_address:
                Intent intent3 = new Intent(getApplicationContext(), AddressPage.class);
                startActivity(intent3);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

