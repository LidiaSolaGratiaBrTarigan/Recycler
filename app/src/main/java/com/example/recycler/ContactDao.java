package com.example.recycler;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Query(value = "SELECT * FROM contacts")
    List<Contact> getAllContacts();

    @Insert
    void insert(Contact contact);

    // Tambahkan metode lain ubuntu operasi seperti update dan delete jika diperlukan.
}
