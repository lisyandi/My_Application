package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import java.util.List;

public class TestActivity extends AppCompatActivity {
AutoCompleteTextView actv;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        actv = findViewById(R.id.actv11);

        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contactList) {
                ContactAdapter adapter = new ContactAdapter(getApplicationContext(), contactList);
                actv.setAdapter(adapter);
            }
        });
    }
}
