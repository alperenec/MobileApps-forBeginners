package com.example.vizeapp_v1

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Yemekleri tutan bir liste oluşturuluyor (başlangıçta 3 yemek var)
    private val meals = mutableListOf(
        Meal("Abısta (Çerkez Mutfağı)", "-1 kg mısır unu\n-1,5 kg su\n-0,5 kg süt\n-1 çorba kaşığı tuz\n-0,5 kg Çerkes peyniri\n-150 gr tereyağı", "Süt ve su büyükçe bir tencereye konularak tuz ilave edilip 90 derece kaynatılır..."),
        Meal("Acı-gıcı", "Dolma içi gibi bulgur, kavurma, tuz, baharat", "Acı-gıcı denilen (ısırganotu) otun uzun yapraklarından yapılır..."),
        Meal("Boza", "Makarna, Su, Tuz, Yağ", "Sarı mısırın suyla kaynatılarak mayalandırılması...")
    )

    // ListView ile kullanılacak olan adapter tanımlanıyor
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ana arayüz yükleniyor

        // Toolbar tanımlanıyor ve aksiyon çubuğu olarak ayarlanıyor
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Yemeklerin sadece isimlerinden oluşan bir liste oluşturuluyor
        val mealNames = meals.map { it.name }.toMutableList()
        val listView = findViewById<ListView>(R.id.meal_list_view)

        // Yemek isimlerini göstermek için bir ArrayAdapter oluşturuluyor
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mealNames)
        listView.adapter = adapter // Adapter, ListView'e bağlanıyor

        // Listeye kısa tıklama: Seçilen yemeğin detaylarını göstermek için bir Intent başlatılıyor
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedMeal = meals[position] // Tıklanan öğeye karşılık gelen yemek
            val intent = Intent(this, MealDetailActivity::class.java)
            // Yemek bilgileri Intent ile diğer aktiviteye aktarılıyor
            intent.putExtra("meal_name", selectedMeal.name)
            intent.putExtra("meal_ingredients", selectedMeal.ingredients)
            intent.putExtra("meal_recipe", selectedMeal.recipe)
            startActivity(intent) // Detay ekranı başlatılıyor
        }

        // Listeye uzun tıklama: Yemek silmek için bir onay penceresi açılıyor
        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(this)
                .setTitle("Yemeği Sil") // Dialog başlığı
                .setMessage("${meals[position].name} silinsin mi?") // Silme onayı mesajı
                .setPositiveButton("Evet") { _: DialogInterface, _: Int ->
                    // Yemek listeden siliniyor ve adapter güncelleniyor
                    meals.removeAt(position)
                    mealNames.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("Hayır", null) // Hayır seçeneği
                .show() // Dialog gösteriliyor
            true
        }

        // Geri tuşuna özel bir davranış atanıyor
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                clearCredentialsAndGoToLogin() // Geri tuşu ile giriş ekranına dönülüyor
            }
        })
    }

    // Menü oluşturuluyor (üstteki üç noktalı menü)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu) // Menüdeki öğeler yükleniyor
        return true
    }

    // Menü öğelerine tıklama işlemleri
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_meal -> {
                showAddMealDialog() // Yemek ekleme dialogu açılıyor
                true
            }
            R.id.menu_game -> {
                showGameDialog() // Oyun ekranı açılıyor
                true
            }
            else -> super.onOptionsItemSelected(item) // Diğer durumlar
        }
    }

    // Yemek ekleme için bir dialog gösteriliyor
    private fun showAddMealDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null)

        // Dialog içindeki input alanları tanımlanıyor
        val mealNameInput = dialogView.findViewById<EditText>(R.id.edit_meal_name)
        val mealIngredientsInput = dialogView.findViewById<EditText>(R.id.edit_meal_ingredients)
        val mealRecipeInput = dialogView.findViewById<EditText>(R.id.edit_meal_recipe)

        AlertDialog.Builder(this)
            .setTitle("Yeni Yemek Ekle") // Dialog başlığı
            .setView(dialogView) // Dialog görünümü
            .setPositiveButton("Kaydet") { _, _ ->
                // Kullanıcının girdiği veriler alınıyor
                val mealName = mealNameInput.text.toString()
                val mealIngredients = mealIngredientsInput.text.toString()
                val mealRecipe = mealRecipeInput.text.toString()

                // Tüm alanlar doluysa yeni yemek listeye ekleniyor
                if (mealName.isNotBlank() && mealIngredients.isNotBlank() && mealRecipe.isNotBlank()) {
                    meals.add(Meal(mealName, mealIngredients, mealRecipe)) // Yeni yemek ekleniyor
                    adapter.add(mealName) // Adapter güncelleniyor
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Yemek eklendi!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tüm alanları doldurmanız gerekiyor!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("İptal", null) // İptal seçeneği
            .create()
            .show()
    }

    // Oyun ekranı için bir dialog gösteriliyor
    private fun showGameDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_game, null)

        // Oyun bileşenleri tanımlanıyor
        val scrambledWordText = dialogView.findViewById<TextView>(R.id.scrambled_word)
        val guessInput = dialogView.findViewById<EditText>(R.id.guess_input)
        val checkButton = dialogView.findViewById<Button>(R.id.check_button)
        val scoreText = dialogView.findViewById<TextView>(R.id.score_text)

        // Kelime listesi ve oyun değişkenleri
        val wordList = listOf("armut", "elma", "kavun", "kiraz", "portakal", "kivi", "karpuz", "mandalin")
        var currentScore = 0
        var currentWord = wordList.random() // Rastgele bir kelime seçiliyor
        scrambledWordText.text = currentWord.toList().shuffled().joinToString("") // Kelime karıştırılıyor

        // Dialog için bir AlertDialog oluşturuluyor
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Kapatılabilir değil
            .create()

        // Tahmin kontrol butonu
        checkButton.setOnClickListener {
            val userGuess = guessInput.text.toString().trim()
            if (userGuess.equals(currentWord, ignoreCase = true)) {
                currentScore += 10 // Doğru tahmin
                scoreText.text = "Puan: $currentScore"
                Toast.makeText(this, "Doğru Tahmin!", Toast.LENGTH_SHORT).show()
                currentWord = wordList.random() // Yeni kelime seçiliyor
                scrambledWordText.text = currentWord.toList().shuffled().joinToString("")
                guessInput.text.clear()
            } else {
                Toast.makeText(this, "Yanlış Tahmin. Tekrar Deneyin!", Toast.LENGTH_SHORT).show()
            }
        }

        // Oyunu kapatma butonu
        dialogView.findViewById<Button>(R.id.close_game_button).setOnClickListener {
            alertDialog.dismiss() // Dialog kapatılıyor
        }

        alertDialog.show() // Dialog gösteriliyor
    }

    // Kullanıcı giriş bilgilerini temizleyip giriş ekranına dönüyor
    private fun clearCredentialsAndGoToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("clear_credentials", true) // Giriş bilgilerini temizleme bayrağı
        startActivity(intent) // LoginActivity başlatılıyor
        finish() // MainActivity kapanıyor
    }
}
