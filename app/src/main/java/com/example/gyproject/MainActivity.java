package com.example.gyproject;


import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    //온도 습도 화재 움직임 설명텍스트
    private TextView txtHum, txtDet, txtFire, txtTem, txtHumNum, txtTemNum, txtOnOff, txtCnt;
    private Switch swOnOff;
    private Button btn1;

    private CountDownTimer countDownTimer;
    //타이머 시간 초 정하기
    private static final long START_TIME_IN_MILLIS = 1800000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    //상황판단 변수
    private int isEmergency = -1;

    //문자전송권환 허용
    static final int SMS_SEND_PERMISSON = 1;

    //실시간 DB 관리 객체얻어오기
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //저장시킬 노드 참조객체 가져오기
    DatabaseReference rootRef = firebaseDatabase.getReference();

    //주소변수
    String address;

    //메세지연속으로 보내는것을 막기위한 변수
    boolean isSendSMS;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //문자전송권한확인
        int permissonCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissonCheck != PackageManager.PERMISSION_GRANTED) {
            //문자보내기거부
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Toast.makeText(getApplicationContext(), "SMS 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
            //문자보내기권한허용
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSON);
        }

        txtCnt = (TextView) findViewById(R.id.txtCnt);
        txtDet = (TextView) findViewById(R.id.txtDet);
        txtFire = (TextView) findViewById(R.id.txtFire);
        txtHum = (TextView) findViewById(R.id.txtHum);
        txtTem = (TextView) findViewById(R.id.txtTem);
        txtHumNum = (TextView) findViewById(R.id.txtHumNum);
        txtTemNum = (TextView) findViewById(R.id.txtTemNum);
        txtOnOff = (TextView) findViewById(R.id.txtOnOff);

        swOnOff = (Switch) findViewById(R.id.swOnOff);

        swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            //가습기의 ON OFF를 체크해주는 메서드
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOnOff.setText("On");
                } else {
                    txtOnOff.setText("OFF");
                }
            }
        });

    }


    //경고창 출력
    protected void onStart() {
        super.onStart();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //파이어베이스DB에 있는 값 읽어오기
                int det = datasnapshot.child("det").getValue(int.class);
                int fire = datasnapshot.child("fire").getValue(int.class);
                int hum = datasnapshot.child("hum").getValue(int.class);
                int tem = datasnapshot.child("tem").getValue(int.class);
                address = datasnapshot.child("address").getValue(String.class);
                txtHumNum.setText(String.valueOf(hum) + "%");
                txtTemNum.setText(String.valueOf(tem) + "℃");

                if (det == 0) {
                    txtDet.setText("움직임 미감지");
                    txtFire.setTextColor(Color.RED);
                    startTimer();
                }
                if (det == 1) {
                    txtDet.setText("움직임 감지");
                    txtFire.setTextColor(Color.BLACK);
                    resetTimer();
                    updateTimer();
                }
                if (fire == 0) {
                    txtFire.setText("화재 미감지");
                    txtFire.setTextColor(Color.BLACK);
                }
                if (fire == 1) //화재가 감지됨
                {
                    txtFire.setText("화재 발생!!!");
                    txtFire.setTextColor(Color.RED);
                    showDialog(1);
                    //다이얼로그 자동닫힘
                    startThread();
                }
                if (hum < 40) { //습도가 낮음
                    txtHum.setText("습도 낮음");
                    txtHum.setTextColor(Color.BLACK);
                }
                if (hum >= 40 && hum <= 60) //습도가 적당함
                {
                    txtHum.setText("습도 적당");

                }
                if (hum > 60) //습도가 높음
                {
                    txtHum.setText("습도 높음");
                }
                if (tem <= 10) { //날씨가 추움
                    txtTem.setText("날씨가 춥습니다!");
                    txtTem.setTextColor(Color.BLUE);
                }
                if (tem > 10 && tem < 20)//날씨가 선선함
                {
                    txtTem.setText("선선한 날씨입니다!");
                    txtTem.setTextColor(Color.BLACK);
                }
                if (tem >= 20) { //날씨가 더움
                    txtTem.setText("날씨가 덥습니다!");
                    txtTem.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
    }


    //액션메뉴바 객채설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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

    //메세지보내기 메서드
    private void sendSMS(String message) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), FLAG_IMMUTABLE);
        SmsManager sms = SmsManager.getDefault();
        rootRef.child("tel").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //비상상황이면 메세지를 DB에 등록된 전화번호로 모두 보내기
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    //데이터가져오기
                    String value = snapshot.getValue(String.class);
                    sms.sendTextMessage(value, null, address+"\n"+message, pi, null);
                }
                Toast.makeText(getBaseContext(), "메세지가 전송되었습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = new ProgressDialog(this); // 사용자에게 보여줄 대화상자
        dialog.setTitle("응급상황확인");
        dialog.setMessage("30초내에 종료버튼을 누르지 않으면 자동으로 응급상황메세지가 보내집니다");
        dialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "종료",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        thread.stop();
                    }
                }
        );
        return dialog;
    }

    public void startThread() {
        //다이얼로그자동 닫힘
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        removeDialog(1);
                        sendSMS("응급상황발생");
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 30000);
            }
        });
        thread.start();
    }


    //타이머객체
    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            public void onFinish() {
                showDialog(1);
                //다이얼로그 자동닫힘
                startThread();
            }
        }.start();
    }

    public void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateTimer();
    }

    public void updateTimer() {
        //초
        int sec = (int) (mTimeLeftInMillis / 1000) % 60;
        //분
        int min = (int) (mTimeLeftInMillis / 1000) / 60;
        //시
        int hour = (int) (mTimeLeftInMillis / 1000) / 60 / 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, min, sec);

        txtCnt.setText(timeLeftFormatted);
    }


}


