package com.medi.medipass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/* listView 잘 설명된 블로그 : http://jo.centis1504.net/?p=1009 */
/* http://recipes4dev.tistory.com/43 */

public class RecordList extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.record_list, container, false);

        ListView listview;
        final ListViewAdapter adapter;

        //Adapter생성
        adapter = new ListViewAdapter();

        //리스트뷰 참조 및 Adapter달기
        listview = (ListView)view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        //아이템 추가
        adapter.addItem("2016-01-02", "장염");
        adapter.addItem("2016-01-03", "배탈");
        adapter.addItem("2016-01-04", "설사");

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
}
