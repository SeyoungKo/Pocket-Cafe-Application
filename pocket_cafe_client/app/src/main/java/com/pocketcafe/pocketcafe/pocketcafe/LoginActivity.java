package com.ysw.ysw34.pocketcafe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import util.IpInfo;

public class LoginActivity extends AppCompatActivity {

    EditText et_id, et_pwd;
    Button btn_login;
    TextView btn_join;

    int division;
    int idx;

    SharedPreferences pref;
    String saved_id;
    String saved_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ActivityCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ){
            setPermission();
            return;
        }

        et_id = findViewById( R.id.et_id );
        et_pwd = findViewById( R.id.et_pwd );
        btn_login = findViewById( R.id.btn_login );
        btn_join = findViewById( R.id.btn_join );

        pref = PreferenceManager.getDefaultSharedPreferences( LoginActivity.this );
        saved_id = pref.getString( "login_id", "" );
        saved_pwd = pref.getString("login_pwd", "");


        if( !saved_id.equals("") && !saved_pwd.equals("") ){

            new LoginAsync().execute(saved_id, saved_pwd);

        }

        btn_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input_id = et_id.getText().toString();
                String input_pwd = et_pwd.getText().toString();

                if( !input_id.equals("") && !input_pwd.equals("") ){

                    new LoginAsync().execute(input_id, input_pwd);

                }else{
                    Toast.makeText( getApplicationContext(), "아이디와 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT ).show();
                }

            }
        });

        btn_join.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent( LoginActivity.this, JoinSelectActivity.class );
                i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                startActivity( i );

            }
        });

    }//onCreate()

    PermissionListener permissionListener = new PermissionListener() {

        @Override
        public void onPermissionGranted() {
            //모든 권한이 수락되었을 경우
            Intent i = new Intent( LoginActivity.this, LoginActivity.class );
            startActivity(i);
            finish();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //한가지라도 수락되지 않은 권한이 있는 경우
            finish();
        }

    };//permissionListener

    private void setPermission(){

        TedPermission.with( this )
                .setPermissionListener( permissionListener )
                .setDeniedMessage( "이 앱에서 요구하는 권한이 있습니다.\n[설정]->[권한]에서 해당 권한을 활성화 해주세요." )
                .setPermissions( Manifest.permission.READ_EXTERNAL_STORAGE )
                .check();

    }//setPermission()

    class LoginAsync extends AsyncTask<String, Void, String>{

        String parameter;
        String serverip = IpInfo.SERVERIP + "login.do";

        String result = "";

        String id = "";
        String pwd = "";

        @Override
        protected String doInBackground(String... strings) {

            id = strings[0];
            pwd = strings[1];

            parameter = "id=" + id + "&pwd=" + pwd;

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
                    division = jsonObject.getInt("division");
                    idx = jsonObject.getInt("idx");
                }

            } catch (Exception e) {
                Log.i( "MY", e.toString() );
            }

            return result;

        }//doInBackground()

        @Override
        protected void onPostExecute(String s) {

            if( s.equals("success") ){

                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                //로그인 정보 SharedPreferences에 저장
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("login_id", id);
                editor.putString( "login_pwd", pwd );
                editor.putInt( "login_idx", idx );
                editor.commit();

                if( division == 1 ){
                    Intent i = new Intent( LoginActivity.this, SearchCafeActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    finish();
                }

                if( division == 2 ){
                    Intent i = new Intent( LoginActivity.this, ManagerMainActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    finish();
                }

            }else if( s.equals("fail_id") ){
                Toast.makeText( getApplicationContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT ).show();
            }else if( s.equals("fail_pwd") ){
                Toast.makeText( getApplicationContext(), "비밀번호를 확인하세요.", Toast.LENGTH_SHORT ).show();
            }

        }//onPostExecute()

    }

}
