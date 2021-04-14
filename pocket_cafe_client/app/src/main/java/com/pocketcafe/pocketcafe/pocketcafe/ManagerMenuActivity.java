package com.ysw.ysw34.pocketcafe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class ManagerMenuActivity extends AppCompatActivity {

    TextView tv_delete, tv_none1, tv_none2;
    ListView lv_menu;
    Button btn_add, btn_back;

    static Dialog dialog;
    Dialog add_dialog;

    ProgressBar progress;

    ScrollView add_scorllview;
    EditText et_name, et_price, et_hot_price, et_ice_price, et_regular_price, et_large_price, et_xlarge_price, et_takeout_price;
    RadioGroup radio_hot, radio_ice, radio_takeout;
    RadioButton radio_hot1, radio_hot2, radio_ice1, radio_ice2, radio_takeout1, radio_takeout2;
    CheckBox check_regular, check_large, check_xlarge;
    LinearLayout hot_price, ice_price, regular_price, large_price, xlarge_price, takeout_price;
    ImageView img, img_add;
    TextView tv_add;
    Button btn_add_menu, btn_cancel;

    SharedPreferences pref;
    int login_idx;
    int store_idx;

    StoreInfoParser storeInfoParser;

    ManagerMenuAdapter adapter = null;
    MenuInfoParser parser;

    boolean bimg = false;
    String imgPath = "";
    String file_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);

        storeInfoParser = new StoreInfoParser();
        parser = new MenuInfoParser();

        tv_delete = findViewById( R.id.tv_delete );
        tv_none1 = findViewById( R.id.tv_none1 );
        tv_none2 = findViewById( R.id.tv_none2 );
        lv_menu = findViewById( R.id.lv_menu );
        btn_add = findViewById( R.id.btn_add );
        btn_back = findViewById( R.id.btn_back );

        pref = PreferenceManager.getDefaultSharedPreferences( ManagerMenuActivity.this );
        login_idx = pref.getInt("login_idx", 0);

        if( login_idx == 0 ){
            Intent i = new Intent( ManagerMenuActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }

        dialog = new Dialog( ManagerMenuActivity.this, R.style.Dialog );
        dialog.setContentView( R.layout.progress_dialog );
        dialog.setCancelable( false );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progress = dialog.findViewById( R.id.progress );
        progress.getIndeterminateDrawable().setColorFilter( Color.rgb( 233, 233, 233 ), PorterDuff.Mode.MULTIPLY );

        dialog.show();

        add_dialog = new Dialog( ManagerMenuActivity.this, R.style.Dialog );
        add_dialog.setContentView( R.layout.add_menu_dialog );
        add_dialog.setCancelable(false);

        add_scorllview = add_dialog.findViewById( R.id.add_scrollview );
        et_name = add_dialog.findViewById( R.id.et_name );
        et_price = add_dialog.findViewById( R.id.et_price );
        et_hot_price = add_dialog.findViewById( R.id.et_hot_price );
        et_ice_price = add_dialog.findViewById( R.id.et_ice_price );
        et_regular_price = add_dialog.findViewById( R.id.et_regular_price );
        et_large_price = add_dialog.findViewById( R.id.et_large_price );
        et_xlarge_price = add_dialog.findViewById( R.id.et_xlarge_price );
        et_takeout_price = add_dialog.findViewById( R.id.et_takeout_price );

        radio_hot = add_dialog.findViewById( R.id.radio_hot );
        radio_ice = add_dialog.findViewById( R.id.radio_ice );
        radio_takeout = add_dialog.findViewById( R.id.radio_takeout );

        radio_hot1 = add_dialog.findViewById( R.id.radio_hot1 );
        radio_hot2 = add_dialog.findViewById( R.id.radio_hot2 );
        radio_ice1 = add_dialog.findViewById( R.id.radio_ice1 );
        radio_ice2 = add_dialog.findViewById( R.id.radio_ice2 );
        radio_takeout1 = add_dialog.findViewById( R.id.radio_takeout1 );
        radio_takeout2 = add_dialog.findViewById( R.id.radio_takeout2 );

        check_regular = add_dialog.findViewById( R.id.check_regular );
        check_large = add_dialog.findViewById( R.id.check_large );
        check_xlarge = add_dialog.findViewById( R.id.check_xlarge );

        hot_price = add_dialog.findViewById( R.id.hot_price );
        ice_price = add_dialog.findViewById( R.id.ice_price );
        regular_price = add_dialog.findViewById( R.id.regular_price );
        large_price = add_dialog.findViewById( R.id.large_price );
        xlarge_price = add_dialog.findViewById( R.id.xlarge_price );
        takeout_price = add_dialog.findViewById( R.id.takeout_price );

        img = add_dialog.findViewById( R.id.img );
        img_add = add_dialog.findViewById( R.id.img_add );
        tv_add = add_dialog.findViewById( R.id.tv_add );

        btn_add_menu = add_dialog.findViewById( R.id.btn_add_menu );
        btn_cancel = add_dialog.findViewById( R.id.btn_cancel );

        new getStoreIdxAsync().execute();

        btn_add.setOnClickListener( click );
        btn_back.setOnClickListener( click );

        radio_hot.setOnCheckedChangeListener( radio_click );
        radio_ice.setOnCheckedChangeListener( radio_click );
        radio_takeout.setOnCheckedChangeListener( radio_click );

        check_regular.setOnClickListener( check_click );
        check_large.setOnClickListener( check_click );
        check_xlarge.setOnClickListener( check_click );

        img.setOnClickListener( click );
        btn_add_menu.setOnClickListener( click );
        btn_cancel.setOnClickListener( click );

    }//onCreate()

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch ( view.getId() ){

                case R.id.btn_add:
                    add_dialog.show();
                    break;

                case R.id.btn_back:
                    finish();
                    break;

                case R.id.img:
                    if( bimg ){
                        AlertDialog.Builder builder = new AlertDialog.Builder( ManagerMenuActivity.this );
                        builder.setTitle("사진삭제");
                        builder.setMessage("해당 사진을 삭제하시겠습니까?");
                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bimg = false;
                                img.setImageBitmap(null);
                                img_add.setVisibility( View.VISIBLE );
                                tv_add.setVisibility( View.VISIBLE );
                                imgPath = "";
                            }
                        });

                        builder.setNegativeButton("아니요", null);

                        builder.show();

                    }else{
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(i, 1);
                    }
                    break;

                case R.id.btn_add_menu:

                    if( et_name.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "메뉴명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }else if( et_price.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "메뉴 가격을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( Integer.parseInt( et_price.getText().toString() ) < 0 ){
                        Toast.makeText( getApplicationContext(), "메뉴 가격을 바르게 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( !radio_hot1.isChecked() && !radio_hot2.isChecked() ){
                        Toast.makeText( getApplicationContext(), "따뜻한 음료 가능 여부를 선택하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( radio_hot1.isChecked() && et_hot_price.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "따뜻한 음료 추가 금액을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( !radio_ice1.isChecked() && !radio_ice2.isChecked() ){
                        Toast.makeText( getApplicationContext(), "차가운 음료 가능 여부를 선택하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( radio_ice1.isChecked() && et_ice_price.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "차가운 음료 추가 금액을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( !check_regular.isChecked() && !check_large.isChecked() && !check_xlarge.isChecked() ){
                        Toast.makeText( getApplicationContext(), "음료 사이즈를 선택하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( check_regular.isChecked() && et_regular_price.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "레귤러 사이즈 추가 금액을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( check_large.isChecked() && et_large_price.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "라지 사이즈 추가 금액을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( check_xlarge.isChecked() && et_xlarge_price.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "엑스라지 사이즈 추가 금액을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( !radio_takeout1.isChecked() && !radio_takeout2.isChecked() ){
                        Toast.makeText( getApplicationContext(), "포장 가능 여부를 선택하세요", Toast.LENGTH_SHORT ).show();
                    }else if( radio_takeout1.isChecked() && et_takeout_price.getText().toString().equals("") ){
                        Toast.makeText( getApplicationContext(), "포장 추가 금액을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else{
                        add_dialog.dismiss();
                        dialog.show();
                        if( bimg ){
                            new UploadPhotoAsync().execute();
                        }else{
                            new InsertMenuAsync().execute();
                        }
                    }

                    break;

                case R.id.btn_cancel:
                    add_scorllview.fullScroll( ScrollView.FOCUS_UP );
                    init_AddDialog();
                    add_dialog.dismiss();
                    break;
            }

        }

    };//click

    RadioGroup.OnCheckedChangeListener radio_click = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch ( radioGroup.getId() ){

                case R.id.radio_hot:

                    if( radio_hot1.isChecked() ){
                        hot_price.setVisibility(View.VISIBLE);
                    }

                    if( radio_hot2.isChecked() ){
                        et_hot_price.setText("");
                        hot_price.setVisibility(View.GONE);
                    }

                    break;

                case R.id.radio_ice:

                    if( radio_ice1.isChecked() ){
                        ice_price.setVisibility(View.VISIBLE);
                    }

                    if( radio_ice2.isChecked() ){
                        et_ice_price.setText("");
                        ice_price.setVisibility(View.GONE);
                    }

                    break;

                case R.id.radio_takeout:

                    if( radio_takeout1.isChecked() ){
                        takeout_price.setVisibility(View.VISIBLE);
                    }

                    if( radio_takeout2.isChecked() ){
                        et_takeout_price.setText("");
                        takeout_price.setVisibility(View.GONE);
                    }

                    break;

            }

        }

    };//radio_click

    View.OnClickListener check_click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch ( view.getId() ){

                case R.id.check_regular:

                    if( check_regular.isChecked() ){
                        regular_price.setVisibility(View.VISIBLE);
                    }else{
                        et_regular_price.setText("");
                        regular_price.setVisibility(View.GONE);
                    }

                    break;

                case R.id.check_large:

                    if( check_large.isChecked() ){
                        large_price.setVisibility(View.VISIBLE);
                    }else{
                        et_large_price.setText("");
                        large_price.setVisibility(View.GONE);
                    }

                    break;

                case R.id.check_xlarge:

                    if( check_xlarge.isChecked() ){
                        xlarge_price.setVisibility(View.VISIBLE);
                    }else{
                        et_xlarge_price.setText("");
                        xlarge_price.setVisibility(View.GONE);
                    }

                    break;

            }

        }

    };//check_click

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        InputStream in = null;
        Bitmap image = null;
        Uri imageUri = null;

        try {
            in = getContentResolver().openInputStream( data.getData() );
            imageUri = data.getData();
            image = BitmapFactory.decodeStream(in);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if( image != null ) {
            bimg = true;
            img.setImageBitmap(image);
            img_add.setVisibility(View.INVISIBLE);
            tv_add.setVisibility(View.INVISIBLE);
            imgPath = getPath( imageUri );
        }

    }

    public class getStoreIdxAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return storeInfoParser.getStoreInfo(login_idx);
        }//doInBackground()

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                store_idx = jsonObject.getInt("idx");
                new ManagerMenuAsync().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }//onPostExecute()

    }

    public class ManagerMenuAsync extends AsyncTask<Void, Void, ArrayList<MenuVO>>{

        @Override
        protected ArrayList<MenuVO> doInBackground(Void... voids) {
            return parser.getMenuInfo( store_idx );
        }//doInBackground()

        @Override
        protected void onPostExecute(ArrayList<MenuVO> list) {

            dialog.dismiss();

            if( list.size() > 0 ){
                adapter = new ManagerMenuAdapter(ManagerMenuActivity.this, R.layout.manager_menu_listview, list, lv_menu);
                lv_menu.setAdapter(adapter);
                tv_delete.setVisibility( View.VISIBLE );
                tv_none1.setVisibility( View.GONE );
                tv_none2.setVisibility( View.GONE );
            }

        }//onPostExecute()

    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);

        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(columnIndex);

    }//getPath()

    //사진 전송 async
    public class UploadPhotoAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String fileName = imgPath;
            String urlString = IpInfo.SERVERIP + "upload_menuphoto.do";
            JSONObject jsonObject = null;

            try {
                File sourceFile = new File(fileName);
                DataOutputStream dos;

                if (!sourceFile.isFile()) {
                    Log.e("MY", "Source File not exist :" + fileName);
                } else {

                    FileInputStream mFileInputStream = new FileInputStream(sourceFile);
                    URL connectUrl = new URL(urlString);

                    // open connection
                    HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    // write data
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd );
                    dos.writeBytes(lineEnd);

                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 1024 * 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    // read image
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                    }

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    mFileInputStream.close();
                    dos.flush();
                    // finish upload...

                    if (conn.getResponseCode() == conn.HTTP_OK) {

                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer stringBuffer = new StringBuffer();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuffer.append(line);
                        }

                        JSONArray jsonArray = new JSONArray( stringBuffer.toString() );
                        jsonObject = jsonArray.getJSONObject(0);

                    }

                    mFileInputStream.close();
                    dos.close();

                }

            } catch (Exception e) {

                Log.i("MY", e.toString());

            }

            return jsonObject;

        }//doInBackground()

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String result = "";

            try {
                result = jsonObject.getString("result");

                if( result.equals("success") ){

                    file_name = jsonObject.getString("file_name");
                    new InsertMenuAsync().execute();

                }else{
                    Toast.makeText(getApplicationContext(), "이미지 업로드 및 저장 실패", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//UploadPhotoAsync

    public class InsertMenuAsync extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            String name = et_name.getText().toString();
            String price = et_price.getText().toString();

            String hot;
            if( radio_hot1.isChecked() ){
                hot = et_hot_price.getText().toString();
            }else{
                hot = "-1";
            }

            String ice;
            if( radio_ice1.isChecked() ){
                ice = et_ice_price.getText().toString();
            }else{
                ice = "-1";
            }

            String regular;
            if( check_regular.isChecked() ){
                regular = et_regular_price.getText().toString();
            }else{
                regular = "-1";
            }

            String large;
            if( check_large.isChecked() ){
                large = et_large_price.getText().toString();
            }else{
                large = "-1";
            }

            String xlarge;
            if( check_xlarge.isChecked() ){
                xlarge = et_xlarge_price.getText().toString();
            }else{
                xlarge = "-1";
            }

            String takeout;
            if( radio_takeout1.isChecked() ){
                takeout = et_takeout_price.getText().toString();
            }else{
                takeout = "-1";
            }

            String parameter = "store_idx=" + store_idx
                    + "&name=" + name
                    + "&price=" + price
                    + "&photo=" + file_name
                    + "&hot=" + hot
                    + "&ice=" + ice
                    + "&regular=" + regular
                    + "&large=" + large
                    + "&xlarge=" + xlarge
                    + "&takeout=" + takeout;

            String serverip = IpInfo.SERVERIP + "insert_menu.do";
            String result = "";

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
                dialog.dismiss();
                Toast.makeText( getApplicationContext(), "메뉴 추가 성공", Toast.LENGTH_SHORT ).show();
            }else{
                Toast.makeText( getApplicationContext(), "메뉴 추가 실패", Toast.LENGTH_SHORT ).show();
            }

            init_AddDialog();
            add_dialog.dismiss();
            new ManagerMenuAsync().execute();

        }//onPostExecute()

    }

    public void init_AddDialog(){

        et_name.setText("");
        et_price.setText("");

        radio_hot.clearCheck();
        hot_price.setVisibility(View.GONE);
        et_hot_price.setText("");

        radio_ice.clearCheck();
        ice_price.setVisibility(View.GONE);
        et_ice_price.setText("");

        check_regular.setChecked(false);
        regular_price.setVisibility(View.GONE);
        et_regular_price.setText("");

        check_large.setChecked(false);
        large_price.setVisibility(View.GONE);
        et_large_price.setText("");

        check_xlarge.setChecked(false);
        xlarge_price.setVisibility(View.GONE);
        et_xlarge_price.setText("");

        radio_takeout.clearCheck();
        takeout_price.setVisibility(View.GONE);
        et_takeout_price.setText("");

        bimg = false;
        img.setImageBitmap(null);
        img_add.setVisibility( View.VISIBLE );
        tv_add.setVisibility( View.VISIBLE );
        imgPath = "";

    }

}
