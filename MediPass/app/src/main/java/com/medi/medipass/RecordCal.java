package com.medi.medipass;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordCal extends Fragment implements OnDateSelectedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    String url_list = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/write_app.php";
    String url_medicine = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/write_medicine.php";
    GettingPHP gPHP;

    ArrayList<CalendarDay> dates = new ArrayList<>();;

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    final ListViewAdapter adapter = new ListViewAdapter();

    private static ArrayList<String> dateArrayList = new ArrayList<String>();
    private static ArrayList<String> nameArrayList = new ArrayList<String>();
    int num=0;

    ListViewAdapter_medicine med_adapter = new ListViewAdapter_medicine();
    static String date, disName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_cal, container, false);

        ButterKnife.bind(this, view);
        widget.setOnDateChangedListener(this);

        final ListView listview = (ListView)view.findViewById(R.id.listview_cal);
        listview.setAdapter(adapter);

        gPHP = new GettingPHP();
        gPHP.execute(url_list);

        //클릭 이벤트 정의
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                View dialogView = (View)v.inflate(getContext(), R.layout.record_item_click, null);
                ListView listview_med_cal = (ListView)dialogView.findViewById(R.id.record_item_click_listView);
                listview_med_cal.setAdapter(med_adapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("처방목록");
                builder.setView(dialogView);
                builder.setPositiveButton("확인", null);

                date = item.getItem_date();
                disName = item.getItem_disName();

                //php를 읽어올때 사용할 변수
                GettingPHP mPHP = new GettingPHP();
                mPHP.execute(url_medicine);

                TextView tvdate = (TextView)dialogView.findViewById(R.id.textView);
                tvdate.setText(item.getItem_date());

                builder.show();
            }
        });

        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        adapter.init();
        for(int i=0;i<num;i++) {
            if(dateArrayList.get(i).equals(getSelectedDatesString())) {
                adapter.addItem(dateArrayList.get(i), nameArrayList.get(i));
            }
        }
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }


    class GettingPHP extends AsyncTask<String, Integer, String> { //<Param, Progress, Result(doInBackground의 반환값, onPostExcute의 매개변수)>

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //php에서 데이터를 읽어오는 역할, 백그라운드 스레드로 동작해야 하는 작업을 실행한다 : 필수구현
        //execute메서드로 전달한 data tye이 params 인수로 전달되는데 여러개의 인수를 전달할 수 있으므로 배열 타입으로 되어 있다.
        //그래서 하나의 인수만 필요하다면 params[0]만 사용하면 된다.
        @Override
        protected String doInBackground(String... params){
            StringBuilder jsonHtml = new StringBuilder();
            try{
                // URL --> openConnection() --> URLConnection  --> getInputStream --> InputStream (내용읽음)
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection(); //URL내용을 읽어오거나 GET/POST로 전달할 때 사용

                if(conn != null){
                    if(params[0].equals(url_medicine)){
                        String data = "date="+date+"& disName="+disName;
                        Log.d("HHH", "data : " + data);

                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(5000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        bw.write(data);
                        bw.flush();
                        bw.close();

                        //post메세지가 전송된다
                        conn.connect();
                    }

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
            try{
                //php에서 받아온 JSON데이터를 JSON오브젝트로 변환
                JSONObject jobject = new JSONObject(str);
                //results라는 key는 JSON배열로 되어있다
                JSONArray results = jobject.getJSONArray("results");

                if(jobject.get("status").equals("list")) {
                    Log.d("HHH", "list");
                    int j=0;
                    //Calendar calendar = Calendar.getInstance();
                    for(int i=0;i<results.length();i++){ //length->child의 갯수
                        JSONObject temp = results.getJSONObject(i);

                        //http://hyeonstorage.tistory.com/205
                        Calendar calendar = Calendar.getInstance();
                        Log.d("ddd", temp.getString("date_cal").substring(0, 4) + " + " + temp.getString("date_cal").substring(4, 6) + " + " + temp.getString("date_cal").substring(6, 8));
                        calendar.set(Integer.parseInt(temp.getString("date_cal").substring(0, 4)),
                                Integer.parseInt(temp.getString("date_cal").substring(4, 6))-1,
                                Integer.parseInt(temp.getString("date_cal").substring(6, 8)));

                        CalendarDay day = CalendarDay.from(calendar);
                        Log.d("ddd", "day : " + day);

                        dates.add(day);

                        dateArrayList.add(temp.getString("record_date"));
                        nameArrayList.add(temp.getString("disease_name"));
                        j++;
                    }
                    widget.addDecorator(new EventDecorator(Color.RED, dates));
                    num = j;
                }

                if(jobject.get("status").equals("medicine")){
                    Log.d("HHH", "medicine");
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
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}