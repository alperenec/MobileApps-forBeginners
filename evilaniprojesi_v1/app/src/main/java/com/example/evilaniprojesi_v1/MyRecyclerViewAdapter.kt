package com.example.evilaniprojesi_v1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// RecyclerView.Adapter sınıfı, RecyclerView için bir bağlayıcıdır
class MyRecyclerViewAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

    // ViewHolder sınıfı, tek bir RecyclerView öğesini temsil eder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView) // Görünümdeki TextView'i bağlar
    }

    // onCreateViewHolder: Yeni bir görünüm oluşturur
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false) // item_recycler_view.xml dosyasını şişirir
        return MyViewHolder(view) // Oluşturulan görünümü ViewHolder'a bağlar
    }

    // onBindViewHolder: Veriyi görünümle bağlar
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = dataList[position] // Listeden alınan veriyi TextView'e ayarlar
    }

    // getItemCount: Listenin eleman sayısını döndürür
    override fun getItemCount(): Int = dataList.size
}
