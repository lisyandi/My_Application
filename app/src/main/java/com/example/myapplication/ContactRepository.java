package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ContactRepository {

    private String DB_NAME = "contactdb";

    private ContactDatabase contactDatabase;

    Context context;

    private Contact mContact;
    private LiveData<List<Contact>> mAllContact;

    public ContactRepository(Context context){
        this.context = context;
        contactDatabase = Room.databaseBuilder(context, ContactDatabase.class, DB_NAME).build();
    }

    LiveData<List<Contact>> getAllContacts() {
        return contactDatabase.contactDAO().getAllContact();
    }

    public void insertTask(final Contact contact){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                contactDatabase.contactDAO().insertTask(contact);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, contact.contact_name +" is Inserted", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
