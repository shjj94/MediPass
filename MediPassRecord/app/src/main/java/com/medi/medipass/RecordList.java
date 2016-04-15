package com.medi.medipass;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* listView 잘 설명된 블로그 : http://jo.centis1504.net/?p=1009 */
/* http://recipes4dev.tistory.com/43 */

public class RecordList extends Fragment {

    //data를 받아올 php주소
    String url = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/write_app.php";
    //Adapter생성
    final ListViewAdapter adapter = new ListViewAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.record_list, container, false);

        ListView listview;

        //리스트뷰 참조 및 Adapter달기
        listview = (ListView)view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        //php를 읽어올때 사용할 변수
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(url);

        /*
        //아이템 추가
        adapter.addItem("2016-01-02", "장염");
        adapter.addItem("2016-01-03", "배탈");
        adapter.addItem("2016-01-04", "설사");
        */

        /*
        //클릭 이벤트 정의
        listview.setOnClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                //
            }
        });
        */

       return view;
    }

    //AsyncTask : thread + handler
    //Async(비동기화) : 병렬회로. 계속 요청을 보내는 통로와 응답을 받는 통로를 따로 만들어두는 것
    //sync(동기화) : 직렬회로. 일이 순차적으로 진행되면서 하나가 해결되면 그다음 일이 진행되는 식으로 네트워크에서는 요청(request)를 보내면 항상 응답(response)을 받아야 진행하는 방식으로 구현
    class GettingPHP extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //php에서 데이터를 읽어오는 역할, 백그라운드 스레드로 동작해야 하는 작업을 실행한다 : 필수구현
        //execute메서드로 전달한 data tye이 params 인수로 전달되는데 여러개의 인수를 전달할 수 있으므로 배열 타입으로 되어 있다.
        //그래서 하나의 인수만 필요하다면 params[0]만 사용하면 된다.
        @Override
        protected String doInBackground(String... params){
            Log.d("PHP", "doInBackground" + params);
            StringBuilder jsonHtml = new StringBuilder();
            try{
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

                if(conn != null){
                    conn.setConnectTimeout(1000); //1초
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while(true){
                            String line = br.readLine();
                            if(line == null) break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                }
                conn.disconnect();
            } catch(Exception e){
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        //가져온 데이터를 이용해 원하는 일을 하도록 한다
        @Override
        protected void onPostExecute(String str){
            Log.d("PHP", "onPostExecute" + str);
            try{
                //php에서 받아온 JSON데이터를 JSON오브젝트로 변환
                JSONObject jobject = new JSONObject(str);
                //results라는 key는 JSON배열로 되어있다
                JSONArray results = jobject.getJSONArray("results");

                /*
                String data = "";
                data += "Status : " + jobject.get("status");
                data += "\n";
                data += "Number of results : " + jobject.get("num_result");
                data += "\n";
                data += "Results : \n";
                */

                for(int i=0;i<results.length();++i){
                    JSONObject temp = results.getJSONObject(i);
                    adapter.addItem("" + temp.get("record_date"), "" + temp.get("disiase_name"));
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
