package com.example.quiz2_version4

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ogrenci::class], version = 1)
abstract class OgrenciDatabase : RoomDatabase() {
    abstract fun ogrenciDao(): OgrenciDAO
}
