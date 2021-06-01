package com.example.jsonobject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView text1,text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = findViewById(R.id.tv_1);
        text2 = findViewById(R.id.tv_2);
        findViewById(R.id.btn_parse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseByJSONObject();
            }
        });
    }
    //=================
    public void parseByJSONObject () {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str = get();
                //解析
                try {
                    //创建json对象
                    JSONObject jo = new JSONObject(str);
                    //从json对象中把我想要的属性的数据拿出来
                    String code = jo.getString("status");
                    String msg = jo.getString("timezone");
                    Log.e("TAG","这里拿到的是json对象中两个属性的值      时区="+msg+"    状态="+code);

//                    JSONObject date = jo.getJSONObject("date");
//                    String instrument_name = date.getString("instrument_name");
                    //显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           text1.setText(code);
                           text2.setText(msg);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

//=======================
    private String get(){
        try {
            //实例化一个URL对象
            URL url = new URL("https://api.caiyunapp.com/v2.5/7KlicJFJqOmm3iIn/114.31,30.52/realtime.json");
            try {
                //获取HttpURLConnection实例
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                //设置和请求相关的属性
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6000);
                //获取响应码并获取响应数据
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    //实例化一个响应流
                    InputStream in = conn.getInputStream();
                    //实例化一个数组
                    byte[] b = new byte[1024];
                    //int一个长度
                    int len = 0;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //将字节数组里面的内容写入缓存流
                    while((len = in.read(b))>-1){
                        //参数一：待写入的数组   参数二：起点    参数三：长度
                        baos.write(b,0,len);
                    }
                    //在控制台上显示出获取的数据
                    String msg = new String(baos.toByteArray());
                    Log.e("TAG",msg+"===这里是get方法拿到的json对象");
                    return msg;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}