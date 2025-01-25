package com.example.evilaniprojesi_v1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Firebase Authentication referansı
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Kullanıcı zaten giriş yaptıysa, doğrudan ana ekrana yönlendirilir
        if (auth.currentUser != null) {
            goToHomePage()
            return
        }

        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.emailedittext)
        val passwordEditText = findViewById<EditText>(R.id.passwordedittext)
        val registerButton = findViewById<Button>(R.id.button)
        val loginButton = findViewById<Button>(R.id.girisbuton)

        // Kayıt olma işlemi
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // E-posta ve şifre alanları boş mu kontrol et
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase ile yeni kullanıcı oluştur
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                    goToHomePage() // Başarılıysa ana ekrana yönlendir
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Kayıt başarısız: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }

        // Giriş yapma işlemi
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // E-posta ve şifre alanları boş mu kontrol et
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase ile giriş yap
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                    goToHomePage() // Başarılıysa ana ekrana yönlendir
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Giriş başarısız: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun goToHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Önceki aktiviteleri temizler
        startActivity(intent)
    }
}
