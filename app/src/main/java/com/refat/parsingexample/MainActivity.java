package com.refat.parsingexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.button);
        final TextView tv = (TextView) findViewById(R.id.textView);

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String,String,String> task = new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        tv.setText("Processing");
                        btn.setEnabled(false);
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        URL url;
                        data="";
                        try {
                            url = new URL("https://api.androidhive.info/contacts/");
                            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                            InputStream stream = conn.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                            String line;
                            while ((line = br.readLine())!=null) {
                                data += line;
                            }
                        } catch (Exception e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        String txt = "";
                        try {
                            JSONObject full = new JSONObject(data);
                            JSONArray contacts = full.getJSONArray("contacts");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject singleContact = contacts.getJSONObject(i);
                                String name = singleContact.getString("name");
                                String email = singleContact.getString("email");
                                JSONObject phone = singleContact.getJSONObject("phone");
                                String mobile = phone.getString("mobile");
                                txt += name + " " + email + " " + mobile + "\n";
                            }
                        } catch (Exception e) {
                            tv.setText("Parsing Error");
                        }
                        tv.setText(txt);
                        btn.setEnabled(true);
                    }
                };
                task.execute();
            }
        });
    }
}
