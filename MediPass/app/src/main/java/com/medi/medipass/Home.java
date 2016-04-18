package com.medi.medipass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hojung.nfc.HojungNFCReadLibrary;
import com.hojung.nfc.interfaces.OnHojungNFCListener;
import com.hojung.nfc.model.NfcModel;

public class Home extends AppCompatActivity {

    final String TAG="NFC";
    public static Activity home_activity;

    /* 하단바를 이용해 home으로 올 때 직전activity를 끄기 위한 선언. activity들을 받아온다. */
    Login login_activity = (Login)Login.login_activity;

    private BackPressCloseHandler backPressCloseHandler;// 뒤로가기 버튼 등록(두번 터치시 종료에 사용)


    /** NFC라이브러리 사용위해 선언 **/
    HojungNFCReadLibrary hojungNFCReadLibrary;


    Context mContext;
    Boolean recptliCked;

    //SubmitPrescription submit_activity = (SubmitPrescription)SubmitPrescription.submit_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        home_activity = Home.this;// 현재 activity를 변수에 넣는다.
        mContext = this;
        final Dialog dialog = new Dialog(Home.this);

        backPressCloseHandler = new BackPressCloseHandler(this);// 뒤로가기 버튼 객체 생성

        /* intent시 직전 activity종료 */
        login_activity.finish();


        android.nfc.NfcAdapter mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(mContext);

        if (mNfcAdapter == null) { // NFC 미지원단말
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



        if(login_activity != null) { login_activity.finish(); }

        Button bt_record = (Button)findViewById(R.id.home_record);
        Button bt_myPage = (Button) findViewById(R.id.home_mypage);
        Button bt_waitList = (Button) findViewById(R.id.home_check_number);
        final Button bt_receipt = (Button) findViewById(R.id.home_receipt);
        Button bt_submit = (Button) findViewById(R.id.home_submit);
        //Button bt_close= (Button) findViewById(R.id.close_dialog);



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
            public void onClick(View v){
                Intent intent_waitlist = new Intent(getApplicationContext(), WaitList.class);
                startActivity(intent_waitlist);
            }
        });

        /* 병원 접수하기 버튼 클릭시 다이얼로그 뜸*/
        bt_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent_receipt = new Intent(getApplicationContext(), HospitalNfc.class);
                startActivity(intent_receipt);*/
                //hospital_activity=HospitalNfc.this;
                recptliCked = true;
//                Dialog dialog = new Dialog(Home.this);
                dialog.setContentView(R.layout.treat_recept);
                dialog.setTitle("병원");

                TextView tv = (TextView) dialog.findViewById(R.id.text);
                tv.setText("NFC스티커에 태그해주세요");

                ImageView iv = (ImageView) dialog.findViewById(R.id.image);
                iv.setImageResource(R.drawable.hosp);

                dialog.show();
            }
        });


        /* 처방전 제출버튼 */
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent_submit = new Intent(getApplicationContext(), SubmitPrescription.class);
                startActivity(intent_submit);
            }
        });


        //cImage = (SmartImageView) findViewById(R.id.image);

        //NFC is use?


        hojungNFCReadLibrary = new HojungNFCReadLibrary(getIntent(), Home.this, new OnHojungNFCListener() {

            @Override
            public void onReceiveMessage(NfcModel[] models) {
                // TODO Auto-generated method stub
                try{
                    if(recptliCked==true){//접수하기 다이얼로그 떠있는 상태일 때
                        /* NFC태그값 토스트로 띄워주고
                         * 다이얼로그 끔
                         */
                        Toast.makeText(Home.this, "type : " + models[0].getTypeStr() + " , " + "payload : " + models[0].getPayloadStr(), Toast.LENGTH_SHORT).show();
                        recptliCked=false;
                        dialog.dismiss();
                    }


                } catch (Exception e) {

                }


                /*
                RequestParams params = new RequestParams();
                params.put("fitroom_code", models[0].getPayloadStr());

                CommonHttpClient.post(NetDefine.FIND_BRAND_INFO, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("response", response.toString());
                        CurrentFittingRoom.getInstance().setInfo(response);
                        cImage.setImageUrl(CurrentFittingRoom.getInstance().getImg_url());
                    }
                });
                * */
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
    @Override
	public void onResume() {
		super.onResume();
		//¾ÛÀÌ ´Ù½Ã ½ÇÇàÇÒ¶§ ndef adapter ½ÇÇà
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
    public void onBackPressed(){
        backPressCloseHandler.onBackPressred();
    }

}
