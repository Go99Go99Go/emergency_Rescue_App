package com.example.gyproject;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelPage extends AppCompatActivity {

    private EditText editPhone, editName;
    private Button btnAddTel, btnDelTel;
    private ListView listView;
    private TextView txtTest;

    //실시간 DB 관리 객체얻어오기
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //저장시킬 노드 참조객체 가져오기
    DatabaseReference rootRef = firebaseDatabase.getReference();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telpage);

        //타이틀변경
        getSupportActionBar().setTitle("전화번호 등록하기");

        //리스트뷰 어댑터 초기화 및 리스트 초기화
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),list,android.R.layout.simple_list_item_2,new String[] {"name","tel"},new int[] {android.R.id.text1, android.R.id.text2});

        btnAddTel = (Button) findViewById(R.id.btnAddTel);
        btnDelTel = (Button) findViewById(R.id.btnDelTel);
        listView = (ListView) findViewById(R.id.listView);
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        //전화번호 하이픈 자동입력
        editPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        //추가/수정버튼 이벤트
        btnAddTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //빈칸확인
                if (editName.length() == 0 || editPhone.length() == 0) {
                    Toast.makeText(getApplicationContext(), "빈칸을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                //전화번호입력확인
                if (editPhone.length() < 13) {
                    Toast.makeText(getApplicationContext(), "전화번호를 끝까지 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //이름과 전화번호를 추가
                    rootRef.child("tel").child(editName.getText().toString()).setValue(editPhone.getText().toString());
                    Toast.makeText(getApplicationContext(), "전화번호를 추가하였습니다", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editPhone.setText("");
                }
            }
        });

        //삭제버튼 이벤트
        btnDelTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //빈칸확인
                if (editName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "삭제할 전화번호를 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //이름과 전화번호를 삭제
                    rootRef.child("tel").child(editName.getText().toString()).removeValue();
                    Toast.makeText(getApplicationContext(), "전화번호를 삭제하였습니다", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editPhone.setText("");
                }
            }
        });


        //리스트 선택 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> data = (HashMap<String, String>) parent.getItemAtPosition(position);
                editName.setText(data.get("name"));
                editPhone.setText(data.get("tel"));
                Toast.makeText(getApplicationContext(), (position+1)+"번째 연락처선택", Toast.LENGTH_SHORT).show();
            }
        });

        rootRef.child("tel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                list.clear();
                //리스트 읽어오기
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    HashMap<String,String> item = new HashMap<String, String>();
                    //데이터가져오기
                    String key = snapshot.getKey();
                    String value = snapshot.getValue(String.class);
                    //리스트에 변수담기
                    item.put("name",key);
                    item.put("tel",value);
                    list.add(item);
                }
                //리스트뷰어댑터 설정
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //실행
    @Override
    protected void onResume() {
        super.onResume();

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

