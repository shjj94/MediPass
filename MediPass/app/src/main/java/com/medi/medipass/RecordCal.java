package com.medi.medipass;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordCal extends Fragment implements OnDateSelectedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    String url = "http://condi.swu.ac.kr/Prof-Kang/2013111539/medipass/write_app.php";
    GettingPHP gPHP;

    @Bind(R.id.calendarView) MaterialCalendarView widget;

    final ListViewAdapter adapter = new ListViewAdapter();

    private static ArrayList<String> dateArrayList;
    private static ArrayList<String> nameArrayList;
    //String p_date [] = new String[] {"", "", "", "", "", "", "", "", "", ""};
    //String p_name [] = new String[] {"", "", "", "", "", "", "", "", "", ""};
    int num=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_cal, container, false);

        ButterKnife.bind(this, view);
        widget.setOnDateChangedListener(this);

        ListView listview;

        listview = (ListView)view.findViewById(R.id.listview_cal);
        listview.setAdapter(adapter);

        gPHP = new GettingPHP();
        gPHP.execute(url);

        dateArrayList = new ArrayList<String>();
        nameArrayList = new ArrayList<String>();

        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        Log.d("CAL", "selectDate : " + getSelectedDatesString() + "ddd");
        adapter.init();
        for(int i=0;i<num;i++) {
            Log.d("CAL", "selectedDate : " + getSelectedDatesString() + " / phpDate : " + dateArrayList.get(i) + " / phpName : " + nameArrayList.get(i));
            if(dateArrayList.get(i).equals(getSelectedDatesString())) {
                //Log.d("CAL", "같아유 / " + p_date[i]);
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


    class GettingPHP extends AsyncTask<String, Integer, String> { //<Param, Progress, Result>
        String name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //php에서 데이터를 읽어오는 역할 : 필수구현
        @Override
        protected String doInBackground(String... params){
            Log.d("CAL", "doInBackground");
            StringBuilder jsonHtml = new StringBuilder();
            try{
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection(); //URL내용을 읽어오거나 GET/POST로 전달할 때 사용

                if(conn != null){
                    conn.setConnectTimeout(5000); //5초, 어떤 서버로 연결 시 실패할 때를 대비
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
            try{
                //php에서 받아온 JSON데이터를 JSON오브젝트로 변환
                JSONObject jobject = new JSONObject(str);
                //results라는 key는 JSON배열로 되어있다
                JSONArray results = jobject.getJSONArray("results");

                Log.d("CAL", "hhh");

                int j=0;
                for(int i=0;i<results.length();i++){ //length->child의 갯수
                    JSONObject temp = results.getJSONObject(i);
                    Log.d("CAL", "name : " + temp.getString("disease_name") + " date : " + temp.getString("record_date_cal"));
                    dateArrayList.add(temp.getString("record_date_cal"));
                    nameArrayList.add(temp.getString("disease_name"));
                    j++;
                }
                num = j;
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
