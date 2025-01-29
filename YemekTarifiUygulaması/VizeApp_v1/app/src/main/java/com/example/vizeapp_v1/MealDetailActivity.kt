package com.example.vizeapp_v1

// Gerekli Android kütüphaneleri projeye dahil ediliyor
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MealDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_detail) // Yemek detaylarının gösterileceği arayüz yükleniyor

        // Intent ile diğer aktiviteden alınan veriler (yemek bilgileri) burada çekiliyor
        val mealName = intent.getStringExtra("meal_name") // Yemek adı
        val mealIngredients = intent.getStringExtra("meal_ingredients") // Yemek malzemeleri
        val mealRecipe = intent.getStringExtra("meal_recipe") // Yemek tarifi

        // Arayüzdeki bileşenler bulunuyor ve gelen veriler bu bileşenlere atanıyor
        findViewById<TextView>(R.id.meal_name).text = mealName // Yemek adı TextView'e atanıyor
        findViewById<TextView>(R.id.meal_ingredients).text = mealIngredients // Yemek malzemeleri TextView'e atanıyor
        findViewById<TextView>(R.id.meal_recipe).text = mealRecipe // Yemek tarifi TextView'e atanıyor
    }
}
