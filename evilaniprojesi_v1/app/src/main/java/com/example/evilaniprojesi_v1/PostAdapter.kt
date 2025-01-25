package com.example.evilaniprojesi_v1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // ViewHolder: Her bir liste öğesi için görünümleri tanımlar
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userEmailText: TextView = itemView.findViewById(R.id.userEmailText)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
        val postTitle: TextView = itemView.findViewById(R.id.postTitle)
        val areaText: TextView = itemView.findViewById(R.id.areaText)
        val roomCountText: TextView = itemView.findViewById(R.id.roomCountText)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
        val cityText: TextView = itemView.findViewById(R.id.cityText)
    }

    // Görünüm oluşturma: item_post.xml'i şişirir ve bir ViewHolder döndürür
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    // Görünüm ve veri bağlama: Belirli bir pozisyondaki veriyi ViewHolder'a bağlar
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position] // İlgili pozisyondaki Post nesnesini al

        holder.userEmailText.text = post.userEmail
        holder.postTitle.text = post.title
        holder.areaText.text = "${post.area} m²"
        holder.roomCountText.text = "${post.roomCount} oda"
        holder.priceText.text = "${post.price} ₺"

        // Şehir bilgisini ayarla
        holder.cityText.text = post.city

        // Picasso kütüphanesi ile yükle
        Picasso.get().load(post.downloadUrl).into(holder.postImage)
    }

    override fun getItemCount(): Int = postList.size
}
