package com.ysw.ysw34.pocketcafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchGPSAdapter extends ArrayAdapter<SearchVO> implements AdapterView.OnItemClickListener {
    Context context;
    ArrayList<SearchVO> list;
    SearchVO vo;
    int resource;
    static int gpsLoc;

    public SearchGPSAdapter(Context context, int resource, ArrayList<SearchVO> list, ListView myListView) {
        super(context, resource,list);
        this.context = context;
        this.resource = resource;
        this.list = list;

        myListView.setOnItemClickListener(this);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater linf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = linf.inflate(resource, null);

        TextView storename = convertView.findViewById(R.id.storename);
        TextView openclose = convertView.findViewById(R.id.openclose);
        TextView location = convertView.findViewById(R.id.location);
        TextView tel = convertView.findViewById(R.id.tel);
        TextView km = convertView.findViewById(R.id.km);

        km.setVisibility(View.VISIBLE);

        String kmStr = list.get(position).getKm();
        kmStr = kmStr.substring(0, 4);
        Float kmFloat = Float.parseFloat(kmStr);
        kmFloat /= 10;

        storename.setText(list.get(position).getName());
        openclose.setText("영업시간: " + list.get(position).getOpenclose());
        location.setText("주소: " + list.get(position).getLocation());
        tel.setText("전화번호: " + list.get(position).getTel());
        km.setText("나와의 거리: 약" + String.format("%.3f", kmFloat) + "km");

    /*    gpsLoc = Integer.parseInt((list.get(position).getKm().substring(1,3)))*1000;

        km.setText("나와의 거리:"+"약"+gpsLoc+"m");
*/
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
