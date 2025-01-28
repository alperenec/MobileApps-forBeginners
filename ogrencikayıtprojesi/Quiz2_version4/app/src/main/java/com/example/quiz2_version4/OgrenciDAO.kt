package com.example.quiz2_version4

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OgrenciDAO {
    @Insert
    suspend fun insertOgrenci(ogrenci: Ogrenci)

    @Query("SELECT * FROM ogrenci")
    suspend fun getAllOgrenciler(): List<Ogrenci>
}
