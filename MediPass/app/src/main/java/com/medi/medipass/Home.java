package com.medi.medipass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hojung.nfc.HojungNFCReadLibrary;
import com.hojung.nfc.interfaces.OnHojungNFCListener;
import com.hojung.nfc.model.NfcModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Home extends AppCompatActivity {

    /*url*/
    String register_url = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/register_wait_list.php";
    String wait_url = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/show_waitnum.php";

    /*대기인원 출력 위한 TextView지정*/
    TextView tv;

    /*태그 이름 지정*/
    final String TAG = "NFC";
    final String PHP = "PHPR";

    /*대기인원 저장변수*/
    static String wait_num = "";
    public static Activity home_activity;

    /*nfc 병원코드 저장 변수*/
    static String hospital_code="000001";

    /*대기인원 업데이트 위한 핸들러*/
    Handler m_handler;
    Runnable m_task;

    /* 하단바를 이용해 home으로 올 때 직전activity를 끄기 위한 선언. activity들을 받아온다. */
    Login login_activity = (Login) Login.login_activity;

    private BackPressCloseHandler backPressCloseHandler;// 뒤로가기 버튼 등록(두번 터치시 종료에 사용)


    /*NFC라이브러리 사용위해 선언*/
    HojungNFCReadLibrary hojungNFCReadLibrary;


    Context mContext;
    Boolean recptClicked;

    //SubmitPrescription submit_activity = (SubmitPrescription)SubmitPrescription.submit_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        home_activity = Home.this;// 현재 activity를 변수에 넣는다.
        mContext = this;

        /*핸들러 시작*/
        m_handler = new Handler();
        m_task = new Runnable() {
            @Override
            public void run() {
                /*대기인원 업데이트 함수*/
                UpdateWait();
                m_handler.postDelayed(m_task, 4000);
            }
        };

        /*다이얼로그(nfc 안내 팝업 띄우기)*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("병원입니다")
                .setMessage("NFC스티커에 태그해주세요.")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = builder.create(); //다이얼로그 생성

        backPressCloseHandler = new BackPressCloseHandler(this);// 뒤로가기 버튼 객체 생성

        /* intent시 직전 activity종료 */
        login_activity.finish();


        if (login_activity != null) {
            login_activity.finish();
        }

        /*버튼 선언*/
        final Button bt_record = (Button) findViewById(R.id.home_record);
        Button bt_myPage = (Button) findViewById(R.id.home_mypage);
        Button bt_waitList = (Button) findViewById(R.id.home_check_number);
        final Button bt_receipt = (Button) findViewById(R.id.home_receipt);
        Button bt_submit = (Button) findViewById(R.id.home_submit);

        //Typeface typeFace = Typeface.createFromAsset(getAssets(), "저장되어있는폰트파일"));
        //텍스트뷰.setTypeface(typeFace);
//        Typeface typeFace = Typeface.createFromAsset(getAssets(), "yoongodic");
//        bt_record.setTypeface(typeFace);
//        bt_myPage.setTypeface(typeFace);
//        bt_receipt.setTypeface(typeFace);
//        bt_submit.setTypeface(typeFace);
//        bt_waitList.setTypeface(typeFace);

        bt_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_record = new Intent(getApplicationContext(), Record.class);
                startActivity(intent_record);
            }
        });

        bt_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_mypage = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent_mypage);
            }
        });

        bt_waitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_waitlist = new Intent(getApplicationContext(), WaitList.class);
                startActivity(intent_waitlist);
            }
        });

        /* 병원 접수하기 버튼 클릭시 다이얼로그 뜸*/
        bt_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recptClicked = true; //다이얼로그 떠있는 상태일 때에만 태그 동작하기 위한 bool값
                dialog.show();    // 알림창 띄우기
            }
        });


        /* 처방전 제출버튼 */
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_submit = new Intent(getApplicationContext(), SubmitPrescription.class);
                startActivity(intent_submit);
            }
        });



        /*nfc 사용 안내*/
        android.nfc.NfcAdapter mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(mContext);

        /*NFC 미지원단말*/
        if (mNfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            /* NFC꺼져있는 경우, NFC켜기 */
            if (!mNfcAdapter.isEnabled()) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                alertbox.setTitle("Info");
                alertbox.setMessage("본 서비스를 이용하기 위해 NFC를 사용하셔야 합니다.");
                alertbox.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    }
                });
                alertbox.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertbox.show();

            }
        } catch (Exception e) {

        }

        hojungNFCReadLibrary = new HojungNFCReadLibrary(getIntent(), Home.this, new OnHojungNFCListener() {

            @Override
            public void onReceiveMessage(NfcModel[] models) {
                // TODO Auto-generated method stub

                try {

                    Log.d("NFC1", "type : " + models[0].getTypeStr() + " , " + "payload : " + models[0].getPayloadStr() + " , " + "recptClicked : " + recptClicked);
                    String spot = models[0].getTypeStr();
                    hospital_code = models[0].getPayloadStr();

                    if (spot instanceof String) {

                        Log.d("NFC1", "spot : " + spot);
                    }

                    /*접수하기 다이얼로그 떠있는 상태일 때*/
                    if (recptClicked == true && spot.equals("hospital")) {

                        /*nfc태그시 대기인원 업데이트 위한 쓰레드 시작지점*/
                        m_handler.postDelayed(m_task, 1000);

                        /* NFC태그값 토스트로 띄워주고 다이얼로그 끔*/
                        //Toast.makeText(Home.this, "type : " + models[0].getTypeStr() + " , " + "payload : " + models[0].getPayloadStr(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Home.this, "접수되었습니다.", Toast.LENGTH_SHORT).show();

                        recptClicked = false;

                        /*대기목록에 이름 추가하기*/
                        registerWaitList();

                        /*접수 진행 후, 대기인원 출력, 버튼 비활성화*/
                        showWaitNum();
                        bt_receipt.setEnabled(false);

                        /*다이얼로그 종료*/
                        dialog.dismiss();

                        /*태그 정보*/
                        Log.d("NFC", "type" + models[0].getTypeStr() + "payload : " + models[0].getPayloadStr());
                    } else {
                        Toast.makeText(Home.this, "병원 접수 버튼입니다. 처방전 제출하기 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {

                }

            }

            @Override
            public void onError(String arg0) {
                // TODO Auto-generated method stub

            }
        });

    }


    private void initNFC() {
        try {
            Log.d("NFC", "intent : " + getIntent().getAction());
            Intent intent = getIntent();
            hojungNFCReadLibrary.onResume(intent);
        } catch (Exception e) {

        }

    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        initNFC();
//        Intent intent=getIntent();
//        hojungNFCReadLibrary.onResume(intent);

    }
    /*
    //뭐하는 앤지 아직! 다시 보기
    @Override
   public void onResume() {
      super.onResume();
      Log.d(TAG,"onResume");

      Log.d(TAG,"intent : "+getIntent().getAction());
      Intent intent=getIntent();
      hojungNFCReadLibrary.onResume(intent);

   }*/

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        hojungNFCReadLibrary.onPause();
    }


    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        hojungNFCReadLibrary.onNewIntent(intent);
    }

    @Override
    /* 뒤로가기 버튼 동작 시, 두번 눌러야 꺼지게 */
    public void onBackPressed() {
        backPressCloseHandler.onBackPressred();
    }


    /*대기목록에 등록하기*/
    public void registerWaitList() {
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(register_url);
    }

    /*대기인원 출력하기*/
    public void showWaitNum() {

        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(wait_url);

    }

    /*대기인원 업데이트하기*/
    public void UpdateWait() {
        showWaitNum();
        tv = (TextView) findViewById(R.id.home_receipt);
        tv.setText(wait_num);
    }


    //AsyncTask : thread + handler
    //Async(비동기화) : 병렬회로. 계속 요청을 보내는 통로와 응답을 받는 통로를 따로 만들어두는 것
    //sync(동기화) : 직렬회로. 일이 순차적으로 진행되면서 하나가 해결되면 그다음 일이 진행되는 식으로 네트워크에서는 요청(request)를 보내면 항상 응답(response)을 받아야 진행하는 방식으로 구현
    class GettingPHP extends AsyncTask<String, Integer, String> { //<Param, Progress, Result>

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //php에서 데이터를 읽어오는 역할, 백그라운드 스레드로 동작해야 하는 작업을 실행한다 : 필수구현
        //execute메서드로 전달한 data tye이 params 인수로 전달되는데 여러개의 인수를 전달할 수 있으므로 배열 타입으로 되어 있다.
        //그래서 하나의 인수만 필요하다면 params[0]만 사용하면 된다.
        @Override
        protected String doInBackground(String... params) {
            Log.d(PHP, "doInBackground " + params[0]);
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // URL --> openConnection() --> URLConnection  --> getInputStream --> InputStream (내용읽음)
                Log.d(PHP, "back_try");
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection(); //URL내용을 읽어오거나 GET/POST로 전달할 때 사용

                if (conn != null) {
                    /*진료 접수 url일 때 실행*/

                        String data = "hospital_code="+hospital_code;
                        Log.d(PHP, "data " + data);
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(5000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        //conn.setRequestProperty("Content-Type", "application/json");
                        conn.setUseCaches(false);

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        bw.write(data);
                        bw.flush();
                        bw.close();
                        Log.d(PHP, "data push end" + data);

                        //post메세지가 전송된다
                        conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            jsonHtml.append(line + "\n");
                            Log.d("HHH", "list_line : " + line);

                            /*대기인원 저장*/
                            wait_num=jsonHtml.toString();
                            Log.d("hhh", "waitn"+wait_num);
                        }
                        br.close();
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.d(PHP, "Error");
                e.printStackTrace();
            }
            Log.d(PHP, "end of doInBackground : " + jsonHtml.toString());

            return jsonHtml.toString();
        }

        //가져온 데이터를 이용해 원하는 일을 하도록 한다
        @Override
        protected void onPostExecute(String str) {
            Log.d(PHP, "onPostExecute" + str);
            try {


                Log.d(PHP, "post_try");

                /*대기인원 지정*/
                //int wt;
                //wt=Integer.parseInt(wait_num);
                //wt=wt-1;
                //Log.d(PHP, "Wt : "+String.valueOf(wt));
                tv = (TextView) findViewById(R.id.home_receipt);
                tv.setText(wait_num);
                tv.setTextSize(20.0f);


            } catch (Exception e) {
                Log.d(PHP, "onPost Error");
                e.printStackTrace();
            }
        }
    }


}