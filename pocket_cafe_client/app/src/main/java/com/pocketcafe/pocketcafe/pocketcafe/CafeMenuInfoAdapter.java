package com.ysw.ysw34.pocketcafe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class CafeMenuInfoAdapter extends ArrayAdapter<MenuVO> implements AdapterView.OnItemClickListener {

    Context context;
    ArrayList<MenuVO> list;
    MenuVO vo;
    int resource;

    public CafeMenuInfoAdapter(Context context, int resource, ArrayList list, GridView ordermenu) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater linf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =linf.inflate(resource, null);

        ImageView menuImg = convertView.findViewById(R.id.menuImg);
        TextView menutxt =  convertView.findViewById(R.id.menutxt);

        menutxt.setText(list.get(position).getName()+"");

        new MenuPhotoInfoAsync(menuImg, list.get(position)).execute();

        return convertView;
    }
    public class MenuPhotoInfoAsync extends AsyncTask<Void, Void, Bitmap> {

        ImageView menuImg;
        MenuVO vo;

        public MenuPhotoInfoAsync( ImageView menuImg, MenuVO vo ){
            this.menuImg = menuImg;
            this.vo = vo;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            String photo = vo.getPhoto();
            Bitmap bitmap = null;

            try {
                // 스트링 주소를 url 형식으로 변환
                url = new URL(IpInfo.SERVERIP + "menu_photo/" + photo);
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
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if( bitmap != null ){
                menuImg.setImageBitmap(bitmap);
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
