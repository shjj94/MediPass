package com.medi.medipass;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by shjjthjj on 2016-04-14.
 */
public class ListViewAdapter extends BaseAdapter implements View.OnClickListener {
    Context mcontext = null;
    ListView listview;
    static String date, disName;
    ListViewAdapter_medicine med_adapter;
    String url = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/write_medicine.php";

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    //ListViewAdapter의 생성자
    public ListViewAdapter(){
    }

    //Adapter에 사용되는 데이터의 개수를 리턴 : 필수구현
    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 view를 리턴 : 필수구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        mcontext = parent.getContext();

        //listview_item 레이아웃을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            convertView.setTag(position);

            convertView.setOnClickListener(this);

        }

        //화면에 표시될 view(layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_date = (TextView) convertView.findViewById(R.id.item_date);
        TextView item_disName = (TextView) convertView.findViewById(R.id.item_disName);

        //Data Set(ListViewItem)에서 position에 위치한 데이터 참조 획득
        ListViewItem listviewItem = listViewItemList.get(position);

        //아이템 내 각 위젯이 데이터 반영
        item_date.setText(listviewItem.getItem_date());
        item_disName.setText(listviewItem.getItem_disName());

        return convertView;
    }

    //지정한 위치(position)에 있는 데이터와 아이템(row)의 ID를 리턴 : 필수구현
    @Override
    public long getItemId(int position){
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴 : 필수구현
    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String date, String disName){
        Log.d("PHP", "addItem");
        ListViewItem item = new ListViewItem();

        item.setItem_date(date);
        item.setItem_disName(disName);

        listViewItemList.add(item);
        notifyDataSetChanged();
    }

    public void init(){
        listViewItemList.clear();
    }

    //http://croute.me/446
    public void onClick(View v){
        int position = (Integer)v.getTag();
        ListViewItem item = listViewItemList.get(position);

        med_adapter = new ListViewAdapter_medicine();

        View dialogView = (View)View.inflate(mcontext, R.layout.record_item_click, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("처방목록");
        builder.setView(dialogView);
        builder.setPositiveButton("확인", null);

        date = item.getItem_date();
        disName = item.getItem_disName();

        //php를 읽어올때 사용할 변수
        GettingPHP gPHP = new GettingPHP();
        gPHP.execute(url);

        TextView tvdate = (TextView)dialogView.findViewById(R.id.textView);
        tvdate.setText(item.getItem_date());

        listview = (ListView)dialogView.findViewById(R.id.record_item_click_listView);
        listview.setAdapter(med_adapter);
        //adapter.addItem(item.getItem_date(), item.getItem_disName());

        builder.show();
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


    class GettingPHP extends AsyncTask<String, Integer, String> { //<Param, Progress, Result>
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        //진저브레드 이상의 안드에서는 메인 쓰레드에서 네트워크 호출을 하면 무조건 에러가 나기 때문에
        //쓰레드를 별도로 하여 php와 연결한다.
        //http://www.androidside.com/bbs/board.php?bo_table=421&wr_id=137
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                //POST(params[0]); //php로 값을 보내는 POST호출

                // URL --> openConnection() --> URLConnection  --> getInputStream --> InputStream (내용읽음)
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection(); //URL내용을 읽어오거나 GET/POST로 전달할 때 사용

                if (conn != null) {
                    //보내기
                    conn.setDefaultUseCaches(false);
                    conn.setDoInput(true);//서버에서 읽기 모드 지정
                    conn.setDoOutput(true);//서버로 쓰기 모드 지정
                    conn.setRequestMethod("POST");

                    // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                    conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("date").append("=").append(date).append("&");
                    buffer.append("disName").append("=").append(disName);

                    OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //받아오기
                    conn.setConnectTimeout(5000); //5초, 어떤 서버로 연결 시 실패할 때를 대비
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            jsonHtml.append(line + "\n");
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


                for (int i = 0; i < results.length(); i++) { //length->child의 갯수
                    JSONObject temp = results.getJSONObject(i);
                    String medName = temp.getString("medName");
                    int onceNum = temp.getInt("onceNum");
                    int dayNum = temp.getInt("dayNum");
                    String notice = temp.getString("notice");
                    med_adapter.addItem(medName, onceNum, dayNum, notice);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
