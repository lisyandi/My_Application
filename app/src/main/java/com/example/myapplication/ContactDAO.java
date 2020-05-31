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

    @Query("SELECT * FROM Contact ORDER BY contact_name ASC")
    public List<Contact> getAllContact();

    @Insert
    Long insertTask(Contact contact);

    @Update
    void updateTask(Contact contact);

    @Delete
    void deleteTask(Contact contact);

}
