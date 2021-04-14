package com.ysw.ysw34.pocketcafe;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import util.IpInfo;

public class CafeInfoActivity extends AppCompatActivity {


    Button btn_back, btn_menu, btn_checkmenu, btn_order,closedrawer;
    TextView txt_addr,txt_tel,txt_openclose,txt_notice,logout,txt_name, tv_drawer_greet;
    ImageView img1, img2;
    ListView lv_menu;

    DrawerLayout layout;
    LinearLayout drawer;
    int idx;

    SharedPreferences pref;
    int login_idx;

    MemberInfoParser member_parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_info);

        pref = PreferenceManager.getDefaultSharedPreferences( CafeInfoActivity.this );
        login_idx = pref.getInt("login_idx", 0);

        if( login_idx == 0 ){
            Intent i = new Intent( CafeInfoActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }

        member_parser = new MemberInfoParser();

        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_checkmenu = findViewById(R.id.btn_checkmenu);
        btn_order = findViewById(R.id.btn_order);
        txt_addr = findViewById(R.id.txt_addr);
        txt_tel = findViewById(R.id.txt_tel);
        txt_openclose = findViewById(R.id.txt_openclose);
        txt_notice = findViewById(R.id.txt_notice);
        drawer = findViewById(R.id.drawer);
        layout = findViewById(R.id.layout);
        closedrawer = findViewById(R.id.closedrawer);
        logout = findViewById(R.id.Logout);
        txt_name= findViewById(R.id.txt_name);
        img1 = findViewById( R.id.img1 );
        img2 = findViewById( R.id.img2 );
        tv_drawer_greet = findViewById( R.id.tv_drawer_greet );
        lv_menu = findViewById( R.id.lv_menu );

        new NicknameAsync().execute();

        btn_menu.setOnClickListener(click);
        closedrawer.setOnClickListener(click);
        btn_back.setOnClickListener(click);
        btn_checkmenu.setOnClickListener(click);
        btn_order.setOnClickListener(click);
        logout.setOnClickListener(click);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        String location = bundle.getString("location");
        String tel = bundle.getString("tel");
        String openclose = bundle.getString("openclose");
        String notice = bundle.getString("notice");
        String name = bundle.getString("name");
        String photo1 = bundle.getString("photo1");
        String photo2 = bundle.getString("photo2");
         idx = bundle.getInt("idx");

        txt_name.setText(name);
        txt_addr.setText("주소: "+location);
        txt_tel.setText("전화번호: "+tel);
        txt_openclose.setText("영업시간: "+openclose);
        txt_notice.setText(notice);

        new StorePhotoAsync().execute("img1", photo1);
        new StorePhotoAsync().execute("img2", photo2);

        lv_menu.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent;
                switch (i){

                    case 0:
                        //나의 주문내역 조회
                        intent = new Intent( CafeInfoActivity.this, MyOrderActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;

                    case 1:
                        //개인정보 변경
                        intent = new Intent( CafeInfoActivity.this, ManagerChangeinfoActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        break;

                    case 2:
                        //비밀번호 변경
                        intent = new Intent( CafeInfoActivity.this, ManagerChangepwdActivity.class );
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
            switch(view.getId()){
                case R.id.btn_order:
                    Bundle b = new Bundle();
                    b.putInt("idx",idx);
                    Intent io = new Intent(CafeInfoActivity.this, OrderMenuActivity.class);
                    io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    io.putExtras(b);
                    startActivity(io);
                    break;
                case R.id.btn_checkmenu:
                    Bundle bundle = new Bundle();
                    bundle.putInt("idx",idx);
                    Intent ic = new Intent(CafeInfoActivity.this, CafeMenuInfoActivity.class);
                    ic.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ic.putExtras(bundle);
                    startActivity(ic);
                    break;
                case R.id.btn_menu:
                    layout.openDrawer(drawer);
                    break;
                case R.id.closedrawer:
                    layout.closeDrawer(drawer);
                    break;
                case R.id.btn_back:
                    Intent i = new Intent(CafeInfoActivity.this, SearchCafeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
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
                tv_drawer_greet.setText(nickname+"님, 안녕하세요!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class StorePhotoAsync extends AsyncTask<String, Void, Bitmap>{

        String action;

        @Override
        protected Bitmap doInBackground(String... strings) {

            action = strings[0];

            URL url = null;
            String photo = strings[1];
            Bitmap bitmap = null;

            try {
                // 스트링 주소를 url 형식으로 변환
                url = new URL(IpInfo.SERVERIP + "store_photo/" + photo);
                // url에 접속 시도
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // 스트림 생성
                InputStream is = conn.getInputStream();
                // 스트림에서 받은 데이터를 비트맵 변환
                // 인터넷에서 이미지 가져올 때는 Bitmap을 사용해야함
                bitmap = BitmapFactory.decodeStream(is);

                // 연결 종료
                is.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if( bitmap != null ){

                if( action.equals("img1") ){
                    img1.setImageBitmap(bitmap);
                    img1.setVisibility(View.VISIBLE);
                }

                if( action.equals("img2")){
                    img2.setImageBitmap(bitmap);
                    img2.setVisibility(View.VISIBLE);
                }

            }

        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            Intent i = new Intent( CafeInfoActivity.this, LoginActivity.class );
            startActivity(i);
            finishAffinity();
            System.runFinalization();
            System.exit(0);

        }
    };

}
