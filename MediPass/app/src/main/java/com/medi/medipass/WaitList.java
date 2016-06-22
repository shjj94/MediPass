package com.medi.medipass;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nara on 2016-04-07.
 */

public class WaitList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    /* 로그인 안하고 대기인원 확인하기 */
    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침
    ListView listView;

    //data를 받아올 php주소
    String url_hosinfo = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/all_hosinfo.php";

    //Adapter생성
    final WaitListAdapter adapter = new WaitListAdapter();


    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_wait);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);


        listView = (ListView) findViewById(R.id.waitlist);
        //리스트뷰 참조 및 Adapter달기
        listView.setAdapter(adapter);

        //php를 읽어올때 사용할 변수
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(url_hosinfo);
        mContext = this;
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
                gPHP.execute(url_hosinfo);
                listView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
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
                Log.d("LIST", "listw");
                if (jobject.get("status").equals("listw")) {
                    Log.d("LIST", "listw");
                    for (int i = 0; i < results.length(); i++) { //length->child의 갯수
                        JSONObject temp = results.getJSONObject(i);
                        String hospital_name = temp.getString("hospital_name");
                        String wait_num = temp.getString("wait_num");
                        adapter.addItem(hospital_name, wait_num);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
