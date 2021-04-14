package com.ysw.ysw34.pocketcafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class OrderAdapter extends ArrayAdapter<OrdersVO> {

    Context context;
    int resource;
    ArrayList<OrdersVO> list;
    OrdersVO vo;
    ListView order_list;
    MemberInfoParser member_parser;

    public OrderAdapter(Context context, int resource, ArrayList<OrdersVO> list, ListView order_list) {
        super(context, resource, list);

        this.context = context;
        this.list = list;
        this.resource = resource;
        this.order_list = order_list;

        this.order_list.setOnItemClickListener(click);
        member_parser = new MemberInfoParser();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );

        convertView = linf.inflate( resource, null );

        vo = list.get(position);

        TextView order_time = convertView.findViewById(R.id.order_time);
        TextView tv_guest = convertView.findViewById( R.id.tv_guest );
        TextView order_what = convertView.findViewById(R.id.order_what);
        TextView tv_price = convertView.findViewById( R.id.tv_price );
        TextView tv_done = convertView.findViewById(R.id.tv_done);

        order_time.setText("주문시간 : " + list.get(position).getOrdertime());
        order_what.setText(list.get(position).getOrder_list());
        tv_price.setText( list.get(position).getPrice() + "원");

        if( list.get(position).getDone() == 1 ) {
            tv_done.setText("완료 (" + list.get(position).getDonetime() + ")");
            tv_done.setTextColor(Color.BLUE);
        }else if( list.get(position).getDone() == 0 ){
            tv_done.setText("미완료");
            tv_done.setTextColor(Color.RED);
        }

        new GuestInfoAsync( tv_guest ).execute(list.get(position).getGeust_idx());

        return convertView;

    }//getView()

    AdapterView.OnItemClickListener click = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {

            final int position = i;

            if( list.get(position).getDone() == 0 ) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("주문 처리 완료");
                builder.setMessage("고객에게 음료가 모두 전달 완료된 주문입니까?");

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new orderFinish().execute(list.get(position).getOrders_idx());
                    }
                });

                builder.setNegativeButton("아니요", null);

                builder.show();
            }

        }
    };

    class orderFinish extends AsyncTask<Integer, Void, String> {

        String parameter;
        String serverip = IpInfo.SERVERIP + "orderFinish.do";

        int order_idx;

        @Override
        protected String doInBackground(Integer... integers) {

            order_idx = integers[0];
            parameter = "idx=" + order_idx;
            String serverip = IpInfo.SERVERIP + "orderFinish.do";
            String result = "";

            try {
                String str;
                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                osw.write( parameter );
                osw.flush();


                if( conn.getResponseCode() == conn.HTTP_OK ) {

                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }

                    JSONArray jsonArray = new JSONArray(buffer.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    result = jsonObject.getString("result");

                }

            } catch (Exception e) {
                Log.i( "MY", e.toString() );
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            if( s.equals("success") ){
                Toast.makeText( context, "주문처리완료", Toast.LENGTH_SHORT ).show();
            }else{
                Toast.makeText( context, "주문처리완료실패", Toast.LENGTH_SHORT ).show();
            }

        }
    }

    public class GuestInfoAsync extends AsyncTask<Integer, Void, JSONObject>{

        TextView guestinfo;

        public GuestInfoAsync( TextView guestinfo ){
            this.guestinfo = guestinfo;
        }

        @Override
        protected JSONObject doInBackground(Integer... integers) {
            return member_parser.getMemberInfo(integers[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String nickname = "";
            String tel = "";

            try {
                nickname = jsonObject.getString("nickname");
                tel = jsonObject.getString("tel");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            guestinfo.setText( "회원닉네임: " + nickname + "/ 회원전화번호: " + tel);

        }
    }

}
