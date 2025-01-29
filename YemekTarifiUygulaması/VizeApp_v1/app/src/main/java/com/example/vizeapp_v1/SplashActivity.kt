package com.example.vizeapp_v1

// Gerekli Android kütüphaneleri projeye dahil ediliyor
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Splash ekranı arayüzü yükleniyor

        // Geri sayım göstermek için kullanılan TextView bileşeni
        val countdownTextView = findViewById<TextView>(R.id.countdown_text)

        // 5 saniyelik bir geri sayım başlatılıyor
        object : CountDownTimer(5000, 1000) { // Toplam süre: 5000 ms (5 saniye), her adım: 1000 ms (1 saniye)
            override fun onTick(millisUntilFinished: Long) {
                // Geriye kalan saniyeleri TextView'e yazdır
                countdownTextView.text = ((millisUntilFinished / 1000) + 1).toString()
            }

            override fun onFinish() {
                // Geri sayım tamamlandığında LoginActivity'ye yönlendirme yapılır
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent) // LoginActivity başlatılıyor
                finish() // SplashActivity kapatılıyor
            }
        }.start() // Geri sayım başlatılıyor
    }
}
