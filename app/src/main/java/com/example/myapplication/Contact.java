package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey
    @NonNull
    public String contact_id;

    @ColumnInfo(name = "contact_name")
    public String contact_name;

    @ColumnInfo(name = "contact_type")
    public String contact_type;

    public Contact(String contact_id, String contact_name, String contact_type){
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.contact_type = contact_type;
    }
}
