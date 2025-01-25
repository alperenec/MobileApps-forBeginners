package com.example.evilaniprojesi_v1

data class Post(
        val userEmail: String = "", // Gönderiyi paylaşan kullanıcının e-postası
        val title: String = "",
        val area: String = "",
        val roomCount: String = "",
        val price: String = "",
        val city: String = "",
        val downloadUrl: String = "",
        val date: Long = System.currentTimeMillis()
)
