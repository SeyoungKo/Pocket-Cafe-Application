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
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import util.Action;
import util.IpInfo;

public class ManagerStoreActivity extends AppCompatActivity {

    EditText et_storename, et_storeloc, et_storetel, et_storeoc, et_storeintro;
    ImageView img1, img2, img_add1, img_add2;
    TextView tv_add1, tv_add2;
    Button btn_back, btn_save;
    Dialog dialog;
    ProgressBar progress;

    List<Address> address;
    Geocoder coder;
    String store_lat, store_lng;

    boolean bimg1 = false;
    boolean bimg2 = false;
    boolean exist_img1 = false;
    boolean exist_img2 = false;
    boolean remove_img1 = false;
    boolean remove_img2 = false;

    int login_idx;
    SharedPreferences pref;

    String img1Path = "";
    String img2Path = "";

    int store_idx = -1;
    int server_result = 0;
    int server_commu;

    StoreInfoParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_store);

        parser = new StoreInfoParser();

        coder = new Geocoder( ManagerStoreActivity.this );

        dialog = new Dialog( ManagerStoreActivity.this, R.style.Dialog );
        dialog.setContentView( R.layout.progress_dialog );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        progress = dialog.findViewById( R.id.progress );
        progress.setIndeterminate(true);
        progress.getIndeterminateDrawable().setColorFilter( Color.rgb( 233, 233, 233 ), PorterDuff.Mode.MULTIPLY );

        dialog.show();

        et_storename = findViewById( R.id.et_storename );
        et_storeloc = findViewById( R.id.et_storeloc );
        et_storetel = findViewById( R.id.et_storetel );
        et_storeoc = findViewById( R.id.et_storeoc );
        et_storeintro = findViewById( R.id.et_storeintro );

        img1 = findViewById( R.id.img1 );
        img2 = findViewById( R.id.img2 );
        img_add1 = findViewById( R.id.img_add1 );
        img_add2 = findViewById( R.id.img_add2 );

        tv_add1 = findViewById( R.id.tv_add1 );
        tv_add2 = findViewById( R.id.tv_add2 );

        btn_back = findViewById( R.id.btn_back );
        btn_save = findViewById( R.id.btn_save );

        pref = PreferenceManager.getDefaultSharedPreferences( ManagerStoreActivity.this );
        login_idx = pref.getInt("login_idx", -1);

        if( login_idx == -1 ){
            Intent i = new Intent( ManagerStoreActivity.this, LoginActivity.class );
            startActivity(i);
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }

        new getStoreInfoAsync().execute();

        img1.setOnClickListener( click );
        img2.setOnClickListener( click );
        btn_back.setOnClickListener( click );
        btn_save.setOnClickListener( click );

    }//onCreate();

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(MediaStore.Images.Media.CONTENT_TYPE);

            AlertDialog.Builder builder = new AlertDialog.Builder( ManagerStoreActivity.this );
            builder.setTitle("사진삭제");
            builder.setMessage("해당 사진을 삭제하시겠습니까?");

            switch ( view.getId() ){

                case R.id.img1:

                    if( bimg1 ) {

                        builder.setNegativeButton("아니요", null);
                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                img1.setImageBitmap(null);
                                img_add1.setVisibility( View.VISIBLE );
                                tv_add1.setVisibility( View.VISIBLE );
                                img1Path = "";
                                bimg1 = false;
                            }
                        });

                        builder.show();

                    }else if( exist_img1 && !remove_img1 ){

                        builder.setNegativeButton("아니요", null);
                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                img1.setImageBitmap(null);
                                img_add1.setVisibility( View.VISIBLE );
                                tv_add1.setVisibility( View.VISIBLE );
                                img1Path = "";
                                remove_img1 = true;
                            }
                        });

                        builder.show();
                    } else {
                        startActivityForResult(i, 1);
                    }

                    break;

                case R.id.img2:

                    if( bimg2 ){

                        builder.setNegativeButton("아니요", null);
                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                img2.setImageBitmap(null);
                                img_add2.setVisibility( View.VISIBLE );
                                tv_add2.setVisibility( View.VISIBLE );
                                img2Path = "";
                                bimg2 = false;
                            }
                        });

                        builder.show();

                    }else if( exist_img2 && !remove_img2 ){

                        builder.setNegativeButton("아니요", null);
                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                img2.setImageBitmap(null);
                                img_add2.setVisibility( View.VISIBLE );
                                tv_add2.setVisibility( View.VISIBLE );
                                img2Path = "";
                                remove_img2 = true;
                            }
                        });

                        builder.show();

                    } else {
                        startActivityForResult(i, 2);
                    }

                    break;

                case R.id.btn_back:
                    finish();
                    break;

                case R.id.btn_save:
                    String store_name = et_storename.getText().toString();
                    String store_loc = et_storeloc.getText().toString();
                    String store_tel = et_storetel.getText().toString();
                    String store_openclose = et_storeoc.getText().toString();
                    String store_intro = et_storeintro.getText().toString();

                    if( store_name.equals("") ){
                        Toast.makeText( getApplicationContext(), "매장명을 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else if( store_loc.equals("") ){
                        Toast.makeText( getApplicationContext(), "주소를 입력하세요.", Toast.LENGTH_SHORT ).show();
                    }else{

                        try {
                            address = coder.getFromLocationName( store_loc, 5 );
                            Address store_address = address.get(0);
                            store_lat = String.format( "%.6f", store_address.getLatitude() );
                            store_lng = String.format( "%.6f", store_address.getLongitude() );
                        } catch (Exception e) {
                            Log.i( "MY", e.toString() );
                        }

                        if (bimg1) {
                            //img1 서버에 저장
                            new UploadPhotoAsync().execute(img1Path, "img1");
                        } else {

                            if( exist_img1 ) {

                                if( remove_img1 ){
                                    new UpdatePhoto1Async().execute("");
                                }
                                else {

                                    if (bimg2) {
                                        new UploadPhotoAsync().execute(img2Path, "img2");
                                    } else {

                                        if (exist_img2) {

                                            if( remove_img2 ){
                                                new UpdatePhoto2Async().execute("");
                                            }else {
                                                new ChangeStoreInfoAsync().execute(store_name, store_loc, store_tel, store_openclose, store_intro, store_lat, store_lng);
                                            }

                                        } else {
                                            new UpdatePhoto2Async().execute("");
                                        }
                                    }
                                }

                            }else {
                                new UpdatePhoto1Async().execute("");
                            }
                        }

                    }

                    break;

            }

        }

    };//click

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

        switch ( requestCode ){

            case 1:
                if( image != null ) {
                    bimg1 = true;
                    img1.setImageBitmap(image);
                    img_add1.setVisibility(View.INVISIBLE);
                    tv_add1.setVisibility(View.INVISIBLE);
                    img1Path = getPath( imageUri );
                }
                break;

            case 2:
                if( image != null ) {
                    bimg2 = true;
                    img2.setImageBitmap(image);
                    img_add2.setVisibility(View.INVISIBLE);
                    tv_add2.setVisibility(View.INVISIBLE);
                    img2Path = getPath( imageUri );
                }
                break;

        }

    }

    //가게 정보 로드 async
    public class getStoreInfoAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return parser.getStoreInfo(login_idx);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String name = "";
            String location = "";
            String tel = "";
            String openclose = "";
            String photo1 = "";
            String photo2 = "";
            String notice = "";

            try {
                store_idx = jsonObject.getInt("idx");
                name = jsonObject.getString("name");
                location = jsonObject.getString("location");
                tel = jsonObject.getString("tel");
                openclose = jsonObject.getString("openclose");
                photo1 = jsonObject.getString("photo1");
                photo2 = jsonObject.getString("photo2");
                notice = jsonObject.getString("notice");
            } catch (Exception e) {
                e.printStackTrace();
            }

            et_storename.setText(name);
            et_storeloc.setText(location);
            et_storetel.setText(tel);
            et_storeoc.setText(openclose);
            et_storeintro.setText(notice);

            if( !photo1.equals("null") && !photo1.equals("") ){
                exist_img1 = true;
                new ManagerStorePhotoAsync().execute( photo1, "img1" );
            }

            if( !photo2.equals("null") && !photo2.equals("") ){
                exist_img2 = true;
                new ManagerStorePhotoAsync().execute( photo2, "img2" );
            }

            dialog.dismiss();

        }
    }

    public class ManagerStorePhotoAsync extends AsyncTask<String, Void, Bitmap> {

        String action = "";

        @Override
        protected Bitmap doInBackground(String... strings) {

            URL url = null;
            String photo = strings[0];
            action = strings[1];
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

        }//doInBackground()

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {

                if ( action.equals("img1") ) {
                    img1.setImageBitmap(bitmap);
                    img_add1.setVisibility(View.INVISIBLE);
                    tv_add1.setVisibility(View.INVISIBLE);
                } else if ( action.equals("img2") ) {
                    img2.setImageBitmap(bitmap);
                    img_add2.setVisibility(View.INVISIBLE);
                    tv_add2.setVisibility(View.INVISIBLE);
                }

            }

        }
    }

    //가게 정보 변경 async
    public class ChangeStoreInfoAsync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String parameter = "manager_idx=" + login_idx
                            +"&name=" + strings[0]
                            + "&location=" + strings[1]
                            + "&tel=" + strings[2]
                            + "&openclose=" + strings[3]
                            + "&notice=" + strings[4]
                            + "&lat=" + strings[5]
                            + "&lng=" + strings[6];

            String serverip = IpInfo.SERVERIP + "change_storeinfo.do";
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
                Toast.makeText( getApplicationContext(), "매장 정보가 저장되었습니다.", Toast.LENGTH_SHORT ).show();
            }else{
                Toast.makeText( getApplicationContext(), s, Toast.LENGTH_SHORT ).show();
            }

            handler_finish.sendEmptyMessageDelayed(0, 800);

        }//onPostExecute()

    }//ChangeStoreInfoAsync

    Handler handler_finish = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            finish();
        }

    };//handler_finish

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);

        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(columnIndex);

    }//getPath()

    //사진 전송 async
    public class UploadPhotoAsync extends AsyncTask<String, Void, JSONObject>{

        String action;

        @Override
        protected JSONObject doInBackground(String... strings) {

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String fileName = strings[0];
            action = strings[1];
            String urlString = IpInfo.SERVERIP + "upload_storephoto.do";
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

                    String file_name = jsonObject.getString("file_name");

                    if( action.equals("img1") ) {
                        new UpdatePhoto1Async().execute(file_name);
                    }else if( action.equals("img2") ){
                        new UpdatePhoto2Async().execute(file_name);
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "이미지 업로드 및 저장 실패", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//UploadPhotoAsync

    //photo1 파일명 db 업데이트 async
    public class UpdatePhoto1Async extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            String parameter = "file_name=" + strings[0] + "&idx=" + store_idx;
            String serverip = IpInfo.SERVERIP + "update_storephoto1.do";

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

                String store_name = et_storename.getText().toString();
                String store_loc = et_storeloc.getText().toString();
                String store_tel = et_storetel.getText().toString();
                String store_openclose = et_storeoc.getText().toString();
                String store_intro = et_storeintro.getText().toString();

                if( bimg2 ){
                    new UploadPhotoAsync().execute(img2Path, "img2");
                }else{

                    if( exist_img2 ) {

                        if( remove_img2 ) {

                            new UpdatePhoto2Async().execute("");

                        }else {
                            new ChangeStoreInfoAsync().execute(store_name, store_loc, store_tel, store_openclose, store_intro, store_lat, store_lng);
                        }

                    }else {
                        new UpdatePhoto2Async().execute("");
                    }

                }

            }else{
                Toast.makeText(getApplicationContext(), "저장 실패1", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class UpdatePhoto2Async extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            String parameter = "file_name=" + strings[0] + "&idx=" + store_idx;
            String serverip = IpInfo.SERVERIP + "update_storephoto2.do";

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

            String store_name = et_storename.getText().toString();
            String store_loc = et_storeloc.getText().toString();
            String store_tel = et_storetel.getText().toString();
            String store_openclose = et_storeoc.getText().toString();
            String store_intro = et_storeintro.getText().toString();

            if( s.equals("success") ){

                new ChangeStoreInfoAsync().execute(store_name, store_loc, store_tel, store_openclose, store_intro, store_lat, store_lng);

            }else{
                Toast.makeText(getApplicationContext(), "저장 실패2", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
