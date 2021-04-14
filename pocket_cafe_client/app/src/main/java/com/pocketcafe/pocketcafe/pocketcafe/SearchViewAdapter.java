package com.ysw.ysw34.pocketcafe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchViewAdapter extends ArrayAdapter<SearchVO> implements AdapterView.OnItemClickListener {
    Context context;
    ArrayList<SearchVO> list;
    SearchVO vo;
    int resource;
    Context m;

    public SearchViewAdapter(Context context, int resource, ArrayList<SearchVO> list, ListView myListView) {
        super(context, resource,list);
        this.context = context;
        this.resource = resource;
        this.list = list;



        myListView.setOnItemClickListener(this);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = linf.inflate(resource, null);

        TextView storename = convertView.findViewById(R.id.storename);
        TextView openclose = convertView.findViewById(R.id.openclose);
        TextView location = convertView.findViewById(R.id.location);
        TextView tel = convertView.findViewById(R.id.tel);

        storename.setText(list.get(position).getName());
        openclose.setText("영업시간 : " + list.get(position).getOpenclose());
        location.setText("주소 : " +list.get(position).getLocation());
        tel.setText("전화번호: "+list.get(position).getTel());

        return convertView;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

}

