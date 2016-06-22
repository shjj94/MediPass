package com.medi.medipass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hojung.nfc.HojungNFCReadLibrary;
import com.hojung.nfc.interfaces.OnHojungNFCListener;
import com.hojung.nfc.model.NfcModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Nara on 2016-05-18.
 */
//https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156
//https://www.simplifiedcoding.net/android-recyclerview-and-cardview-tutorial/
public class SubmitPrescription extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //http://ggari.tistory.com/528
    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침
    ListView listView;


    String url_showPrescription = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/show_prescription.php";
    String url_submitPrescription = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/submit_prescription.php";
    String url_registerPharm = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/register_wait_list_pharm.php";

    //Adapter생성
    final SubmitAdapter adapter = new SubmitAdapter();


    /*태그 이름 지정*/
    final String TAG = "NFCP";
    final String PHP = "PHPP";

    /*NFC라이브러리 사용위해 선언*/
    HojungNFCReadLibrary hojungNFCReadLibrary;
    Context mContext;
    Boolean submitClicked;

    /*처방전 제출위함*/
    static String presnum, pharm_code;


    private BackPressCloseHandler backPressCloseHandler;// 뒤로가기 버튼 등록(두번 터치시 종료에 사용)


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);
        /*리스트*/
        listView = (ListView) findViewById(R.id.submitlist);

        //리스트뷰 참조 및 Adapter달기
        listView.setAdapter(adapter);

        //php를 읽어올때 사용할 변수
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(url_showPrescription);


        mContext = this;

                /*다이얼로그(nfc 안내 팝업 띄우기)*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("약국입니다.")
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                submitClicked = true; //다이얼로그 떠있는 상태일 때에만 태그 동작하기 위한 bool값
                SubmitItem item = (SubmitItem) parent.getItemAtPosition(position);
                presnum = item.getPrescription();
                Log.d("PRES", "presnum : " + presnum);
                dialog.show();
            }
        });

        /*nfc 사용 안내*/
        android.nfc.NfcAdapter mNfcAdapter1 = android.nfc.NfcAdapter.getDefaultAdapter(mContext);

        /*NFC 미지원단말*/
        if (mNfcAdapter1 == null) {
            Toast.makeText(getApplicationContext(), "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            /* NFC꺼져있는 경우, NFC켜기 */
            if (!mNfcAdapter1.isEnabled()) {

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

        hojungNFCReadLibrary = new HojungNFCReadLibrary(getIntent(), SubmitPrescription.this, new OnHojungNFCListener() {

            @Override
            public void onReceiveMessage(NfcModel[] models) {
                // TODO Auto-generated method stub

                try {

                    Log.d("NFC1", "type : " + models[0].getTypeStr() + " , " + "payload : " + models[0].getPayloadStr() + " , " + "submitClicked : " + submitClicked);
                    String spot = models[0].getTypeStr();

                    if (spot instanceof String) {

                        Log.d("NFC1", "spot : " + spot);
                    }

                    /*접수하기 다이얼로그 떠있는 상태일 때*/
                    if (submitClicked == true && spot.equals("pharmacy")) {


                        /* NFC태그값 토스트로 띄워주고 다이얼로그 끔*/
                        //Toast.makeText(Home.this, "type : " + models[0].getTypeStr() + " , " + "payload : " + models[0].getPayloadStr(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(SubmitPrescription.this, "제출되었습니다.", Toast.LENGTH_SHORT).show();

                        submitClicked = false;

                        submitPrescription();


                        /*다이얼로그 종료*/
                        dialog.dismiss();

                        /*태그 정보*/
                        Log.d("NFC", "type" + models[0].getTypeStr() + "payload : " + models[0].getPayloadStr());
                        pharm_code = models[0].getPayloadStr();
                        Log.d("NFC", "pharm_code:" + pharm_code);
                    } else if (submitClicked == false && spot.equals("pharmacy")) {
                        Toast.makeText(SubmitPrescription.this, "처방전을 클릭해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SubmitPrescription.this, "처방전 제출 버튼입니다. 접수하기 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
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


    //새로고침
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.init();
                adapter.notifyDataSetChanged();
                //해당 어댑터를 서버와 통신한 값이 나오면 됨
                GettingPHP gPHP = new GettingPHP();
                gPHP.execute(url_showPrescription);
                listView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
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


    /*처방전 제출하기*/
    public void submitPrescription() {
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(url_submitPrescription);
        Log.d("Pharmcode","Pharmcode : "+pharm_code);
        registePharm();
    }

    public void registePharm() {
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(url_registerPharm);
    }


    //AsyncTask : thread + handler
    //Async(비동기화) : 병렬회로. 계속 요청을 보내는 통로와 응답을 받는 통로를 따로 만들어두는 것
    //sync(동기화) : 직렬회로. 일이 순차적으로 진행되면서 하나가 해결되면 그다음 일이 진행되는 식으로 네트워크에서는 요청(request)를 보내면 항상 응답(response)을 받아야 진행하는 방식으로 구현
    class GettingPHP extends AsyncTask<String, Integer, String> { //<Param, Progress, Result(doInBackground의 반환값, onPostExcute의 매개변수)>

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //php에서 데이터를 읽어오는 역할, 백그라운드 스레드로 동작해야 하는 작업을 실행한다 : 필수구현
        //execute메서드로 전달한 data tye이 params 인수로 전달되는데 여러개의 인수를 전달할 수 있으므로 배열 타입으로 되어 있다.
        //그래서 하나의 인수만 필요하다면 params[0]만 사용하면 된다.
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // URL --> openConnection() --> URLConnection  --> getInputStream --> InputStream (내용읽음)
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection(); //URL내용을 읽어오거나 GET/POST로 전달할 때 사용

                if (conn != null) {
                    //if (params[0].equals(url_submitPrescription)) {
                        Log.d("NFC", "pharm_code11:" + pharm_code);
                        String data = "presnum=" + presnum + "& pharm_code=" + pharm_code;
                        Log.d(PHP, data);
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

                        //post메세지가 전송된다
                        conn.connect();


                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            jsonHtml.append(line + "\n");
                            Log.d("HHH", "list_line : " + line);
                        }
                        br.close();
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        //가져온 데이터를 이용해 원하는 일을 하도록 한다
        @Override
        protected void onPostExecute(String str) {
            try {
                //php에서 받아온 JSON데이터를 JSON오브젝트로 변환
                JSONObject jobject = new JSONObject(str);
                //results라는 key는 JSON배열로 되어있다
                JSONArray results = jobject.getJSONArray("results");

                if (jobject.get("status").equals("listp")) {
                    Log.d("LIST", "list_pres");
                    for (int i = 0; i < results.length(); i++) { //length->child의 갯수
                        JSONObject temp = results.getJSONObject(i);
                        String date = temp.getString("record_date");
                        String valid_date = temp.getString("valid_date");
                        String hos_name = temp.getString("hospital_name");
                        String pres_num = temp.getString("prescription_num");
                        String dis_name = temp.getString("disease_name");
                        adapter.addItem(date, valid_date, hos_name, dis_name, pres_num);

                    }
                }

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

