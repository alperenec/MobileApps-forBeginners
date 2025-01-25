package com.example.evilaniprojesi_v1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddPostActivity : AppCompatActivity() {

    // Kullanıcı tarafından seçilen resmin URI'sini tutar
    private var selectedImageUri: Uri? = null

    // Firebase bileşenleri
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // Galeriden resim seçimi için ActivityResultLauncher
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        // Firebase başlatma
        storage = FirebaseStorage.getInstance() // Resimleri depolamak için
        firestore = FirebaseFirestore.getInstance() // Gönderi verilerini saklamak için
        auth = FirebaseAuth.getInstance() // Kullanıcı kimlik doğrulaması

        // UI bileşenlerini tanımlama
        val imageView = findViewById<ImageView>(R.id.imageView)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val areaEditText = findViewById<EditText>(R.id.areaEditText)
        val roomCountEditText = findViewById<EditText>(R.id.roomCountEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val cityEditText = findViewById<EditText>(R.id.cityEditText)
        val uploadButton = findViewById<Button>(R.id.uploadButton)

        // Galeriden içerik seçmek için başlatıcı
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri //
                imageView.setImageURI(uri) // Resmi ImageView'de gösterir
            } else {
                Toast.makeText(this, "Resim seçimi iptal edildi.", Toast.LENGTH_SHORT).show()
            }
        }

        imageView.setOnClickListener {
            checkGalleryPermission()
        }

        // Gönderiyi yüklemek için tıklama işlemi
        uploadButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val area = areaEditText.text.toString().trim()
            val roomCount = roomCountEditText.text.toString().trim()
            val price = priceEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()

            if (selectedImageUri == null || title.isEmpty() || area.isEmpty() || roomCount.isEmpty() || price.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "Tüm alanları doldurun ve resim seçin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadPost(selectedImageUri!!, title, area, roomCount, price, city)
        }
    }

    // Galeri iznini kontrol et
    private fun checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                galleryLauncher.launch("image/*")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 101)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                galleryLauncher.launch("image/*")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            }
        }
    }

    // Resmi Firebase Storage'a yükle
    private fun uploadPost(imageUri: Uri, title: String, area: String, roomCount: String, price: String, city: String) {
        val storageRef = storage.reference.child("images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri) // Resmi Firebase'e yükler
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    savePostToFirestore(title, area, roomCount, price, city, downloadUrl.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Resim yükleme hatası: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    // Gönderi bilgilerini Firestore'a kaydet
    private fun savePostToFirestore(title: String, area: String, roomCount: String, price: String, city: String, downloadUrl: String) {
        val currentUserEmail = auth.currentUser?.email ?: "Bilinmeyen Kullanıcı"
        val post = mapOf(

            
            "userEmail" to currentUserEmail,
            "title" to title,
            "area" to area,
            "roomCount" to roomCount,
            "price" to price,
            "city" to city,
            "downloadUrl" to downloadUrl,
            "date" to System.currentTimeMillis()
        )

        // Firestore koleksiyonuna gönderiyi ekle
        firestore.collection("Posts")
            .add(post)
            .addOnSuccessListener {
                Toast.makeText(this, "Gönderi başarıyla kaydedildi.", Toast.LENGTH_SHORT).show()
                finish() // Aktiviteyi kapat
            }
            .addOnFailureListener {
                Toast.makeText(this, "Firestore hatası: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}
