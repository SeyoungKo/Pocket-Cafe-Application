package com.ysw.ysw34.pocketcafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

public class SearchCafeActivity extends AppCompatActivity {

    ListView myListView, lv_menu;
    Button btn_menu, btn_search, closedrawer;
    EditText et_search;
    TextView txt_gpsAddr, search_gps, noResult, tv_drawer_greet, logout; //search_gps : 버튼처럼 clickable임
    SearchViewAdapter adapter;

    int start = 1; //검색은 1부터
    static String name;

    DrawerLayout layout;
    LinearLayout drawer;


    boolean myLockListView = true; // 스크롤링 감지

    SharedPreferences pref;
    int login_idx;

    MemberInfoParser member_parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cafe);

        pref = PreferenceManager.getDefaultSharedPreferences(SearchCafeActivity.this);
        login_idx = pref.getInt("login_idx", 0);

        if( login_idx == 0 ){
            Intent i = new Intent( SearchCafeActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }

        member_parser = new MemberInfoParser();

        btn_menu = findViewById(R.id.btn_menu);
        btn_search = findViewById(R.id.btn_search);
        myListView = findViewById(R.id.myListView);
        lv_menu = findViewById( R.id.lv_menu );
        et_search = findViewById(R.id.et_search);
        //txt_gpsAddr = findViewById(R.id.txt_gpsAddr);
        search_gps = findViewById(R.id.search_gps);
        layout = findViewById(R.id.layout);
        drawer = findViewById(R.id.drawer);
        closedrawer = findViewById(R.id.closedrawer);
        noResult = findViewById(R.id.noResult);
        logout = findViewById(R.id.Logout);
        tv_drawer_greet = findViewById( R.id.tv_drawer_greet );

        btn_search.setOnClickListener(click);
        search_gps.setOnClickListener(click);
        btn_menu.setOnClickListener(click);
        closedrawer.setOnClickListener(click);
        logout.setOnClickListener( click );

        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        new NicknameAsync().execute();

        lv_menu.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                layout.closeDrawer(drawer);
                Intent intent;
                switch (i){

                    case 0:
                        //나의 주문내역 조회
                        intent = new Intent( SearchCafeActivity.this, MyOrderActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;

                    case 1:
                        //개인정보 변경
                        intent = new Intent( SearchCafeActivity.this, ManagerChangeinfoActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;

                    case 2:
                        //비밀번호 변경
                        intent = new Intent( SearchCafeActivity.this, ManagerChangepwdActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;

                }

            }
        });

    }//onCreate()

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_search:
                    //검색어로 검색
                    if (!et_search.getText().toString().equals("")) {
                      start = 1;
                        adapter = null;

                        name = et_search.getText().toString(); //보낼 값
                        SearchAsync task = new SearchAsync();
                        try {
                            task.execute(name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText( getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }
                 break;

                case R.id.search_gps:
                    // 나와 가까운 카페(gps)
                    Intent i = new Intent(SearchCafeActivity.this, GpsSearchCafeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);

                    break;

                case R.id.btn_menu:
                    layout.openDrawer(drawer);
                    break;

                case R.id.closedrawer:
                    layout.closeDrawer(drawer);
                    break;

                case R.id.Logout:
                    SharedPreferences.Editor editor = pref.edit();

                    editor.remove("login_id");
                    editor.remove("login_pwd");
                    editor.remove("login_idx");

                    editor.commit();

                    Toast.makeText( getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT ).show();
                    handler.sendEmptyMessageDelayed( 0, 500 );

                    break;

            }

        }
    };

    public class NicknameAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return member_parser.getMemberInfo(login_idx);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String nickname;

            try {
                nickname = jsonObject.getString("nickname");
                tv_drawer_greet.setText(nickname + "님, 안녕하세요!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class SearchAsync extends AsyncTask<String, Void, ArrayList> {
        String sendMsg, receiveMsg;
        ArrayList<SearchVO> list;


        @Override
        protected void onPostExecute(ArrayList arrayList) {
         /*   Log.i("array", String.valueOf(arrayList.size()));*/

            noResult.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new SearchViewAdapter(SearchCafeActivity.this, R.layout.search_list, arrayList, myListView);
               myListView.setAdapter(adapter);

               myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       Bundle bundle = new Bundle();
                       bundle.putString("location",list.get(i).getLocation());
                       bundle.putString("tel",list.get(i).getTel());
                       bundle.putString("openclose",list.get(i).getOpenclose());
                       bundle.putString("notice",list.get(i).getNotice());
                       bundle.putString("name",list.get(i).getName());
                       bundle.putInt("idx",list.get(i).getIdx());
                       bundle.putString("photo1", list.get(i).getPhoto1());
                       bundle.putString("photo2", list.get(i).getPhoto2());

                       Intent il = new Intent(SearchCafeActivity.this, CafeInfoActivity.class);
                       il.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        il.putExtras(bundle);
                        startActivity(il);

                   }
               });
            }
            adapter.notifyDataSetChanged();
            myLockListView = false;

            if(list.size()==0){
                noResult.setVisibility(View.VISIBLE);
            }

        }

        protected ArrayList doInBackground(String... strings) {
            try {
                String str;
                String serverip = IpInfo.SERVERIP + "SearchKeyword.do";

                // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
                //URL url = new URL("http://192.168.113.1:9090/PocketCafeJSP/SearchKeyword.do");
                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                // 전송할 데이터. GET 방식으로 작성
                sendMsg = "name=" + strings[0];

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

                    JSONArray jsonArray = new JSONObject(buffer.toString()).getJSONArray("search");
                    JSONObject jsonObject = null;

                    list = new ArrayList();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        SearchVO vo = new SearchVO();

                        vo.setManager_idx(jsonObject.getInt("manager_idx"));
                        vo.setName(jsonObject.getString("name"));
                        vo.setIdx(jsonObject.getInt("idx"));
                        vo.setTel(jsonObject.getString("tel"));
                        vo.setNotice(jsonObject.getString("notice"));
                        vo.setOpenclose(jsonObject.getString("openclose"));
                        vo.setLocation(jsonObject.getString("location"));
                        vo.setPhoto1(jsonObject.getString("photo1"));
                        vo.setPhoto2(jsonObject.getString("photo2"));

                        list.add(vo);
                    }
                    Log.i("abc", list.get(0).getName());

                  /*  runOnUiThread(new Runnable() {
                        @Override           //메인이 갱신되는 시점
                        public void run() {
                            txtList.setText(result[0]);  //텍스트만 갱신하려면
                        }
                    });*/

                    receiveMsg = buffer.toString();
                } else {
                    // 통신 실패
                }
            } catch (Exception e) {
                Log.i("aaa", e.getMessage());
                e.printStackTrace();
            }

            //jsp로부터 받은 리턴 값
            return list; // vo저장한 list반환
        } // doInBackground()

    }//Async

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            Intent i = new Intent( SearchCafeActivity.this, LoginActivity.class );
            startActivity(i);
            finishAffinity();
            System.runFinalization();
            System.exit(0);

        }
    };

}


