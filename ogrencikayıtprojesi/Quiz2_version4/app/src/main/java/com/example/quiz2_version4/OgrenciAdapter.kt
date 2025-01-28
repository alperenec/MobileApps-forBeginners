package com.example.quiz2_version4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter

class OgrenciAdapter(
    private val context: Context,
    private var ogrenciler: MutableList<Ogrenci>
) : BaseAdapter() {

    override fun getCount(): Int = ogrenciler.size

    override fun getItem(position: Int): Any = ogrenciler[position]

    override fun getItemId(position: Int): Long = ogrenciler[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_ogrenci, parent, false)

        val ogrenci = ogrenciler[position]
        val textView = view.findViewById<TextView>(R.id.textViewOgrenci)
        textView.text = "${ogrenci.ad} ${ogrenci.soyad} ${ogrenci.numara} ${ogrenci.altDersVar}"

        return view
    }

    fun updateData(newData: List<Ogrenci>) {
        ogrenciler.clear()
        ogrenciler.addAll(newData)
        notifyDataSetChanged()
    }
}