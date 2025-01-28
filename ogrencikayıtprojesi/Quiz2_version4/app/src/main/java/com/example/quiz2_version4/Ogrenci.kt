package com.example.quiz2_version4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ogrenci(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ad: String,
    val soyad: String,
    val numara: Int,
    val altDersVar: Boolean
)
