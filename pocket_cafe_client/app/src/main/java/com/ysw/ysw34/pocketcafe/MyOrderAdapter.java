package com.ysw.ysw34.pocketcafe;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrderAdapter extends ArrayAdapter<OrdersVO> {

    Context context;
    int resource;
    OrdersVO vo;
    ArrayList<OrdersVO> list;
    StoreInfoParser store_parser;

    public MyOrderAdapter(Context context, int resource, ArrayList<OrdersVO> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;

        store_parser = new StoreInfoParser();

    }


    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = linf.inflate( resource, null );

        vo = list.get(position);

        TextView tv_store = convertView.findViewById( R.id.tv_store );
        TextView tv_orderlist = convertView.findViewById( R.id.tv_orderlist );
        TextView tv_ordertime = convertView.findViewById( R.id.tv_ordertime );
        TextView tv_price = convertView.findViewById( R.id.tv_price );
        TextView tv_done = convertView.findViewById( R.id.tv_done );

        tv_orderlist.setText( vo.getOrder_list() );
        tv_ordertime.setText( "주문시간: " + vo.getOrdertime() );
        tv_price.setText( "총 합계: " + vo.getPrice() );

        if( vo.getDone() == 0 ){
            tv_done.setText("미완료");
            tv_done.setTextColor(Color.RED);
        }else if( vo.getDone() == 1 ){
            tv_done.setText("완료 (" + vo.getDonetime() + ")");
            tv_done.setTextColor(Color.BLUE);
        }

        new StoreInfoAsync( tv_store ).execute( vo.getStore_idx() );
        return convertView;

    }

    public class StoreInfoAsync extends AsyncTask<Integer, Void, JSONObject>{

        TextView storeinfo;

        public StoreInfoAsync( TextView storeinfo ){
            this.storeinfo = storeinfo;
        }

        @Override
        protected JSONObject doInBackground(Integer... integers) {
            return store_parser.getStoreInfo_storeidx(integers[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String name = "";
            String location = "";

            try {
                name = jsonObject.getString("name");
                location = jsonObject.getString("location");
                storeinfo.setText( "가게명: " + name + "(" + location + ")" );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
