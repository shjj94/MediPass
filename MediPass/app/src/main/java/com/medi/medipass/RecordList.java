package com.medi.medipass;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
 * Created by Sohyeon on 2016-05-03.
 */
/* listView 잘 설명된 블로그 : http://jo.centis1504.net/?p=1009 */
/* http://recipes4dev.tistory.com/43 */

public class RecordList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침
    ListView listview;

    //data를 받아올 php주소
    String url_list = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/write_app.php";
    String url_medicine = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/write_medicine.php";
    //Adapter생성
    final ListViewAdapter adapter = new ListViewAdapter();
    static String date, disName;
    ListViewAdapter_medicine med_adapter = new ListViewAdapter_medicine();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_record);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        //php를 읽어올때 사용할 변수
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(url_list);


        //클릭 이벤트 정의
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //int position = (Integer)v.getTag();
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                View dialogView = (View) v.inflate(getContext(), R.layout.record_item_click, null);
                ListView listview_med = (ListView) dialogView.findViewById(R.id.record_item_click_listView);
                listview_med.setAdapter(med_adapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("처방목록");
                builder.setView(dialogView);
                builder.setPositiveButton("확인", null);

                date = item.getItem_date();
                disName = item.getItem_disName();

                //php를 읽어올때 사용할 변수
                GettingPHP mPHP = new GettingPHP();
                mPHP.execute(url_medicine);

                TextView tvdate = (TextView) dialogView.findViewById(R.id.textView);
                tvdate.setText(item.getItem_date());

                builder.show();
            }
        });


        return view;
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
                gPHP.execute(url_list);
                listview.setAdapter(adapter);
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
                    if (params[0].equals(url_medicine)) {
                        String data = "date=" + date + "& disName=" + disName;

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
                    }

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

                if (jobject.get("status").equals("list")) {
                    Log.d("LIST", "list");
                    for (int i = 0; i < results.length(); i++) { //length->child의 갯수
                        JSONObject temp = results.getJSONObject(i);
                        String date = temp.getString("record_date");
                        String name = temp.getString("disease_name");
                        adapter.addItem(date, name);
                    }
                }

                if (jobject.get("status").equals("medicine")) {
                    Log.d("LIST", "medicine");
                    med_adapter.init();
                    for (int i = 0; i < results.length(); i++) { //length->child의 갯수
                        JSONObject temp = results.getJSONObject(i);
                        String medName = temp.getString("medName");
                        int onceNum = temp.getInt("onceNum");
                        int dayNum = temp.getInt("dayNum");
                        String notice = temp.getString("notice");
                        med_adapter.addItem(medName, onceNum, dayNum, notice);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*
    //http://blog.naver.com/PostView.nhn?blogId=aiger&logNo=100131220273
    //http://www.a2big.com/_board/content.php?id=1106&pass_id=14&secret=0&slug=&page=
    public static String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            //HttpClient생성
            HttpClient httpClient = new DefaultHttpClient();
            //php서버 url의 POST request를 만든다
            HttpPost post = new HttpPost(url);
eJsonData();

            //StringEntity에 JSON문자열을 UTF-8형태로 설정한다
            StringEntity se = new StringEntity(json, "UTF-8");

            //hostPost Entity 설정
            post.setEntity(se);

            //서버에 전송을 하기 위해 headers정보를
            //JSONArray형태로 데이터를 만든다
            //JSONArray를 String문자열로 변환한다
            String json = mak 설정한다.
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "applicationn/json");
            post.setHeader("Accept-Charset", "UTF-8");

            //POST request를 실행한다
            HttpResponse httpResponse = httpclient.execute(post);

            //서버로 부터 응답 메세지를 받는다
            inputStream = httpResponse.getEntity().getContent();

            //수신한 응답 메세지의 inputstream을 string형태로 변환한다
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else
                result = "Did not work!";
        }catch (Exception e){
            Log.e("JH", e.getLocalizedMessage());
        }

        //return result
        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static String makeJsonData(){
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.addProperty("date", date);
        jsonObject1.addProperty("disName", disName);

        JSONArray jArray = new JSONArray();

        jArray.add(jsonObject1);
        String str = jArray.toString();
        return str;
    }
    */

}

/*
public class CommonHttpClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("CommonClient", params.toString());
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return NetDefine.BASE_URL + relativeUrl;
    }
}
*/
