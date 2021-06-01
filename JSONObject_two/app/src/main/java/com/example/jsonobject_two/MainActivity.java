package com.example.jsonobject_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jsonobject_two.NetWork.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jsonobject_two.NetWork.GetRequest.get;

public class MainActivity extends AppCompatActivity {
    private TextView text1,text2,text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = findViewById(R.id.tv_1);
        text2 = findViewById(R.id.tv_2);
        text3 = findViewById(R.id.tv_3);
        findViewById(R.id.btn_parse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseByJSONObject();
            }
        });

    }

    private void parseByJSONObject() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str = get();
                //解析
                try {
                    //创建json对象
                    //assert str != null;
                    JSONObject jo = new JSONObject(str);
                    //从json对象中把我想要的属性的数据拿出来
                    String code = jo.getString("status");
                    String msg = jo.getString("timezone");
                    Log.e("TAG","这里拿到的是json对象中两个属性的值      时区="+msg+"    状态="+code);
                    JSONObject result =jo.getJSONObject("result");
                    int primary = result.getInt("primary");
                    Log.e("TAG","主要值为"+primary);
                    JSONObject realtime = result.getJSONObject("realtime");
                    JSONObject wind = realtime.getJSONObject("wind");
                    int speed = wind.getInt("speed");
                    int direction = wind.getInt("direction");
                    Log.e("TAG","风速为"+speed+"     方向为"+direction);
                    //显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text1.setText(code);
                            text2.setText(msg);
                            text3.setText(speed+"     "+direction);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}