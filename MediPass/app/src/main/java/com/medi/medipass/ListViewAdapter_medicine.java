package com.medi.medipass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shjjthjj on 2016-04-14.
 */
public class ListViewAdapter_medicine extends BaseAdapter {
    Context mcontext = null;

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem_medicine> listViewItemList_medicine = new ArrayList<ListViewItem_medicine>();

    //ListViewAdapter의 생성자
    public ListViewAdapter_medicine() {
    }

    //Adapter에 사용되는 데이터의 개수를 리턴 : 필수구현
    @Override
    public int getCount() {
        return listViewItemList_medicine.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 view를 리턴 : 필수구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        mcontext = parent.getContext();

        //listview_item 레이아웃을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_medicine, parent, false);
        }

        //화면에 표시될 view(layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_medName = (TextView) convertView.findViewById(R.id.listview_medName);
        TextView item_onceNum = (TextView) convertView.findViewById(R.id.listview_once_num);
        TextView item_dayNum = (TextView) convertView.findViewById(R.id.listview_day_num);
        TextView item_notice = (TextView) convertView.findViewById(R.id.listview_notice);

        //Data Set(ListViewItem)에서 position에 위치한 데이터 참조 획득
        ListViewItem_medicine listviewItem = listViewItemList_medicine.get(position);

        item_medName.setText(listviewItem.getMedName());
        item_onceNum.setText(String.valueOf(listviewItem.getOnce_num()));
        item_dayNum.setText(String.valueOf(listviewItem.getDay_num()));
        item_notice.setText(listviewItem.getNotice());

        return convertView;
    }

    //지정한 위치(position)에 있는 데이터와 아이템(row)의 ID를 리턴 : 필수구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴 : 필수구현
    @Override
    public Object getItem(int position) {
        return listViewItemList_medicine.get(position);
    }

    //아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String medName, int onceNum, int dayNum, String notice) {
        ListViewItem_medicine item = new ListViewItem_medicine();

        item.setMedName(medName);
        item.setOnce_num(onceNum);
        item.setDay_num(dayNum);
        item.setNotice(notice);

        listViewItemList_medicine.add(item);
        notifyDataSetChanged();
    }

    public void init() {
        listViewItemList_medicine.clear();
    }

}
