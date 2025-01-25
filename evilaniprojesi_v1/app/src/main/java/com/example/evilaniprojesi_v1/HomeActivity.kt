package com.example.evilaniprojesi_v1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    // Firebase Firestore referansı
    private lateinit var firestore: FirebaseFirestore

    // RecyclerView ve Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private val postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firestore = FirebaseFirestore.getInstance() // Firestore başlatılıyor
        recyclerView = findViewById(R.id.recyclerView) // RecyclerView bağlanıyor

        // RecyclerView Ayarları
        recyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postList) // PostAdapter ile liste bağlanıyor
        recyclerView.adapter = postAdapter

        // Firestore'dan verileri çekme
        fetchPosts()
    }

    // Firestore'dan gönderileri çekmek için
    private fun fetchPosts() {
        firestore.collection("Posts") //
            .orderBy("date", Query.Direction.DESCENDING) // Tarihe göre sıralama (yeniden eskiye)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("HomeActivity", "Veri çekme hatası: ${exception.localizedMessage}")
                    Toast.makeText(this, "Veri çekme hatası: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    postList.clear()
                    for (document in snapshot.documents) {
                        try {
                            val post = document.toObject(Post::class.java)
                            if (post != null) {
                                postList.add(post) // Gönderiyi listeye ekle
                            }
                        } catch (e: Exception) {
                            Log.e("HomeActivity", "Veri dönüşüm hatası: ${e.localizedMessage}")
                        }
                    }
                    postAdapter.notifyDataSetChanged() // Adapteri güncelle
                }
            }
    }

    // Menü oluşturma
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu) // Menü düzenini şişirir
        return true
    }

    // Menü öğelerine tıklama işlemleri
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_post -> { //
                startActivity(Intent(this, AddPostActivity::class.java))
                true
            }
            R.id.logout -> { //
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Eski aktiviteleri temizle
                startActivity(intent) // Giriş ekranına geçiş
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
