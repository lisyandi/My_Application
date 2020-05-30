package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
EditText contact, text1, text2;
Button btnSend, btnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contact = findViewById(R.id.editText0);
        text1 = findViewById(R.id.editText1);
        text2 = findViewById(R.id.editText2);

        btnSend = findViewById(R.id.btnSend);
        btnClear = findViewById(R.id.btnClear);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Contact = contact.getText().toString();
                String Text1 = text1.getText().toString();
                String Text2 = text2.getText().toString();

                new myTask(Contact, Text1, Text2).execute();
            }
        });
    }

    private class myTask extends AsyncTask<Void, Void, Void>{

        String contact;
        String Text1;
        String Text2;

        myTask(String contact, String Text1, String Text2) {
            // list all the parameters like in normal class define
            this.contact = contact;
            this.Text1 = Text1;
            this.Text2 = Text2;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

            String apiToken = "1288385401:AAFLIyi0I8MRxbQRT90YDtLRTGKJHhtc_RI";

            String chatId = "1283417746";
            String text = "Hello world!";

            urlString = String.format(urlString, apiToken, contact, Text1);

            try {
                URL url = new URL(urlString);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                try {
                    urlConnection.setDoOutput(true);

                    DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());
                    dStream.flush();
                    dStream.close();

                    int responsecode = urlConnection.getResponseCode();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput =  new StringBuilder();
                    while((line = bufferedReader.readLine()) != null){
                        responseOutput.append(line);
                    }

                    Log.d("Result : ", responseOutput.toString());
                } finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e) {
                String a = e.getMessage();
            }
            return null;
        }
    }
}
