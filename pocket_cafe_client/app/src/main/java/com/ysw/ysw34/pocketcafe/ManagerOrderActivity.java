package com.ysw.ysw34.pocketcafe;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import util.Action;
import util.IpInfo;

public class ManagerOrderActivity extends AppCompatActivity {

    ListView order_list;
    Button btn_back;
    OrderAdapter adapter = null;
    OrdersVO vo;

    int login_idx;
    static String result = "";

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_order);

        order_list = findViewById(R.id.order_list);
        btn_back = findViewById( R.id.btn_back );

        pref = PreferenceManager.getDefaultSharedPreferences( ManagerOrderActivity.this );
        login_idx = pref.getInt("login_idx", 0);

        new SearchStore().execute("" + login_idx);

        btn_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Handler.sendEmptyMessageDelayed(0, 30000);

    }

    public class OrderSearch extends AsyncTask<String, Void, ArrayList<OrdersVO>> {

        String sendMsg;
        ArrayList<OrdersVO> list;

        @Override
        protected ArrayList doInBackground(String... strings) {

            try {
                String str;
                String serverip = IpInfo.SERVERIP + "order_info.do";
                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "store_idx=" + strings[0];

                osw.write( sendMsg );
                osw.flush();


                if( conn.getResponseCode() == conn.HTTP_OK ) {

                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }

                    vo = new OrdersVO();

                    JSONArray jsonArray = new JSONArray(buffer.toString());
                    JSONObject jsonObject = null;

                    list = new ArrayList();

                    for(int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        Log.i("aaaa", "jsonObject" + jsonObject);
                        vo = new OrdersVO();

                        vo.setOrders_idx(jsonObject.getInt("orders_idx"));
                        vo.setGeust_idx( jsonObject.getInt("guest_idx") );
                        vo.setOrdertime(jsonObject.getString("ordertime"));
                        vo.setOrder_list(jsonObject.getString("order_list"));
                        vo.setPrice(jsonObject.getInt("price"));
                        vo.setDone(jsonObject.getInt("done"));
                        vo.setDonetime(jsonObject.getString("donetime"));

                        list.add(vo);

                    }

                    Log.i( "aaaa", "list : " + list );
                }

            } catch (Exception e) {
                Log.i( "aaaa", e.getMessage() );
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList list) {

            adapter = new OrderAdapter(ManagerOrderActivity.this, R.layout.order_form, list, order_list);
            order_list.setAdapter(adapter);
            handler.sendEmptyMessageDelayed(0, 20000);

        }
    }

    // store_idx 찾기
    class SearchStore extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

            JSONObject jsonObject = null;
            String parameter = "login_idx=" + login_idx;
            String serverip = IpInfo.SERVERIP + "search_store.do";

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
                    jsonObject = jsonArray.getJSONObject(0);
                    jsonObject.put("action", strings[1]);
                }

            } catch (Exception e) {
                Log.i( "MY", e.toString() );
                Log.i("aaaa", "jsonObject1 : " + jsonObject);
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                result = jsonObject.getString("store_idx");

                Log.i("aaaa", "result : " + result);

                new OrderSearch().execute("" + result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            new OrderSearch().execute("" + result);
        }

    };

}
