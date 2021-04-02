package com.ysw.ysw34.pocketcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class MyOrderActivity extends AppCompatActivity {

    Button btn_back;
    ListView lv_orders;
    MyOrderAdapter adapter;

    SharedPreferences pref;
    int login_idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        pref = PreferenceManager.getDefaultSharedPreferences( MyOrderActivity.this );
        login_idx = pref.getInt( "login_idx", 0 );

        if( login_idx == 0 ){
            Intent i = new Intent( MyOrderActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }

        btn_back = findViewById( R.id.btn_back );
        lv_orders = findViewById( R.id.lv_orders );

        btn_back.setOnClickListener( click );

        new MyOrdersAsync().execute();

    }

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch ( view.getId() ){

                case R.id.btn_back:
                    finish();
                    break;

            }

        }

    };

    public class MyOrdersAsync extends AsyncTask<Void, Void, ArrayList<OrdersVO>>{

        @Override
        protected ArrayList<OrdersVO> doInBackground(Void... voids) {

            String parameter = "guest_idx=" + login_idx;
            String serverip = IpInfo.SERVERIP + "orders_info_guest.do";
            ArrayList<OrdersVO> list = new ArrayList<>();

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

                    for( int i = 0; i < jsonArray.length(); i++ ){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OrdersVO vo = new OrdersVO();
                        vo.setStore_idx(jsonObject.getInt("store_idx"));
                        vo.setOrdertime(jsonObject.getString("ordertime"));
                        vo.setOrder_list(jsonObject.getString("order_list").replaceAll(",", "\n"));
                        vo.setPrice(jsonObject.getInt("price"));
                        vo.setDone(jsonObject.getInt("done"));
                        vo.setDonetime(jsonObject.getString("donetime"));

                        list.add(vo);
                    }

                }

            } catch (Exception e) {
                Log.i( "MY", e.toString() );
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<OrdersVO> list) {

            if( list.size() > 0 ){

                adapter = new MyOrderAdapter( MyOrderActivity.this, R.layout.myorder_listview, list );
                lv_orders.setAdapter(adapter);

            }

        }
    }

}
