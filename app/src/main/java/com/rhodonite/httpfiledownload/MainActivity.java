package com.rhodonite.httpfiledownload;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    TextView tv;
    Button bt;
    HttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) this.findViewById(R.id.tv);
        bt = (Button) this.findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("load...");
                AsyncTaskParseJson task = new AsyncTaskParseJson();
                String yourStringUrl = "yourURL"; //input your http address
                task.execute(yourStringUrl);
            }
        });
    }
    public class AsyncTaskParseJson extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... arg0) {
            String result = "";
            InputStream inputStream = null;
            try {
                client = new DefaultHttpClient();
                HttpResponse httpResponse = client.execute(new HttpGet(arg0[0]));
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null)
                {
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                    String line ;
                    int i = 0;
                    while((line = bufferedReader.readLine()) != null) {
                        publishProgress((int)((i/(float)720)*10));
                        result += line;i++;

                    }
                    inputStream.close();
                    return result;
                }
                else
                    result = "Did not work!";
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            tv.setText("");
            try {
                tv.append( strFromDoInBg);
            }catch (Exception ignored){}

        }
    }

}