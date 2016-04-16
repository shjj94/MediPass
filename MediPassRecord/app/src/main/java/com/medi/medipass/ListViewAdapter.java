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
 * Created by shjjthjj on 2016-04-14.
 */
public class ListViewAdapter extends BaseAdapter {

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
    public View getView(int position, View convertview, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //listview_item 레이아웃을 inflate하여 convertView 참조 획득
        if (convertview == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.listview_item, parent, false);
        }

        //화면에 표시될 view(layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_date = (TextView) convertview.findViewById(R.id.item_date);
        TextView item_disName = (TextView) convertview.findViewById(R.id.item_disName);

        //Data Set(ListViewItem)에서 position에 위치한 데이터 참조 획득
        ListViewItem listviewItem = listViewItemList.get(position);

        //아이템 내 각 위젯이 데이터 반영
        item_date.setText(listviewItem.getItem_date());
        item_disName.setText(listviewItem.getItem_disName());

        return convertview;
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
    }
}
