package com.ysw.ysw34.pocketcafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.List;
import java.util.Locale;

import util.IpInfo;


public class GpsSearchCafeActivity extends AppCompatActivity {

    Button btn_menu,btn_gps, closedrawer;
    TextView txt_gpsAddr, search_keyword, noResult, tv_drawer_greet;
    ListView lv_menu;
    static double gpslat , gpslng ;  //gps 위도경도
    static String provider;
    static String str;  //gps 위도경도 주소로 변환

    ListView gpsListView;
    TextView txtAddr;
    SearchGPSAdapter adapter;
    DrawerLayout layout;
    LinearLayout drawer;

    boolean myLockListView = true; //스크롤링 감지

    SharedPreferences pref;
    int login_idx;

    MemberInfoParser member_parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_search_cafe);

        pref = PreferenceManager.getDefaultSharedPreferences(GpsSearchCafeActivity.this);
        login_idx = pref.getInt("login_idx", 0);

        if( login_idx == 0 ){
            Intent i = new Intent( GpsSearchCafeActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }

        member_parser = new MemberInfoParser();

        btn_menu = findViewById(R.id.btn_menu);
        btn_gps = findViewById(R.id.btn_gps);

        txt_gpsAddr = findViewById(R.id.txt_gpsAddr);
        search_keyword = findViewById(R.id.search_keyword);
        txtAddr = findViewById(R.id.txtAddr);
        gpsListView = findViewById(R.id.gpsListView);
        noResult = findViewById(R.id.noResult);

        layout= findViewById(R.id.layout);
        drawer = findViewById(R.id.drawer);
        closedrawer=findViewById(R.id.closedrawer);
        tv_drawer_greet = findViewById(R.id.tv_drawer_greet);
        lv_menu = findViewById( R.id.lv_menu );

        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ListView gpsListView = findViewById(R.id.gpsListView);

        btn_menu.setOnClickListener(click);
        closedrawer.setOnClickListener(click);

        getGPS();
        GPSAsync task = new GPSAsync();
        try {
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        lv_menu.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent;
                switch (i){

                    case 0:
                        //나의 주문내역 조회
                        intent = new Intent( GpsSearchCafeActivity.this, MyOrderActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;

                    case 1:
                        //개인정보 변경
                        intent = new Intent( GpsSearchCafeActivity.this, ManagerChangeinfoActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;

                    case 2:
                        //비밀번호 변경
                        intent = new Intent( GpsSearchCafeActivity.this, ManagerChangepwdActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;
                }

            }
        });

        //검색어로 검색하기
        search_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GpsSearchCafeActivity.this, SearchCafeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        //gps버튼
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGPS();

                GPSAsync task = new GPSAsync();
                try {
                    task.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new NicknameAsync().execute();

    } // onCreate()

    //드로어 레이아웃
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_menu:
                    layout.openDrawer(drawer);
                    break;
                case R.id.closedrawer:
                    layout.closeDrawer(drawer);
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
                tv_drawer_greet.setText(nickname+"님, 안녕하세요!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class GPSAsync extends AsyncTask<String,Void,ArrayList>{
        String sendMsg, receiveMsg;
        ArrayList<SearchVO> list;

        @Override
        protected void onPostExecute(ArrayList arrayList) {

            noResult.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new SearchGPSAdapter(GpsSearchCafeActivity.this, R.layout.search_list, arrayList, gpsListView);
                gpsListView.setAdapter(adapter);

                gpsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle bundle = new Bundle();
                        bundle.putString("location",list.get(i).getLocation());
                        bundle.putString("tel",list.get(i).getTel());
                        bundle.putString("openclose",list.get(i).getOpenclose());
                        bundle.putString("notice",list.get(i).getNotice());
                        bundle.putString("name",list.get(i).getName());

                        Intent il = new Intent(GpsSearchCafeActivity.this, CafeInfoActivity.class);
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

        @Override
        protected ArrayList doInBackground(String... strings) {
            try {
                String str;
                String serverip = IpInfo.SERVERIP + "SearchGPSAction.do";

                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                // 전송할 데이터. GET 방식으로 작성
                sendMsg = "lat=" + gpslat+"&lng=" +gpslng;

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

                    JSONArray jsonArray = new JSONObject(buffer.toString()).getJSONArray("gpsSearch");
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
                        vo.setKm(jsonObject.getString("distance"));

                        list.add(vo);
                    }
                    Log.i("abc", list.get(0).getName());

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

    }


    public void getGPS(){  // **gps 가져오기 메서드 **

        // Location
        LocationManager lm  = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if( Build.VERSION.SDK_INT >= 23 &&

                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GpsSearchCafeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},

                    0);
        }

        else {
            // locationmanager.getLastKnownLocation () : 가장 최근의 위치 가져오기
            Criteria criteria = new Criteria();
            String pv = lm.getBestProvider(criteria, false);
            Location location  = lm.getLastKnownLocation(pv);

            if( location != null ) {
                txt_gpsAddr.setTextSize(20);
                provider = location.getProvider();
                gpslat = location.getLatitude(); //gps의 현재 위도값
                gpslng = location.getLongitude(); //gps의 현재 경도값
            }

            str = getAddress(gpslat,gpslng);
            txt_gpsAddr.setText(str);
            // requestLocationUPdates(LocationManager, 콜백 최소 시간간격, 최소 변경m, LocationManager)  : 위치 정보 업데이트
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,gpsLocationListener);


        }// if

    }// getGps()

    //gpsLocation
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            provider = location.getProvider();
            gpslat = location.getLongitude();
            gpslng  = location.getLatitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };

    //위도와 경도 기반으로 주소를 반환하는 메서드
    public String getAddress(double lat, double lng) {
        String address = null;

        // 위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> list = null;

        try {
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list == null) {
            Toast.makeText(getApplicationContext(),
                    "주소데이터 얻기 실패", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (list.size() > 0) {
            Address addr = list.get(0);
            address = "";


            if( addr.getAdminArea() != null ){
                address += addr.getAdminArea() + " ";
            }

            if( !(addr.getLocality().equals("서울특별시") || addr.getLocality().equals("부산광역시") || addr.getLocality().equals("대구광역시")
                 || addr.getLocality().equals("인천광역시") || addr.getLocality().equals("광주광역시") || addr.getLocality().equals("대전광역시")
                 || addr.getLocality().equals("울산광역시") || addr.getLocality().equals("Seoul") || addr.getLocality().equals("Busan") || addr.getLocality().equals("Daegu")
                 || addr.getLocality().equals("Incheon") || addr.getLocality().equals("Gwangju") || addr.getLocality().equals("Daejeon") || addr.getLocality().equals("Ulsan")) && addr.getLocality() != null ){
                address += addr.getLocality() + " ";
            }

            if( addr.getThoroughfare() != null ){
                address += addr.getThoroughfare() + " ";
            }

            if( addr.getFeatureName() != null ){
                address += addr.getFeatureName();
            }
        }
        return address;
    }// getAddress()


}
