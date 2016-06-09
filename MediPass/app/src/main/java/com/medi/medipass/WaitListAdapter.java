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
 * Created by Elizabeth on 2016-05-31.
 */
public class WaitListAdapter extends BaseAdapter {
    Context mcontext = null;

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<WaitListItem> waitItemList = new ArrayList<WaitListItem>();

    //WaitListAdapter 생성자
    public WaitListAdapter() {
    }

    //Adapter에 사용되는 데이터의 개수를 리턴 : 필수구현
    @Override
    public int getCount() {
        return waitItemList.size();
    }

    //지정한 위치(position)에 있는 데이터와 아이템(row)의 ID를 리턴 : 필수구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴 : 필수구현
    @Override
    public Object getItem(int position) {
        return waitItemList.get(position);
    }


    //아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String hosname, String waitnum) {
        Log.d("PHP", "addItem");
        WaitListItem item = new WaitListItem(hosname, waitnum);

        item.setHosname(hosname);
        item.setHoswait(waitnum);

        waitItemList.add(item);
        notifyDataSetChanged();
    }

    public void init() {
        waitItemList.clear();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 view를 리턴 : 필수구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        mcontext = parent.getContext();

        //listview_item 레이아웃을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.wait_item, parent, false);

            convertView.setTag(position);
        }

        //화면에 표시될 view(layout이 inflate된)으로부터 위젯에 대한 참조 획득
        //waithos, waitnum
        TextView item_hosname = (TextView) convertView.findViewById(R.id.waithos);
        TextView item_waitnum = (TextView) convertView.findViewById(R.id.waitnum);

        //Data Set(ListViewItem)에서 position에 위치한 데이터 참조 획득
        WaitListItem listviewItem = waitItemList.get(position);

        //아이템 내 각 위젯이 데이터 반영
        item_hosname.setText(listviewItem.getHosname());
        item_waitnum.setText(listviewItem.getHoswait());

        return convertView;
    }


}
