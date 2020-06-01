package com.example.myapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository mRepository;

    private LiveData<List<Contact>> mAllContacts;

    public ContactViewModel (@NonNull Application application) {
        super(application);
        mRepository = new ContactRepository(application);
        mAllContacts = mRepository.getAllContacts();
    }

    LiveData<List<Contact>> getAllContacts() { return mAllContacts; }
}
