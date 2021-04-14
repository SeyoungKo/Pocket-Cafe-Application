package com.ysw.ysw34.pocketcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import util.IpInfo;

public class ManagerChangepwdActivity extends AppCompatActivity {

    EditText et_pwd, et_newpwd, et_confirmpwd;
    TextView tv_pwd, tv_newpwd;
    Button btn_back, btn_change;

    boolean check_pwd = false;
    boolean check_newpwd = false;

    SharedPreferences pref;

    String login_id, login_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_changepwd);

        et_pwd = findViewById( R.id.et_pwd );
        et_newpwd = findViewById( R.id.et_newpwd );
        et_confirmpwd = findViewById( R.id.et_confirmpwd );

        tv_pwd = findViewById( R.id.tv_pwd );
        tv_newpwd = findViewById( R.id.tv_newpwd );

        btn_back = findViewById( R.id.btn_back );
        btn_change = findViewById( R.id.btn_change );

        pref = PreferenceManager.getDefaultSharedPreferences( ManagerChangepwdActivity.this );
        login_id = pref.getString("login_id", "");
        login_pwd = pref.getString("login_pwd", "");

        if( login_id.equals("") || login_pwd.equals("") ){
            Intent i = new Intent( ManagerChangepwdActivity.this, LoginActivity.class );
            startActivity(i);
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }

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
                if( pwd.equals( login_pwd ) ){
                    check_pwd = true;
                    tv_pwd.setText("");
                }else{
                    check_pwd = false;
                    tv_pwd.setText("현재 비밀번호가 일치하지 않습니다.");
                }

            }
        });

        et_newpwd.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String new_pwd = et_newpwd.getText().toString();
                String confirm_pwd = et_confirmpwd.getText().toString();

                if( new_pwd.equals( confirm_pwd ) ){
                    check_newpwd = true;
                    tv_newpwd.setText("");
                }else{
                    check_newpwd = false;
                    tv_newpwd.setText("새로운 비밀번호가 일치하지 않습니다.");
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

                String new_pwd = et_newpwd.getText().toString();
                String confirm_pwd = et_confirmpwd.getText().toString();

                if( new_pwd.equals( confirm_pwd ) ){
                    check_newpwd = true;
                    tv_newpwd.setText("");
                }else{
                    check_newpwd = false;
                    tv_newpwd.setText("새로운 비밀번호가 일치하지 않습니다.");
                }

            }
        });

        btn_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_change.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( check_pwd && check_newpwd ){

                    String new_pwd = et_newpwd.getText().toString();
                    new ChangeManagerPwdAsync().execute( login_id, new_pwd );

                }

            }
        });

    }//onCreate();

    public class ChangeManagerPwdAsync extends AsyncTask<String, Void, String>{

        String new_pwd = "";

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String parameter = "id=" + strings[0] + "&pwd=" + strings[1];
            String serverip = IpInfo.SERVERIP + "change_memberpwd.do";
            new_pwd = strings[1];

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

        }//doInBackground();

        @Override
        protected void onPostExecute(String s) {

            if( s.equals("success") ){
                Toast.makeText( getApplicationContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT ).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("login_pwd", new_pwd);
                editor.commit();
            }else{
                Toast.makeText( getApplicationContext(), "변경 실패", Toast.LENGTH_SHORT ).show();
            }

            handler_finish.sendEmptyMessageDelayed(0, 800);

        }//onPostExecute();
    }

    Handler handler_finish = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            finish();
        }

    };

}
