package com.ysw.ysw34.pocketcafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class ManagerMenuAdapter extends ArrayAdapter<MenuVO> implements AdapterView.OnItemLongClickListener {

    Context context;
    int resource;
    ArrayList<MenuVO> list;
    MenuVO vo;

    public ManagerMenuAdapter(Context context, int resource, ArrayList<MenuVO> list, ListView listView) {
        super(context, resource, list);

        this.context = context;
        this.resource = resource;
        this.list = list;

        listView.setOnItemLongClickListener(this);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = linf.inflate( resource, null );

        vo = list.get(position);

        ImageView menu_photo = convertView.findViewById( R.id.menu_photo );
        TextView menu_name = convertView.findViewById( R.id.menu_name );
        TextView menu_price = convertView.findViewById( R.id.menu_price );
        TextView menu_hotice = convertView.findViewById( R.id.menu_hotice );
        TextView menu_size = convertView.findViewById( R.id.menu_size );
        TextView menu_takeout = convertView.findViewById( R.id.menu_takeout );

        String name = "??????: " + vo.getName();
        String price = "??????: " + vo.getPrice();

        String hotice = "";

        if( vo.getHot() == 0 ){
            hotice += "????????? ?????? / ";
        }else if( vo.getHot() > 0 ){
            hotice += "????????? ?????? (" + vo.getHot() + "??? ??????) / ";
        }else{
            hotice += "????????? ?????? ?????? / ";
        }

        if( vo.getIce() == 0 ){
            hotice += "????????? ??????";
        }else if( vo.getIce() > 0 ){
            hotice += "????????? ?????? (" + vo.getIce() + "??? ??????)";
        }else{
            hotice += "????????? ?????? ??????";
        }

        String size = "?????????: ";

        if( vo.getRegular() > -1 ){
            size += "?????????";

            if( vo.getRegular() > 0 ) {
                size += "(" + vo.getRegular() +"??? ??????)";
            }

            if( vo.getLarge() > -1 ){
                size += ", ";

            }

        }

        if( vo.getLarge() > -1 ){
            size += "??????";

            if( vo.getLarge() > 0 ){
                size += "(" + vo.getLarge() + "??? ??????)";
            }

            if( vo.getXlarge() > -1 ){
                size += ", ";

            }

        }

        if( vo.getXlarge() > -1 ){
            size += "????????????";

            if( vo.getXlarge() > 0 ){
                size += "(" + vo.getXlarge() +"??? ??????)";
            }

        }

        String takeout = "";

        if( vo.getTakeout() > -1 ){
            takeout += "?????? ??????";

            if( vo.getTakeout() > 0 ){
                takeout += "(" + vo.getTakeout() +"??? ??????)";
            }
        }

        menu_name.setText( name );
        menu_price.setText( price );
        menu_hotice.setText( hotice );
        menu_size.setText( size );
        menu_takeout.setText( takeout );
        new MenuPhotoAsync( menu_photo, vo ).execute();

        return convertView;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        final int selected_idx = i;

        final MenuVO selected_vo = list.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle("?????? ??????");
        builder.setMessage( selected_vo.getName() + "??? ?????????????????????????");
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ManagerMenuActivity.dialog.show();
                new DeleteMenuAsync().execute( selected_vo.getIdx(), selected_idx );
            }
        });
        builder.setNegativeButton("?????????", null);

        builder.show();

        return true;
    }

    public class MenuPhotoAsync extends AsyncTask<Void, Void, Bitmap>{

        ImageView menu_img;
        MenuVO vo;

        public MenuPhotoAsync( ImageView menu_img, MenuVO vo ){
            this.menu_img = menu_img;
            this.vo = vo;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            URL url = null;
            String photo = vo.getPhoto();
            Bitmap bitmap = null;

            try {
                // ????????? ????????? url ???????????? ??????
                url = new URL(IpInfo.SERVERIP + "menu_photo/" + photo);
                // url??? ?????? ??????
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // ????????? ??????
                InputStream is = conn.getInputStream();
                // ??????????????? ?????? ???????????? ????????? ??????
                // ??????????????? ????????? ????????? ?????? Bitmap??? ???????????????
                bitmap = BitmapFactory.decodeStream(is);

                // ?????? ??????
                is.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if( bitmap != null ){
                menu_img.setImageBitmap(bitmap);
            }

        }
    }

    public class DeleteMenuAsync extends AsyncTask<Integer, Void, String>{

        int selected_idx;

        @Override
        protected String doInBackground(Integer... integers) {

            selected_idx = integers[1];
            String parameter = "idx=" + integers[0];
            String serverip = IpInfo.SERVERIP + "delete_menu.do";
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
        }

        @Override
        protected void onPostExecute(String s) {

            if ( s.equals("success") ){

                ManagerMenuActivity.dialog.dismiss();
                list.remove(selected_idx);
                notifyDataSetChanged();
                Toast.makeText( context, "?????????????????????.", Toast.LENGTH_SHORT ).show();

            }else{
                Toast.makeText( context, "????????????", Toast.LENGTH_SHORT ).show();
            }
        }
    }

}
