package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ContactCreateActivity extends AppCompatActivity {
EditText id, name, type;
Button create;
RadioButton rdbPerson, rdbGroup;
String cId, cName, cType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_create);

        id = findViewById(R.id.txt_contactId);
        name = findViewById(R.id.txt_contactName);
        //type = findViewById(R.id.txt_contactType);
        create = findViewById(R.id.btnCreate);
        rdbPerson = findViewById(R.id.rdb_Person);
        rdbGroup = findViewById(R.id.rdb_Group);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id.getText().toString().isEmpty()||name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fill Data", Toast.LENGTH_SHORT).show();
                }
                else{
                    cId = id.getText().toString().trim();
                    cName= name.getText().toString().trim();
                    //cType = type.getText().toString().trim();
                    cType = "P";
                    if(rdbPerson.isChecked()){

                    }
                    else{
                        cType = "G";
                    }

                    ContactRepository contactRepository = new ContactRepository(getApplicationContext());
                    Contact contact = new Contact(cId, cName, cType);
                    contactRepository.insertTask(contact);
                }
            }
        });

    }
}
