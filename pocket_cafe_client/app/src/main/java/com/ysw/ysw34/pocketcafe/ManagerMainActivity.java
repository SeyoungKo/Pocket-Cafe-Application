package com.ysw.ysw34.pocketcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import util.Action;

public class ManagerMainActivity extends AppCompatActivity {

    static TextView tv_greet;
    TextView tv_logout;
    Button btn_morder, btn_mstore, btn_mmenu, btn_mid;

    SharedPreferences pref;
    String id;

    MemberInfoParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        parser = new MemberInfoParser();

        tv_greet = findViewById( R.id.tv_greet );
        tv_logout = findViewById( R.id.tv_logout );

        btn_morder = findViewById( R.id.btn_morder );
        btn_mstore = findViewById( R.id.btn_mstore );
        btn_mmenu = findViewById( R.id.btn_mmenu );
        btn_mid = findViewById( R.id.btn_mid );

        pref = PreferenceManager.getDefaultSharedPreferences( ManagerMainActivity.this );
        id = pref.getString("login_id", "");

        if( id.equals("") ){
            Intent i = new Intent( ManagerMainActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }

        new getNicknameAsync().execute();

        tv_logout.setOnClickListener( click );
        btn_morder.setOnClickListener( click );
        btn_mstore.setOnClickListener( click );
        btn_mmenu.setOnClickListener( click );
        btn_mid.setOnClickListener( click );

    }//onCreate();

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent i;

            switch ( view.getId() ){

                case R.id.tv_logout:

                    SharedPreferences.Editor editor = pref.edit();

                    editor.remove("login_id");
                    editor.remove("login_pwd");
                    editor.remove("login_idx");

                    editor.commit();

                    Toast.makeText( getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT ).show();
                    handler_logout.sendEmptyMessageDelayed(0,800);

                    break;

                case R.id.btn_morder:
                    i = new Intent( ManagerMainActivity.this, ManagerOrderActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    break;

                case R.id.btn_mstore:
                    i = new Intent( ManagerMainActivity.this, ManagerStoreActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    break;

                case R.id.btn_mmenu:
                    i = new Intent( ManagerMainActivity.this, ManagerMenuActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    break;

                case R.id.btn_mid:
                    i = new Intent( ManagerMainActivity.this, ManagerMypageActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    break;

            }

        }

    };//click

    Handler handler_logout = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            Intent i = new Intent( ManagerMainActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }
    };

    public class getNicknameAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return parser.getMemberInfo(id);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String nickname = "";

            try {
                nickname = jsonObject.getString("nickname");
                tv_greet.setText( nickname + "님, 안녕하세요!" );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
