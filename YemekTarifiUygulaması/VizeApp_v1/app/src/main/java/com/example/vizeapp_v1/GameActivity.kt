package com.example.vizeapp_v1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    // Oyun sırasında tutulan skor
    private var currentScore = 0

    // Şu anda üzerinde çalışılan kelime
    private lateinit var currentWord: String

    // Oyun için kullanılacak kelimelerin listesi
    private val wordList = listOf("armut", "elma", "cilek", "kiraz", "portakal", "muz")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game) // Oyun ekranı arayüzü yükleniyor

        // Arayüzdeki bileşenler tanımlanıyor
        val scrambledWordText = findViewById<TextView>(R.id.scrambled_word) // Karışık kelimeyi gösterecek TextView
        val guessInput = findViewById<EditText>(R.id.guess_input) // Kullanıcıdan tahmin alınan EditText
        val checkButton = findViewById<Button>(R.id.check_button) // Kontrol butonu
        val scoreText = findViewById<TextView>(R.id.score_text) // Skoru gösterecek TextView

        // İlk kelimeyi rastgele seç ve karıştırarak ekranda göster
        currentWord = wordList.random() // Kelime listesi içinden rastgele bir kelime seçiliyor
        scrambledWordText.text = currentWord.toList().shuffled().joinToString("") // Seçilen kelimenin harfleri karıştırılıyor

        // Kontrol butonuna tıklama olayı
        checkButton.setOnClickListener {
            val userGuess = guessInput.text.toString().trim() // Kullanıcıdan gelen tahmin alınır ve boşluklar kırpılır
            if (userGuess.equals(currentWord, ignoreCase = true)) { // Tahmin doğru mu kontrol edilir (büyük/küçük harf duyarsız)
                currentScore += 10 // Doğru tahminde skor 10 artırılır
                scoreText.text = "Puan: $currentScore" // Güncel skor ekranda gösterilir
                Toast.makeText(this, "Doğru Tahmin!", Toast.LENGTH_SHORT).show() // Kullanıcıya doğru tahmin mesajı gösterilir

                // Yeni kelime seç ve karıştır
                currentWord = wordList.random() // Yeni rastgele kelime seçiliyor
                scrambledWordText.text = currentWord.toList().shuffled().joinToString("") // Yeni kelimenin harfleri karıştırılıyor
                guessInput.text.clear() // Kullanıcının önceki tahmini temizleniyor
            } else {
                // Yanlış tahmin durumunda mesaj gösterilir
                Toast.makeText(this, "Yanlış Tahmin. Tekrar Deneyin!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
