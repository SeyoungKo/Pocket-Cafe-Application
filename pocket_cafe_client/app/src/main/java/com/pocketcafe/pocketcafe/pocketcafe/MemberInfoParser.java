package com.ysw.ysw34.pocketcafe;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import util.IpInfo;

public class MemberInfoParser {

    public JSONObject getMemberInfo( String id ){

        JSONObject jsonObject = null;
        String parameter = "id=" + id;
        String serverip = IpInfo.SERVERIP + "member_info.do";

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
                jsonObject = jsonArray.getJSONObject(0);

            }

        } catch (Exception e) {
            Log.i( "MY", e.toString() );
        }

        return jsonObject;

    }

    public JSONObject getMemberInfo( int idx ){

        JSONObject jsonObject = null;
        String parameter = "idx=" + idx;
        String serverip = IpInfo.SERVERIP + "member_info.do";

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
                jsonObject = jsonArray.getJSONObject(0);

            }

        } catch (Exception e) {
            Log.i( "MY", e.toString() );
        }

        return jsonObject;

    }

}
