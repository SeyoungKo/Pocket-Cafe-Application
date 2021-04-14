package com.ysw.ysw34.pocketcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManagerMypageActivity extends AppCompatActivity {

    Button btn_back, btn_changeinfo, btn_changepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_mypage);

        btn_back = findViewById( R.id.btn_back );
        btn_changeinfo = findViewById( R.id.btn_changeinfo );
        btn_changepwd = findViewById( R.id.btn_changepwd );

        btn_back.setOnClickListener( click );
        btn_changeinfo.setOnClickListener( click );
        btn_changepwd.setOnClickListener( click );

    }//OnCreate();

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent i;

            switch ( view.getId() ){

                case R.id.btn_back:
                    finish();
                    break;

                case R.id.btn_changeinfo:
                    i = new Intent( ManagerMypageActivity.this, ManagerChangeinfoActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    break;

                case R.id.btn_changepwd:
                    i = new Intent( ManagerMypageActivity.this, ManagerChangepwdActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity(i);
                    break;

            }

        }

    };//click

}
