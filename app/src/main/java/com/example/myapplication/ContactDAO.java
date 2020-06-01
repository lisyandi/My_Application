package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert
    Long insertTask(Contact contact);

    @Update
    void updateTask(Contact contact);

    @Delete
    void deleteTask(Contact contact);

//    @Query("SELECT * FROM Contact ORDER BY contact_name ASC")
//    List<Contact> getAllContact();

    @Query("SELECT * FROM Contact ORDER BY contact_name ASC")
    LiveData<List<Contact>> getAllContact();
}
