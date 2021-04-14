package com.ysw.ysw34.pocketcafe;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import util.IpInfo;

import static com.ysw.ysw34.pocketcafe.R.layout.ordermenu_item;

public class OrderMenuActivity extends AppCompatActivity {

    ListView ordermenu;
    TextView menuInfo,noMenu, logout, tv_drawer_greet, tv_orderlist;
    Button btn_menu, closedrawer, btn_back, btn_order;
    LinearLayout orderlist;

    OrderMenuAdapter adapter;
    DrawerLayout layout;
    LinearLayout drawer;
    int idx;
    int count = 0;
    int order_price = 0;
    int total_price = 0;

    boolean myLockListView = true; // 스크롤링 감지
//--------------------------dialog------------------
    Dialog dialog;
    Button closedialog, btn_save, btn_count_plus, btn_count_minus;
    TextView cdrink, totalprice, tv_count;
    RadioGroup radio_hotice, radio_takeout, radio_size;
    RadioButton radio_hot, radio_ice, radio_regular, radio_large, radio_xlarge, tyes, tno;
    ArrayAdapter<CharSequence> carr,  hiar,sarr ;
    int total, totals, totalh, totalt;

    static Dialog loading_dialog;
    ProgressBar progress;

    SharedPreferences pref;
    int login_idx;

    MemberInfoParser member_parser;
    ArrayList<String> order_list;
    String order_list_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_menu);

        pref = PreferenceManager.getDefaultSharedPreferences(OrderMenuActivity.this);
        login_idx = pref.getInt("login_idx", 0);

        if( login_idx == 0 ){
            Intent i = new Intent( OrderMenuActivity.this, LoginActivity.class );
            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity(i);
            finish();
        }

        member_parser = new MemberInfoParser();

        loading_dialog = new Dialog( OrderMenuActivity.this, R.style.Dialog );
        loading_dialog.setContentView( R.layout.progress_dialog );
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.setCancelable(false);

        progress = loading_dialog.findViewById( R.id.progress );
        progress.setIndeterminate(true);
        progress.getIndeterminateDrawable().setColorFilter(Color.rgb( 233, 233, 233 ), PorterDuff.Mode.MULTIPLY );

        loading_dialog.show();

         ordermenu = findViewById(R.id.orderMenu);
         menuInfo = findViewById(R.id.menuInfo);
         noMenu = findViewById(R.id.noMenu);
        btn_menu = findViewById(R.id.btn_menu);
        closedrawer = findViewById(R.id.closedrawer);
        layout = findViewById(R.id.layout);
        drawer = findViewById(R.id.drawer);
        btn_back = findViewById(R.id.btn_back);
        logout = findViewById( R.id.Logout );
        tv_drawer_greet = findViewById( R.id.tv_drawer_greet );
        tv_orderlist = findViewById( R.id.tv_orderlist );
        orderlist = findViewById( R.id.orderlist );
        btn_order = findViewById( R.id.btn_order );

        new NicknameAsync().execute();

        btn_menu.setOnClickListener(click);
        closedrawer.setOnClickListener(click);
        btn_back.setOnClickListener(click);
        logout.setOnClickListener(click);
        btn_order.setOnClickListener( click );

        order_list = new ArrayList<>();

        OrderMenuAsync task = new OrderMenuAsync();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        idx = bundle.getInt("idx");

        try {
            task.execute(String.valueOf(idx));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//onCreate()

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_menu:
                    layout.openDrawer(drawer);
                    break;
                case R.id.closedrawer:
                    layout.closeDrawer(drawer);
                    break;
                case R.id.btn_back:
                    Intent i = new Intent(OrderMenuActivity.this, CafeInfoActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    break;
                case R.id.Logout:
                    SharedPreferences.Editor editor = pref.edit();

                    editor.remove("login_id");
                    editor.remove("login_pwd");
                    editor.remove("login_idx");

                    editor.commit();

                    Toast.makeText( getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT ).show();
                    handler.sendEmptyMessageDelayed( 0, 500 );
                    break;

                case R.id.btn_order:
                    //async
                    new InsertOrderAsync().execute();
                    break;
            }
        }
    };

    public class NicknameAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return member_parser.getMemberInfo(login_idx);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            String nickname;

            try {
                nickname = jsonObject.getString("nickname");
                tv_drawer_greet.setText(nickname + "님, 안녕하세요!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class OrderMenuAsync extends AsyncTask<String, Void, ArrayList> {
        String sendMsg, receiveMsg;
        ArrayList<MenuVO> list;

        @Override
            protected void onPostExecute(ArrayList arrayList) {

                noMenu.setVisibility(View.GONE);
                if (adapter == null) {

                    adapter = new OrderMenuAdapter(OrderMenuActivity.this, ordermenu_item, arrayList, ordermenu);
                    ordermenu.setAdapter(adapter);

                    ordermenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            final int position = i;

                            total = list.get(position).getPrice();
                            count = 1;
                            order_price = 0;
                            dialog = new Dialog(OrderMenuActivity.this, R.style.Dialog);

                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.ordercheck_dialog);

                            closedialog = dialog.findViewById(R.id.closedialog);
                            btn_save= dialog.findViewById(R.id.btn_save);
                            cdrink = dialog.findViewById(R.id.cdrink);
                            tv_count = dialog.findViewById( R.id.tv_count );
                            btn_count_plus = dialog.findViewById( R.id.btn_count_plus );
                            btn_count_minus = dialog.findViewById( R.id.btn_count_minus );
                            radio_hotice = dialog.findViewById( R.id.radio_hotice );
                            radio_hot = dialog.findViewById( R.id.radio_hot );
                            radio_ice = dialog.findViewById( R.id.radio_ice );
                            radio_size = dialog.findViewById( R.id.radio_size );
                            radio_regular = dialog.findViewById( R.id.radio_regular );
                            radio_large = dialog.findViewById( R.id.radio_large );
                            radio_xlarge = dialog.findViewById( R.id.radio_xlarge );
                            radio_takeout = dialog.findViewById( R.id.radio_takeout );
                            tyes = dialog.findViewById(R.id.tyes);
                            tno = dialog.findViewById(R.id.tno);
                            totalprice = dialog.findViewById(R.id.totalPrice);

                            cdrink.setText("선택하신 음료 :  "+list.get(i).getName());
                            tv_count.setText("1");
                            totalprice.setText("결제금액: " + list.get(i).getPrice() +"원" );

                            btn_count_plus.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    count++;
                                    tv_count.setText(""+count);

                                    total = count * list.get(position).getPrice();
                                    Log.i("MY", "가격:" + list.get(position).getPrice());

                                    if( radio_regular.isChecked() ){
                                        totals = count * list.get(position).getRegular();
                                    }else if( radio_large.isChecked() ){
                                        totals = count * list.get(position).getLarge();
                                    }else if( radio_xlarge.isChecked() ){
                                        totals = count * list.get(position).getXlarge();
                                    }else{
                                        totals = 0;
                                    }

                                    if( radio_hot.isChecked() ){
                                        totalh = count * list.get(position).getHot();
                                    }else if( radio_ice.isChecked() ){
                                        totalh = count * list.get(position).getIce();
                                    }else{
                                        totalh = 0;
                                    }

                                    if( tyes.isChecked() ){
                                        totalt = count * list.get(position).getTakeout();
                                    }else{
                                        totalt = 0;
                                    }

                                    order_price = total + totals + totalh + totalt;
                                    totalprice.setText( "결제금액: " + order_price + "원" );

                                }

                            });

                            btn_count_minus.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if( count > 1 ) {
                                        count--;
                                        tv_count.setText("" + count);

                                        total = count * list.get(position).getPrice();

                                        if( radio_regular.isChecked() ){
                                            totals = count * list.get(position).getRegular();
                                        }else if( radio_large.isChecked() ){
                                            totals = count * list.get(position).getLarge();
                                        }else if( radio_xlarge.isChecked() ){
                                            totals = count * list.get(position).getXlarge();
                                        }else{
                                            totals = 0;
                                        }

                                        if( radio_hot.isChecked() ){
                                            totalh = count * list.get(position).getHot();
                                        }else if( radio_ice.isChecked() ){
                                            totalh = count * list.get(position).getIce();
                                        }else{
                                            totalh = 0;
                                        }

                                        if( tyes.isChecked() ){
                                            totalt = count * list.get(position).getTakeout();
                                        }else{
                                            totalt = 0;
                                        }

                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText( "결제금액: " + order_price +"원" );

                                    }else{
                                        Toast.makeText( getApplicationContext(), "최소 1잔 이상의 음료만 주문 가능합니다.", Toast.LENGTH_SHORT ).show();
                                    }
                                }

                            });

                            if( list.get(position).getHot() < 0 ){
                                radio_hot.setVisibility(View.GONE);
                            }else{

                                radio_hot.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        totalh = count * list.get(position).getHot();

                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText( "결제금액: " + order_price + "원" );
                                    }
                                });

                            }

                            if( list.get(position).getIce() < 0 ){
                                radio_ice.setVisibility(View.GONE);
                            }else{

                                radio_ice.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        totalh = count * list.get(position).getIce();

                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText( "결제금액: " + order_price + "원" );
                                    }
                                });

                            }

                            if( list.get(position).getRegular() < 0 ){
                                radio_regular.setVisibility(View.GONE);
                            }else{

                                radio_regular.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        totals = count * list.get(position).getRegular();

                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText( "결제금액: " + order_price + "원" );
                                    }
                                });

                            }

                            if( list.get(position).getLarge() < 0 ){
                                radio_large.setVisibility(View.GONE);
                            }else{

                                radio_large.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        totals = count * list.get(position).getLarge();

                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText( "결제금액: " + order_price + "원" );
                                    }
                                });

                            }

                            if( list.get(position).getXlarge() < 0 ){
                                radio_xlarge.setVisibility(View.GONE);
                            }else{

                                radio_xlarge.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        totals = count * list.get(position).getXlarge();

                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText( "결제금액: " + order_price + "원" );
                                    }
                                });

                            }

                            if( list.get(position).getTakeout() < 0 ){
                                tyes.setVisibility(View.GONE);
                                totalt = 0;
                            }else{

                                tyes.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        totalt = count * list.get(position).getTakeout();
                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText("결제금액: " + order_price + "원");
                                    }
                                });

                                tno.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        totalt = 0;
                                        order_price = total + totals + totalh + totalt;
                                        totalprice.setText("결제금액: " + order_price + "원");

                                    }
                                });
                            }

                            closedialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            btn_save.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!radio_hot.isChecked() && !radio_ice.isChecked()) {
                                        Toast.makeText(getApplicationContext(), "Hot/Ice 여부를 선택하세요.", Toast.LENGTH_SHORT).show();
                                    } else if (!radio_regular.isChecked() && !radio_large.isChecked() && !radio_xlarge.isChecked()) {
                                        Toast.makeText(getApplicationContext(), "사이즈를 선택하세요.", Toast.LENGTH_SHORT).show();
                                    } else if (!tyes.isChecked() && !tno.isChecked()) {
                                        Toast.makeText(getApplicationContext(), "테이크아웃 여부를 선택하세요.", Toast.LENGTH_SHORT).show();
                                    } else {

                                        String orderStr = "";
                                        String orderlist_Str = "";
                                        String jsonObject_order_Str = "";
                                        order_list_str = "";

                                        total_price += order_price;
                                        orderStr = list.get(position).getName() + "/수량:" + count;


                                        if (radio_hot.isChecked()) {
                                            orderStr += "/Hot";
                                        } else if (radio_ice.isChecked()) {
                                            orderStr += "/Ice";
                                        }

                                        orderStr += "/사이즈:";

                                        if (radio_regular.isChecked()) {
                                            orderStr += "Regular";
                                        } else if (radio_large.isChecked()) {
                                            orderStr += "Large";
                                        } else if (radio_xlarge.isChecked()) {
                                            orderStr += "Xlarge";
                                        }

                                        if (tyes.isChecked()) {
                                            orderStr += "/Takeout";
                                        }

                                        order_list.add(orderStr);
                                        orderlist.setVisibility(View.VISIBLE);

                                        for (int num = 0; num < order_list.size(); num++) {
                                            orderlist_Str += order_list.get(num);
                                            order_list_str += order_list.get(num);
                                            if( num < order_list.size() - 1 ) {
                                                orderlist_Str += "\n";
                                                order_list_str += ",";
                                            }
                                        }

                                        orderlist_Str += "\n\n";
                                        orderlist_Str += "총합계:" + total_price;

                                        tv_orderlist.setText(orderlist_Str);
                                        btn_order.setVisibility(View.VISIBLE);

                                        dialog.dismiss();
                                    }
                                }
                            });

                            dialog.setCancelable(false);
                            dialog.show();
                        }
                    });

                }
                adapter.notifyDataSetChanged();
                myLockListView = false;
                if(list.size()==0){
                    noMenu.setVisibility(View.VISIBLE);
                }

        }

        protected ArrayList doInBackground(String... strings) {
            try {
                String str;
                String serverip = IpInfo.SERVERIP + "menu_info.do";
                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "idx=" + idx;

                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();

                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }

                    JSONArray jsonArray = new JSONArray(buffer.toString());

                    list = new ArrayList();
                   for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MenuVO vo = new MenuVO();

                        vo.setIdx(jsonObject.getInt("idx"));
                        vo.setStore_idx(jsonObject.getInt("store_idx"));
                        vo.setPrice(jsonObject.getInt("price"));
                        vo.setName(jsonObject.getString("name"));
                        vo.setHot(jsonObject.getInt("hot"));
                        vo.setIce(jsonObject.getInt("ice"));
                        vo.setXlarge(jsonObject.getInt("regular"));
                        vo.setLarge(jsonObject.getInt("large"));
                        vo.setXlarge(jsonObject.getInt("xlarge"));
                        vo.setTakeout(jsonObject.getInt("takeout"));
                        vo.setPhoto(jsonObject.getString("photo"));

                        list.add(vo);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    // 통신 실패
                }
            } catch (Exception e) {
                Log.i("aaa", e.getMessage());
                e.printStackTrace();
            }
            return list;
        } // doInBackground()

        @Override
        protected void onPreExecute() {
            loading_dialog.dismiss();
        }
    }//Async

    public class InsertOrderAsync extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            String parameter = "guest_idx=" + login_idx + "&store_idx=" + idx + "&order_list=" + order_list_str +"&price=" + total_price;
            String serverip = IpInfo.SERVERIP + "insert_order.do";
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

            if( s.equals("success") ){
                Toast.makeText( getApplicationContext(), "주문이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText( getApplicationContext(), "주문실패", Toast.LENGTH_SHORT ).show();
            }

            finish();

        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            Intent i = new Intent( OrderMenuActivity.this, LoginActivity.class );
            startActivity(i);
            finishAffinity();
            System.runFinalization();
            System.exit(0);

        }
    };

}
