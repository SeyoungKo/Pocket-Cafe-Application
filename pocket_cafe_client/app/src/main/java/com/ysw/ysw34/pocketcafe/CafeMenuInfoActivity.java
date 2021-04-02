package com.ysw.ysw34.pocketcafe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class CafeMenuInfoActivity extends AppCompatActivity {

    GridView gridView;
    Button btn_back, btn_ok;
    static int idx;

    CafeMenuInfoAdapter adapter;
    boolean myLockListView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_menu_info);

        btn_back = findViewById(R.id.btn_back);
        btn_ok = findViewById(R.id.btn_ok);
        gridView = findViewById(R.id.gridView);

        btn_back.setOnClickListener(click);
        btn_ok.setOnClickListener(click);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        idx = bundle.getInt("idx");

        CafeMenuInfoAsync task = new CafeMenuInfoAsync();
        try{
            task.execute(String.valueOf(idx));
        }catch(Exception e){
            e.printStackTrace();
        }

    }//onCreate()

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_ok:
                    Bundle b = new Bundle();
                    b.putInt("idx",idx);
                    Intent i = new Intent(CafeMenuInfoActivity.this, OrderMenuActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtras(b);
                    startActivity(i);
                    break;
                
                case R.id.btn_back:
                    Intent i2 = new Intent(CafeMenuInfoActivity.this, CafeInfoActivity.class);
                    i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i2);
                    break;
            }
        }
    };

    class CafeMenuInfoAsync extends AsyncTask<String, Void, ArrayList> {
        String sendMsg, receiveMsg;
        ArrayList<MenuVO> list;

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            //*   Log.i("array", String.valueOf(arrayList.size()));*//*

            //  noResult.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new CafeMenuInfoAdapter(CafeMenuInfoActivity.this, R.layout.cafemenuinfo_item, arrayList, gridView);
                gridView.setAdapter(adapter);

            }
            adapter.notifyDataSetChanged();
            myLockListView = false;

            if(list.size()==0){
                //   noResult.setVisibility(View.VISIBLE);
            }
        }

        protected ArrayList doInBackground(String... strings) {
            try {
                String str;
                String serverip = IpInfo.SERVERIP + "menu_info.do";

                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "idx="+idx;

                osw.write(sendMsg);
                osw.flush();

                //jsp와 통신 성공 시 수행
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();


                    // jsp에서 보낸 값을 받는 부분
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }

                    JSONArray jsonArray = new JSONArray(buffer.toString());

                    list = new ArrayList();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MenuVO vo = new MenuVO();

                        vo.setName(jsonObject.getString("name"));
                        vo.setPhoto(jsonObject.getString("photo"));

                        list.add(vo);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    // 통신 실패
                }
            } catch (Exception e) {
                Log.i("aaa", e.getMessage());
                e.printStackTrace();
            }
            return list;
        } // doInBackground()

    }//Async

}