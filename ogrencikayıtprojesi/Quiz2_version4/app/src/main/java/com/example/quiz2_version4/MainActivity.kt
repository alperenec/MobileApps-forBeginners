package com.example.quiz2_version4

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var database: OgrenciDatabase
    private lateinit var adapter: OgrenciAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adEditText = findViewById<EditText>(R.id.editTextAd)
        val soyadEditText = findViewById<EditText>(R.id.editTextSoyad)
        val numaraEditText = findViewById<EditText>(R.id.editTextNumara)
        val dersCheckBox = findViewById<CheckBox>(R.id.checkBoxDers)
        val kaydetButton = findViewById<Button>(R.id.buttonKaydet)
        val listView = findViewById<ListView>(R.id.listViewOgrenciler)

        // Room Database kurulumu
        database = Room.databaseBuilder(
            applicationContext,
            OgrenciDatabase::class.java,
            "ogrenci_database"
        ).build()

        // Liste adaptörü
        adapter = OgrenciAdapter(this, mutableListOf())
        listView.adapter = adapter

        // Kaydet butonu işlemi
        kaydetButton.setOnClickListener {
            val ad = adEditText.text.toString().trim()
            val soyad = soyadEditText.text.toString().trim()
            val numara = numaraEditText.text.toString().trim()

            // Eğer numara boşsa veya geçerli değilse kaydetme
            if (numara.isEmpty() || !numara.all { it.isDigit() }) {
                numaraEditText.error = "Lütfen geçerli bir numara girin"
                return@setOnClickListener
            }

            val altDersVar = dersCheckBox.isChecked
            val yeniOgrenci = Ogrenci(0, ad, soyad, numara.toInt(), altDersVar)

            // Veritabanına ekleme işlemi
            lifecycleScope.launch {
                database.ogrenciDao().insertOgrenci(yeniOgrenci)
                loadOgrenciler()
            }

            // Giriş alanlarını temizle
            adEditText.text.clear()
            soyadEditText.text.clear()
            numaraEditText.text.clear()
            dersCheckBox.isChecked = false
        }

        // Uygulama başlarken verileri yükle
        lifecycleScope.launch {
            loadOgrenciler()
        }
    }

    // Tüm öğrencileri yükleyip listeye ekler
    private suspend fun loadOgrenciler() {
        val ogrenciler = database.ogrenciDao().getAllOgrenciler()
        adapter.updateData(ogrenciler)
    }
}
