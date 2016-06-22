package com.medi.medipass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nara on 2016-05-18.
 */

public class SubmitAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<SubmitItem> data;
    private int layout;
    Context mcontext = null;

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<SubmitItem> submitItemList = new ArrayList<SubmitItem>();

    //SubmitAdapter의 생성자
    public SubmitAdapter() {
    }


    @Override
    public int getCount() {
        return submitItemList.size();
    }


    //지정한 위치(position)에 있는 데이터와 아이템(row)의 ID를 리턴 : 필수구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴 : 필수구현
    @Override
    public Object getItem(int position) {
        return submitItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String vTitle, String vDate, String vHospital, String vDisease, String vPresnum) {
        Log.d("PHP", "addItem");
        SubmitItem item = new SubmitItem(vTitle, vDate, vHospital, vDisease, vPresnum);

        item.setTitle(vTitle);
        item.setDate(vDate);
        item.setHospital(vHospital);
        item.setDisease(vDisease);
        item.setPrescription(vPresnum);

        submitItemList.add(item);
        notifyDataSetChanged();
    }

    public void init() {
        submitItemList.clear();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        mcontext = parent.getContext();

        //listview_item 레이아웃을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.submit_item, parent, false);

            convertView.setTag(position);
        }

        //화면에 표시될 view(layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView date = (TextView) convertView.findViewById(R.id.tv_date);
        TextView hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
        TextView disease = (TextView) convertView.findViewById(R.id.tv_disease);
        TextView presnum = (TextView) convertView.findViewById(R.id.tv_presnum);

        //Data Set(ListViewItem)에서 position에 위치한 데이터 참조 획득
        SubmitItem listviewItem = submitItemList.get(position);


        //아이템 내 각 위젯이 데이터 반영
        title.setText(listviewItem.getTitle());
        date.setText(listviewItem.getDate());
        hospital.setText(listviewItem.getHospital());
        disease.setText(listviewItem.getDisease());
        presnum.setText(listviewItem.getPrescription());


        return convertView;
    }


}