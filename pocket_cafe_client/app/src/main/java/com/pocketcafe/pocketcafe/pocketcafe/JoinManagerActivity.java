package com.ysw.ysw34.pocketcafe;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import util.IpInfo;

public class JoinManagerActivity extends AppCompatActivity {

    EditText et_id, et_pwd, et_confirmpwd, et_tel, et_nickname, et_storename, et_storeloc;
    TextView tv_id, tv_pwd;
    Button btn_checkid, btn_join;
    String store_lat, store_lng;

    List<Address> address;
    Geocoder coder;

    boolean check_id = false;
    boolean check_pwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_manager);

        coder = new Geocoder( JoinManagerActivity.this );

        et_id = findViewById( R.id.et_id );
        et_pwd = findViewById( R.id.et_pwd );
        et_confirmpwd = findViewById( R.id.et_confirmpwd );
        et_tel = findViewById( R.id.et_tel );
        et_nickname = findViewById( R.id.et_nickname );
        et_storename = findViewById( R.id.et_storename );
        et_storeloc = findViewById( R.id.et_storeloc );

        tv_id = findViewById( R.id.tv_id );
        tv_pwd = findViewById( R.id.tv_pwd );

        btn_checkid = findViewById( R.id.btn_checkid );
        btn_join = findViewById( R.id.btn_join );

        et_id.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                check_id = false;
                String id = et_id.getText().toString();
                tv_id.setTextColor(Color.RED);

                if( id.equals("") ){
                    tv_id.setText("아이디를 입력하세요.");
                }else {
                    tv_id.setText("아이디 중복체크를 해주세요.");
                }

            }
        });

        et_pwd.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String pwd = et_pwd.getText().toString();
                String confirmpwd = et_confirmpwd.getText().toString();

                if( pwd.length() < 8 ){
                    check_pwd = false;
                    tv_pwd.setText("비밀번호는 8자리 이상으로 입력하세요.");
                }else if( !pwd.equals( confirmpwd ) ){
                    check_pwd = false;
                    tv_pwd.setText("비밀번호가 일치하지 않습니다.");
                }else{
                    check_pwd = true;
                    tv_pwd.setText("");
                }

            }
        });

        et_confirmpwd.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String pwd = et_pwd.getText().toString();
                String confirmpwd = et_confirmpwd.getText().toString();

                if( pwd.length() < 8 ){
                    check_pwd = false;
                    tv_pwd.setText("비밀번호는 8자리 이상으로 입력하세요.");
                }else if( !pwd.equals( confirmpwd ) ){
                    check_pwd = false;
                    tv_pwd.setText("비밀번호가 일치하지 않습니다.");
                }else{
                    check_pwd = true;
                    tv_pwd.setText("");
                }

            }
        });

        btn_checkid.setOnClickListener( click );
        btn_join.setOnClickListener( click );

    }//onCreate();

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch ( view.getId() ){

                case R.id.btn_checkid:
                    if( et_id.getText().toString().equals("") ){
                        Toast.makeText(getApplicationContext(), "아이디를 입력 후 중복체크하세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        new CheckIdAsync().execute();
                    }
                    break;

                case R.id.btn_join:

                    if( !check_id ){
                        Toast.makeText( getApplicationContext(), "아이디를 확인하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( !check_pwd ){
                        Toast.makeText( getApplicationContext(), "비밀번호를 확인하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( et_tel.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "휴대전화번호를 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( et_nickname.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "닉네임을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( et_storename.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "가게이름을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( et_storeloc.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "가게위치를 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else{
                        String store_loc = et_storeloc.getText().toString();
                        try {
                            address = coder.getFromLocationName( store_loc, 5 );
                            Address store_address = address.get(0);
                            store_lat = String.format( "%.7f", store_address.getLatitude() );
                            store_lng = String.format( "%.7f", store_address.getLongitude() );
                        } catch (Exception e) {
                            Log.i( "MY", e.toString() );
                        }
                        new JoinManagerAsync().execute();
                    }

                    break;

            }

        }

    };//click

    class CheckIdAsync extends AsyncTask<Void, Void, String> {

        String id = et_id.getText().toString();
        String parameter = "id=" + id;

        String serverip = IpInfo.SERVERIP + "check_id.do";

        String result = "";

        @Override
        protected String doInBackground(Void... voids) {
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

        }//doInBackground()

        @Override
        protected void onPostExecute(String s) {

            if( s.equals("success") ){

                check_id = true;
                tv_id.setText("사용가능한 아이디입니다.");
                tv_id.setTextColor( Color.BLUE );

            }else {

                check_id = false;
                tv_id.setText("이미 존재하는 아이디입니다.");
                tv_id.setTextColor( Color.RED );

            }

        }//onPostExecute()

    }

    class JoinManagerAsync extends AsyncTask<Void, Void, String>{

        String id = et_id.getText().toString();
        String pwd = et_pwd.getText().toString();
        String nickname = et_nickname.getText().toString();
        String tel = et_tel.getText().toString();
        String store_name = et_storename.getText().toString();
        String store_loc = et_storeloc.getText().toString();

        String parameter = "id=" + id + "&pwd=" + pwd + "&nickname=" + nickname + "&tel=" + tel + "&store_name=" + store_name + "&store_loc=" + store_loc + "&store_lat=" + store_lat + "&store_lng=" + store_lng;
        String serverip = IpInfo.SERVERIP + "join_manager.do";

        String result = "";

        @Override
        protected String doInBackground(Void... voids) {

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

        }//doInBackground()

        @Override
        protected void onPostExecute(String s) {

            if( s.equals("success") ){
                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }else if( s.equals("fail_join") ){
                Toast.makeText( getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT ).show();
            }else if( s.equals("fail_store") ){
                Toast.makeText( getApplicationContext(), "가게 등록 실패", Toast.LENGTH_SHORT ).show();
            }

            handler.sendEmptyMessageDelayed(0, 1500);

        }//onPostExecute()
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            Intent i = new Intent( JoinManagerActivity.this, LoginActivity.class );
            startActivity(i);
            finishAffinity();
            System.runFinalization();
            System.exit(0);

        }
    };

}
