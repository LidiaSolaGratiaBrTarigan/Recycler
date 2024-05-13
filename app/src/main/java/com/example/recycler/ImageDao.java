
package com.example.recycler;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {
    @Query("SELECT * FROM images")
    List<Image> getAllImages();

    @Insert
    void insert(Image image);
}

