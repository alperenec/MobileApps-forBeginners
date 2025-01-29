package com.example.vizeapp_v1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    // SharedPreferences: Kullanıcı verilerini depolamak için kullanılacak değişken
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Login ekranı arayüzü yükleniyor

        // Arayüzdeki bileşenler tanımlanıyor
        val usernameField = findViewById<EditText>(R.id.username) // Kullanıcı adı alanı
        val passwordField = findViewById<EditText>(R.id.password) // Şifre alanı
        val rememberMeCheckBox = findViewById<CheckBox>(R.id.remember_me) // "Beni Hatırla" kutusu
        val loginButton = findViewById<Button>(R.id.login_button) // Giriş butonu

        // SharedPreferences başlatılıyor (Kullanıcı verilerini saklamak için)
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // "Beni Hatırla" seçeneği ile kaydedilmiş kullanıcı bilgileri yükleniyor
        loadRememberedCredentials(usernameField, passwordField, rememberMeCheckBox)

        // Giriş butonuna tıklama olayı
        loginButton.setOnClickListener {
            val username = usernameField.text.toString().trim() // Kullanıcı adı alınıyor
            val password = passwordField.text.toString().trim() // Şifre alınıyor

            // Giriş bilgileri doğrulanıyor
            if (validateCredentials(username, password)) {
                // "Beni Hatırla" seçeneği işleniyor
                handleRememberMe(username, password, rememberMeCheckBox.isChecked)
                // Ana ekrana yönlendiriliyor
                navigateToMainActivity()
            } else {
                // Hatalı giriş durumunda uyarı mesajı gösteriliyor
                Toast.makeText(this, "Hatalı kullanıcı adı veya şifre!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Kaydedilmiş giriş bilgilerini yükleme
    private fun loadRememberedCredentials(
        usernameField: EditText, // Kullanıcı adı alanı
        passwordField: EditText, // Şifre alanı
        rememberMeCheckBox: CheckBox // "Beni Hatırla" kutusu
    ) {
        // SharedPreferences'dan daha önce kaydedilen veriler okunuyor
        val savedUsername = sharedPreferences.getString("username", "") // Kaydedilmiş kullanıcı adı
        val savedPassword = sharedPreferences.getString("password", "") // Kaydedilmiş şifre
        val isRemembered = sharedPreferences.getBoolean("remember_me", false) // "Beni Hatırla" durumu

        // Eğer "Beni Hatırla" seçiliyse alanlar dolduruluyor
        if (isRemembered) {
            usernameField.setText(savedUsername) // Kullanıcı adı alanına veri yazılıyor
            passwordField.setText(savedPassword) // Şifre alanına veri yazılıyor
            rememberMeCheckBox.isChecked = true // Kutucuk işaretleniyor
        }
    }

    // Giriş bilgilerini doğrulama
    private fun validateCredentials(username: String, password: String): Boolean {
        // Sabit kullanıcı adı ve şifre kontrolü (örnek uygulama için)
        return username == "alperen" && password == "123"
    }

    // "Beni Hatırla" seçeneğini işleme
    private fun handleRememberMe(username: String, password: String, isChecked: Boolean) {
        val editor = sharedPreferences.edit() // SharedPreferences düzenleyicisi

        if (isChecked) {
            // Eğer "Beni Hatırla" seçiliyse kullanıcı adı, şifre ve seçim durumu kaydediliyor
            editor.putString("username", username)
            editor.putString("password", password)
            editor.putBoolean("remember_me", true)
        } else {
            // Seçim yapılmadıysa tüm veriler temizleniyor
            editor.clear()
        }
        editor.apply() // Değişiklikler kaydediliyor
    }

    // Ana ekrana yönlendirme
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java) // MainActivity'e geçiş için Intent oluşturuluyor
        intent.putExtra("clear_credentials", true) // Giriş bilgilerini temizlemesi için bayrak gönderiliyor
        startActivity(intent) // MainActivity başlatılıyor
        finish() // LoginActivity kapatılıyor
    }

    override fun onResume() {
        super.onResume()

        // Arayüzdeki kullanıcı adı ve şifre alanları bulunuyor
        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)

        // clear_credentials bayrağı kontrol ediliyor
        val clearCredentials = intent.getBooleanExtra("clear_credentials", false)
        if (clearCredentials) {
            // Eğer bayrak true ise alanlar temizleniyor
            usernameField.text.clear()
            passwordField.text.clear()
        }
    }
}
