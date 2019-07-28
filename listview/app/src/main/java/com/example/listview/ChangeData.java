package com.example.listview;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChangeData extends AppCompatActivity {
    static int debug_int =0 ;

    Button button_update ;
    Button button_delete ;

    TextView textView_debug;
    TextView textView_username ;
    EditText editText_height;
    EditText editText_weight;
    EditText editText_bmi;
    EditText editText_bmr;

    String username="";
    Integer Height=0;
    Integer Weight=0;
    Double BMI =0.00;
    Double BMR =0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);

        initialize();
        settingListeners();
        GetData();
    }

    // Initialize
    /* ======================================= */
    // initialize component
    // connect its ID with the view
    private  void initialize(){
        button_update = findViewById(R.id.button_update) ;
        textView_username = findViewById(R.id.textView_username);
        editText_height = findViewById(R.id.editText_height);
        editText_weight = findViewById(R.id.editText_weight);
        editText_bmi = findViewById(R.id.editText_bmi);
        editText_bmr = findViewById(R.id.editText_bmr);

        button_delete = findViewById(R.id.button_delete) ;
        textView_debug = findViewById(R.id.textView_debugger_2) ;
    }

    // Event setting SET
    /* ======================================== */
    // Below are many kinds of Event Listeners
    // And the Setting function
    private void settingListeners(){
        button_update.setOnClickListener(UpdateAndTurnBack);
        button_delete.setOnClickListener(Delete);
    }

    // Method
    private View.OnClickListener Delete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Only need to know username
            // SO we can start thread now
            Thread m_thread = new Thread(DeleteData);
            m_thread.start();
        }
    };

    private View.OnClickListener UpdateAndTurnBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Update
            // How does it work
            // Step1: Connect to the INTERNET
            // Step2: Send new data to that php file(Which will handle the data sent by Android)
            // (And then the php file update the data to SQL)

            // Get Data from EditText
            Height= Integer.valueOf(editText_height.getText().toString());
            Weight= Integer.valueOf(editText_weight.getText().toString());
            BMI = Double.valueOf(editText_bmi.getText().toString());
            BMR =Double.valueOf(editText_bmr.getText().toString());

            //test
            Thread m_thread = new Thread(connect_SendData);
            m_thread.start();
            // Turn back
            //ChangeData.this.finish();
        }
    };

    // Another Method
    /* ======================================== */
    // Another method to be used

    // This is main to get data from ListView Page
    private void GetData(){
        Bundle bundle =this.getIntent().getExtras();
        DecimalFormat nf = new DecimalFormat("0.00");

        //Get Data from mainPage
        username = bundle.getString("username");
        textView_username.setText(bundle.getString("username"));
        editText_height.setText(Integer.toString(bundle.getInt("Height")));
        editText_weight.setText(Integer.toString(bundle.getInt("Weight")));
        editText_bmi.setText(nf.format(bundle.getDouble("bmi")));
        editText_bmr.setText(nf.format(bundle.getDouble("bmr")));

    }

    // About Http connect
    /* ======================================== */

    // 建立一個執行緒執行的事件傳送網路資料
    // Android 有規定，連線網際網路的動作都不能再主線程做執行
    // 畢竟如果使用者連上網路結果等太久整個系統流程就卡死了
    //
    // This method is to connect to A php file
    // And then send the update data to it
    // The php file will Handle the data sent by Android
    // And then update it in SQL
    private Runnable connect_SendData = new Runnable(){
        public void run()
        {
            URL url;
            HttpURLConnection conn = null;
            String urlString=("username="+username+"&"+"Height="+Height+"&"+"Weight="+Weight+"&"+"BMI="+BMI+"&"+"BMR="+BMR) ;

            try {
                //Create connection
                url = new URL("http://10.0.2.2:8888/infoUpdate.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                conn.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlString.getBytes().length));
                conn.setRequestProperty("Content-Language", "en-US");

                conn.setUseCaches(false);     // Post cannot use caches
                conn.setDoInput(true);        // Read from the connection. Default is true.


                // Output to the connection. Default is false, set to true because post
                // method must write something to the connection
                conn.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        conn.getOutputStream());
                wr.writeBytes(urlString);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                textView_debug.setText(response);
                reader.close();


            } catch (Exception e) {

                e.printStackTrace();
                textView_debug.setText("error_1");
            } finally {

                if (conn != null) {
                    conn.disconnect();
                }
            }

            // 當這個執行緒完全跑完後執行
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
        }
    };
    // This is the Delete Thread
    private  Runnable DeleteData = new Runnable() {
        @Override
        public void run() {
            URL url;
            HttpURLConnection conn = null;
            String urlString=("username="+username) ;

            try {
                //Create connection
                url = new URL("http://10.0.2.2:8888/infoDelete.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                conn.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlString.getBytes().length));
                conn.setRequestProperty("Content-Language", "en-US");

                conn.setUseCaches(false);     // Post cannot use caches
                conn.setDoInput(true);        // Read from the connection. Default is true.


                // Output to the connection. Default is false, set to true because post
                // method must write something to the connection
                conn.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        conn.getOutputStream());
                wr.writeBytes(urlString);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                textView_debug.setText(response);
                reader.close();


            } catch (Exception e) {

                e.printStackTrace();
                textView_debug.setText("error_1");
            } finally {

                if (conn != null) {
                    conn.disconnect();
                }
            }

            // 當這個執行緒完全跑完後執行
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
        }
    };
}



/*
* try {
                URL url = new URL("http://10.0.2.2:8888/infoUpdate.php");
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

                    connection.disconnect();
                }

            } catch(Exception e) {

            }

            // 當這個執行緒完全跑完後執行
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
* */