package com.ysw.ysw34.pocketcafe;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText et_id, et_pwd;
    Button btn_login;
    TextView btn_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById( R.id.et_id );
        et_pwd = findViewById( R.id.et_pwd );
        btn_login = findViewById( R.id.btn_login );
        btn_join = findViewById( R.id.btn_join );

        btn_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = et_id.getText().toString();
                String pwd = et_pwd.getText().toString();

                if( !id.equals("") && !pwd.equals("") ){

                    new LoginAsync().execute();

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

    class LoginAsync extends AsyncTask<Void, Void, String>{

        String id = et_id.getText().toString();
        String pwd = et_pwd.getText().toString();

        String parameter = "id=" + id + "&pwd=" + pwd;
        String ip = "192.168.105.89";
        String serverip = "http://" + ip + ":9090/PocketCafeJSP/login.do";

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

                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

            }else if( s.equals("fail_id") ){
                Toast.makeText( getApplicationContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT ).show();
            }else if( s.equals("fail_pwd") ){
                Toast.makeText( getApplicationContext(), "비밀번호를 확인하세요.", Toast.LENGTH_SHORT ).show();
            }

        }//onPostExecute()

    }

}
