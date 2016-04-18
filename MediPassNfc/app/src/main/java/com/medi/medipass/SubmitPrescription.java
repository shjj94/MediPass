package com.medi.medipass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by Elizabeth on 2016-04-08.
 */
public class SubmitPrescription extends AppCompatActivity {

    Record record_activity = (Record)Record.record_activity;
    public static Activity submit_activity;
    final String TAG="NFC";

    /** NFC라이브러리 사용위해 선언 **/
    HojungNFCReadLibrary hojungNFCReadLibrary;


    Context mContext;
    Boolean submitPres; //처방전을 제출 했는지 안했는지에 대해 식별


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_prescription);
        final Dialog dialog = new Dialog(SubmitPrescription.this);

        if(record_activity != null) {
            record_activity.finish();
        }


        //제출하기 버튼
        Button btn_submit = (Button)findViewById(R.id.btn_submit);
        //Button btn_closedialog = (Button)findViewById(R.id.close_dialog);

        /** 제출하기 버튼 클릭시 다이얼로그 뜸 **/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.setContentView(R.layout.treat_recept);
                dialog.setTitle("약국");

                TextView tv = (TextView) dialog.findViewById(R.id.text);
                tv.setText("NFC스티커에 태그해주세요");

                ImageView iv = (ImageView) dialog.findViewById(R.id.image);
                iv.setImageResource(R.drawable.drug);

                dialog.show();
                submitPres=true;
            }
        });



        hojungNFCReadLibrary = new HojungNFCReadLibrary(getIntent(), SubmitPrescription.this, new OnHojungNFCListener() {

            @Override
            public void onReceiveMessage(NfcModel[] models) {
                // TODO Auto-generated method stub
                try{
                    if(submitPres==true){ //제출하기 다이얼로그가 떠있는 상태일 때
                        /* NFC태그값 토스트로 띄워주고
                         * 다이얼로그 끔
                         */
                        Toast.makeText(SubmitPrescription.this, "type : " + models[0].getTypeStr() + " , " + "payload : " + models[0].getPayloadStr(), Toast.LENGTH_SHORT).show();
                        submitPres=false;
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

        submit_activity=SubmitPrescription.this;

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
}
