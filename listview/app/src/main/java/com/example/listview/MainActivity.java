package com.example.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    String result;
    ListView listView ;
    TextView debugger ;

    String[] data_Name= new String[100];
    int[] data_Height= new int[100] ;
    int[] data_Weight= new int[100] ;
    Double[] data_BMI= new Double[100] ;
    Double[] data_BMR= new Double[100] ;

    JSONArray jArray ;
    DecimalFormat nf = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        listView.setAdapter(new MyAdapter());
        SettingListener();

        Thread m_threadConnect = new Thread(mutiThread);
        m_threadConnect.start();
    }

    // Inialize_component
    private void initialize(){
        listView = findViewById(R.id.listView_main) ;
        debugger = findViewById(R.id.debugger);
    }

    // Event Listener setting
    private  void SettingListener(){
        listView.setOnItemClickListener(onItemClick);
    }

    // Event Set
    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this,"Name:"+data_Name[position],Toast.LENGTH_SHORT).show();
        }
    };

    // About new class  extends Baseadapter
    private class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView ;
            Holder holder;
            // Check first
            if (view ==null){
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.style_listview,null);

                holder =new Holder();
                holder.imageView=view.findViewById(R.id.imageView);
                holder.textView_name=view.findViewById(R.id.textView_name);
                holder.textView_height=view.findViewById(R.id.textView_height);
                holder.textView_weight=view.findViewById(R.id.textView_weight);
                holder.textView_bmi=view.findViewById(R.id.textView_bmi);
                holder.textView_bmr=view.findViewById(R.id.textView_bmr);

                view.setTag(holder);

            }else{
                holder = (Holder)view.getTag();
            }

            // Basic setting is finished
            // Below it to put data in listView
            try{
                switch (position){
                    case 0:
                        holder.imageView.setImageResource(R.drawable.test_c);
                        holder.textView_name.setText(data_Name[position]);
                        holder.textView_height.setText(nf.format(data_Height[position]));
                        holder.textView_weight.setText(nf.format(data_Weight[position]));
                        holder.textView_bmi.setText(nf.format(data_BMI[position]));
                        holder.textView_bmr.setText(nf.format(data_BMR[position]));
                        break;
                    case 1:
                        holder.imageView.setImageResource(R.drawable.test_b);
                        holder.textView_name.setText(data_Name[position]);
                        holder.textView_height.setText(nf.format(data_Height[position]));
                        holder.textView_weight.setText(nf.format(data_Weight[position]));
                        holder.textView_bmi.setText(nf.format(data_BMI[position]));
                        holder.textView_bmr.setText(nf.format(data_BMR[position]));

                        break;
                    case 2:
                        holder.imageView.setImageResource(R.drawable.test_a);
                        holder.textView_name.setText(data_Name[position]);
                        holder.textView_height.setText(nf.format(data_Height[position]));
                        holder.textView_weight.setText(nf.format(data_Weight[position]));
                        holder.textView_bmi.setText(nf.format(data_BMI[position]));
                        holder.textView_bmr.setText(nf.format(data_BMR[position]));

                        break;
                }
            }catch (Exception e){
                Toast.makeText(MainActivity.this,"Error Happen",Toast.LENGTH_SHORT).show();
            }

            return view;
        }
    }
    //About new class Hold
    private class Holder{
        ImageView imageView;

        TextView textView_name;
        TextView textView_height;
        TextView textView_weight;
        TextView textView_bmi;
        TextView textView_bmr;
    }

    // About Http connect
    /* ======================================== */

    // 建立一個執行緒執行的事件取得網路資料
    // Android 有規定，連線網際網路的動作都不能再主線程做執行
    // 畢竟如果使用者連上網路結果等太久整個系統流程就卡死了
    private Runnable mutiThread = new Runnable(){
        public void run()
        {

            try {
                URL url = new URL("http://10.0.2.2:8888/information.php");
                // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 建立 Google 比較挺的 HttpURLConnection 物件
                connection.setRequestMethod("POST");
                // 設定連線方式為 POST
                connection.setDoOutput(true); // 允許輸出
                connection.setDoInput(true); // 允許讀入
                connection.setUseCaches(false); // 不使用快取
                connection.connect(); // 開始連線

                int responseCode = connection.getResponseCode();
                // 建立取得回應的物件
                if(responseCode == HttpURLConnection.HTTP_OK){
                    // 如果 HTTP 回傳狀態是 OK ，而不是 Error
                    InputStream inputStream = connection.getInputStream();
                    // 取得輸入串流
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    // 讀取輸入串流的資料
                    String box = ""; // 宣告存放用字串
                    String line = null; // 宣告讀取用的字串
                    while((line = bufReader.readLine()) != null) {
                        box += line + "\n";
                        // 每當讀取出一列，就加到存放字串後面
                    }
                    inputStream.close(); // 關閉輸入串流
                    result = box; // 把存放用字串放到全域變數

                    jArray = new JSONArray(result);

                    for(int i = 0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        data_Name[i] = json_data.getString("Name");
                        data_Height[i] = json_data.getInt("Height");
                        data_Weight[i] = json_data.getInt("Weight");
                        data_BMI[i] = json_data.getDouble("BMI");
                        data_BMR[i] = json_data.getDouble("BMR");

                    }
                }


            } catch(Exception e) {
                result = "error: "+e.toString(); // 如果出事，回傳錯誤訊息
            }

            // 當這個執行緒完全跑完後執行
            runOnUiThread(new Runnable() {
                public void run() {
                    for(int i=0;i<jArray.length();i++){
                        debugger.append(data_Name[i]+" "+nf.format(data_Height[i])+" "+nf.format(data_Weight[i])+" "+nf.format(data_BMI[i])+" "+nf.format(data_BMR[i])+"\n");

                    }
                }
            });
        }
    };
}
