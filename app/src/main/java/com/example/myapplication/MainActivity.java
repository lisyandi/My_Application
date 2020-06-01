package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText contact, text1, text2;
    Button btnSend, btnClear, btnSelectFile, btnAddContact;
    ImageView image1;
    TextView lblPath;
    ArrayList<String> contactArrayList;
    AutoCompleteTextView autoCompleteTextView;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactRepository contactRepository = new ContactRepository(getApplicationContext());
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        autoCompleteTextView = findViewById(R.id.actv10);

        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contactList) {
                ContactAdapter adapter = new ContactAdapter(getApplicationContext(), contactList);
                autoCompleteTextView.setAdapter(adapter);
            }
        });

        contact = findViewById(R.id.actv10);
        text1 = findViewById(R.id.editText1);
        text2 = findViewById(R.id.editText2);

        btnSend = findViewById(R.id.btnSend);
        btnClear = findViewById(R.id.btnClear);
        btnSelectFile = findViewById(R.id.btnSelectFile);
        image1 = findViewById(R.id.image1);
        lblPath = findViewById(R.id.lblPath);

        btnAddContact = findViewById(R.id.btnCreateContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ContactCreateActivity.class);
                startActivity(i);
            }
        });

        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                     if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                         String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                         requestPermissions(permissions, PERMISSION_CODE);
                     }
                     else{
                         pickImageFromGallery();
                     }
                 }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Contact = contact.getText().toString().trim();
                if(!Contact.isEmpty()) {
                    String Text1 = text1.getText().toString().trim();
                    String Text2 = text2.getText().toString().trim();
                    String LblPath = lblPath.getText().toString().trim();
                    new myTask(Contact, Text1, Text2, LblPath).execute();
                }
                else{

                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setText("");
                text2.setText("");
                lblPath.setText("");
                image1.setImageResource(android.R.color.transparent);
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent  = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    void ClearForm(Void res){
        text1.setText("");
        text2.setText("");
        lblPath.setText("");
        image1.setImageResource(android.R.color.transparent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case PERMISSION_CODE: {
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   pickImageFromGallery();
               }
               else{
                   Toast.makeText(this, "permission denied !", Toast.LENGTH_SHORT).show();
               }
           }
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image1.setImageURI(data.getData());
            lblPath.setText(getRealPathFromURI(data.getData()));
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private class myTask extends AsyncTask<Void, Void, Void>{

        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        String Contact;
        String Text1;
        String Text2;
        String LblPath;

        myTask(String Contact, String Text1, String Text2, String LblPath) {
            // list all the parameters like in normal class define
            this.Contact = Contact;
            this.Text1 = Text1;
            this.Text2 = Text2;
            this.LblPath = LblPath;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Processing...");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String apiToken = "1288385401:AAFLIyi0I8MRxbQRT90YDtLRTGKJHhtc_RI";
            String urlImage = "https://api.telegram.org/bot%s/sendPhoto";
            String url="";
            if(!Text1.matches(""))
            {
                url = String.format(urlString, apiToken, Contact, Text1);
                SendTelegramText(url);
            }
            if(!Text2.matches(""))
            {
                url = String.format(urlString, apiToken, Contact, Text2);
                SendTelegramText(url);
            }
            if(!LblPath.matches("")){
                url = String.format(urlImage, apiToken, Contact);
                SendTelegramImage(url, Contact, LblPath);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            this.dialog.hide();
            ClearForm(aVoid);
        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    public void SendTelegramText(String param){
        try {
            URL url = new URL(param);
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
    }

    public void SendTelegramImage(String param, String contactx, String LblPath){
        Bitmap bitmap = BitmapFactory.decodeFile(LblPath);
        String attachmentName = "bitmap";
        String attachmentFileName = "bitmap.bmp";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            URL url = new URL(param);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            try {
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("Cache-Control", "no-cache");
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());
                dStream.writeBytes(twoHyphens + boundary + crlf);
                dStream.writeBytes("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n");
                dStream.writeBytes(contactx + "\r\n");

                dStream.writeBytes("--" + boundary + "\r\n");
                dStream.writeBytes("Content-Disposition: form-data; name=\"photo\"; filename=\"" + attachmentFileName + "\"\r\n\r\n");

                dStream.write(bitmapdata);
                dStream.writeBytes(crlf);

                dStream.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
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
    }
}
