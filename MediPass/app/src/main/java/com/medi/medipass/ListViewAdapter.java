package com.medi.medipass;

import android.app.AlertDialog;
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
public class ListViewAdapter extends BaseAdapter implements View.OnClickListener {
    Context mcontext = null;

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

    public void addItemv(String date, String name){
        Log.d("PHP", "addItem_cal");
        ListViewItem item = new ListViewItem();

        item.setItem_date(date);
        item.setItem_disName(name);

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

        //Bundle extras = new Bundle();
        //extras.putString("date", item.getItem_date());
        //extras.putString("name", item.getItem_disName());

        //Intent intent = new Intent(mcontext, RecordItemClick.class);
        //intent.putExtras(extras);

        //mcontext.startActivity(intent);

        View dialogView = (View)View.inflate(mcontext, R.layout.record_item_click, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("처방목록");
        builder.setView(dialogView);

        TextView tvdate = (TextView)dialogView.findViewById(R.id.textView);
        TextView tvname = (TextView)dialogView.findViewById(R.id.textView2);

        tvdate.setText(item.getItem_date());
        tvname.setText(item.getItem_disName());

        builder.show();
    }
}
