package com.ysw.ysw34.pocketcafe;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import util.Action;
import util.IpInfo;

public class ManagerChangeinfoActivity extends AppCompatActivity {

    Button btn_back, btn_change;
    static EditText et_nickname, et_tel;
    static Dialog dialog;
    ProgressBar progress;

    SharedPreferences pref;
    String id;

    MemberInfoParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_changeinfo);

        parser = new MemberInfoParser();

        dialog = new Dialog( ManagerChangeinfoActivity.this, R.style.Dialog );
        dialog.setContentView( R.layout.progress_dialog );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        progress = dialog.findViewById( R.id.progress );
        progress.setIndeterminate(true);
        progress.getIndeterminateDrawable().setColorFilter( Color.rgb( 233, 233, 233 ), PorterDuff.Mode.MULTIPLY );

        dialog.show();

        pref = PreferenceManager.getDefaultSharedPreferences( ManagerChangeinfoActivity.this );
        id = pref.getString( "login_id", "" );

        if( id.equals("") ){
            Intent i = new Intent( ManagerChangeinfoActivity.this, LoginActivity.class );
            startActivity(i);
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }

        new getMemberInfoAsync().execute();

        btn_back = findViewById( R.id.btn_back );
        btn_change = findViewById( R.id.btn_change );

        et_nickname = findViewById( R.id.et_nickname );
        et_tel = findViewById( R.id.et_tel );

        btn_back.setOnClickListener( click );
        btn_change.setOnClickListener( click );

    }//onCreate();

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch ( view.getId() ){

                case  R.id.btn_back:
                    finish();
                    break;

                case R.id.btn_change:
                    String nickname = et_nickname.getText().toString();
                    String tel = et_tel.getText().toString();
                    new ChangeManagerInfoAsync().execute( id, nickname, tel );
                    break;

            }

        }

    };

    public  class getMemberInfoAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return parser.getMemberInfo(id);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String nickname = "";
            String tel = "";

            try {
                nickname = jsonObject.getString("nickname");
                tel = jsonObject.getString("tel");

                et_nickname.setText(nickname);
                et_tel.setText(tel);

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }

    }

    public class ChangeManagerInfoAsync extends AsyncTask<String, Void, String> {

        String nickname;

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            String parameter = "id=" + strings[0] + "&nickname=" + strings[1] + "&tel=" + strings[2];
            nickname = strings[1];
            String serverip = IpInfo.SERVERIP + "change_memberinfo.do";

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

        }

        @Override
        protected void onPostExecute(String s) {

            if( s.equals("success") ){
                ManagerMainActivity.tv_greet.setText( nickname + "님, 안녕하세요!" );
                Toast.makeText( getApplicationContext(), "개인정보가 변경되었습니다.", Toast.LENGTH_SHORT ).show();
            }else{
                Toast.makeText( getApplicationContext(), "변경 실패", Toast.LENGTH_SHORT ).show();
            }

            handler_finish.sendEmptyMessageDelayed(0, 800);

        }

    }

    Handler handler_finish = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            finish();
        }

    };

}
