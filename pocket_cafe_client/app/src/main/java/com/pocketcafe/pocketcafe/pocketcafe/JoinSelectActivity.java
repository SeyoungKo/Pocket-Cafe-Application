package com.ysw.ysw34.pocketcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoinSelectActivity extends AppCompatActivity {

    Button btn_guest, btn_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_select);

        btn_guest = findViewById( R.id.btn_guest );
        btn_manager = findViewById( R.id.btn_manager );

        btn_guest.setOnClickListener( click );
        btn_manager.setOnClickListener( click );

    }//onCreate()

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent i;

            switch ( view.getId() ){

                case R.id.btn_guest:
                    i = new Intent( JoinSelectActivity.this, JoinGuestActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity( i );
                    break;

                case R.id.btn_manager:
                    i = new Intent( JoinSelectActivity.this, JoinManagerActivity.class );
                    i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    startActivity( i );
                    break;

            }

        }

    };//click

}
