package com.ysw.ysw34.pocketcafe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

public class OrderMenuAdapter  extends ArrayAdapter<MenuVO> implements AdapterView.OnItemClickListener {
    Context context;
    ArrayList<MenuVO> list;
    MenuVO vo;
    int resource;
    Context m;


    public OrderMenuAdapter(Context context, int resource, ArrayList<MenuVO> list, ListView ordermenu) {
        super(context,resource,list);

        this.context = context;
        this.resource = resource;
        this.list = list;

        ordermenu.setOnItemClickListener(this);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = linf.inflate(resource, null);
        // vo = list.get(position);
       TextView menuInfo = convertView.findViewById(R.id.menuInfo);
       ImageView storeImg = convertView.findViewById(R.id.storeImg);
       TextView extra = convertView.findViewById(R.id.extra);
       TextView priceInfo = convertView.findViewById(R.id.priceInfo);

       // Log.i("lp", String.valueOf(list.get(position).getName()));

        menuInfo.setText(list.get(position).getName()+"");
         priceInfo.setText(list.get(position).getPrice()+"원");

         String extra_text = "";

         if( list.get(position).getHot() >= 0 ){

             if( list.get(position).getHot() == 0 ){
                 extra_text += "Hot / ";
             }else{
                 extra_text += "Hot( " + list.get(position).getHot() + "원 추가 ) / ";
             }

         }else{
            extra_text += "Hot 불가 / ";
         }

         if( list.get(position).getIce() >= 0 ){

             if( list.get(position).getIce() == 0 ){
                 extra_text += "Ice";
             }else{
                 extra_text += "Ice( " + list.get(position).getIce() +"원 추가 )";
             }

         }else{
             extra_text += "Ice 불가";
         }

         extra_text += "\n";
         extra_text += "사이즈: ";

         if( list.get(position).getRegular() >= 0 ){

            extra_text += "Regular";

            if( list.get(position).getRegular() > 0 ){
                extra_text += "( " + list.get(position).getRegular() +"원 추가 )";
            }

            if( list.get(position).getLarge() >= 0 ){
                extra_text += ", ";
            }

        }

        if( list.get(position).getLarge() >= 0 ){

            extra_text += "Large";

            if( list.get(position).getLarge() > 0 ){
                extra_text += "( " + list.get(position).getLarge() +"원 추가 )";
            }

            if( list.get(position).getXlarge() >= 0 ){
                extra_text += ", ";
            }

        }

        if( list.get(position).getXlarge() >= 0 ){

            extra_text += "Xlarge";

            if( list.get(position).getXlarge() > 0 ){
                extra_text += "( " + list.get(position).getXlarge() +"원 추가 )";
            }

        }

        extra_text += "\n";

        if( list.get(position).getTakeout() >= 0 ){
            extra_text += "테이크아웃 가능";

            if( list.get(position).getTakeout() > 0 ){
                extra_text += "( " + list.get(position).getTakeout() +"원 추가 )";
            }

        }else{
            extra_text += "테이크아웃 불가";
        }

        extra.setText(extra_text);
         new MenuPhotoAsync(storeImg, list.get(position)).execute();

         return convertView;

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
                menu_img.setImageBitmap(bitmap);
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }
}


