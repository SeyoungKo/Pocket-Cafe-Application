package com.ysw.ysw34.pocketcafe;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class MenuInfoParser {

    MenuVO vo;

    public ArrayList<MenuVO> getMenuInfo(int idx){

        String parameter = "idx=" + idx;
        String serverip = IpInfo.SERVERIP + "menu_info.do";
        ArrayList<MenuVO> list = new ArrayList<>();

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

                for( int i = 0; i < jsonArray.length(); i++ ){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    vo = new MenuVO();
                    vo.setIdx( jsonObject.getInt("idx") );
                    vo.setStore_idx( jsonObject.getInt("store_idx") );
                    vo.setName( jsonObject.getString("name") );
                    vo.setPrice( jsonObject.getInt("price") );
                    vo.setPhoto( jsonObject.getString("photo") );
                    vo.setIce( jsonObject.getInt("ice") );
                    vo.setHot( jsonObject.getInt("hot") );
                    vo.setRegular( jsonObject.getInt("regular") );
                    vo.setLarge( jsonObject.getInt("large") );
                    vo.setXlarge( jsonObject.getInt("xlarge") );
                    vo.setTakeout( jsonObject.getInt("takeout") );

                    list.add(vo);
                }

            }

        } catch (Exception e) {
            Log.i( "MY", e.toString() );
        }

        return list;

    }

}
