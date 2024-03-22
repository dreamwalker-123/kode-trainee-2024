package com.example.kode.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: String,
    val position: String,
    val birthday: String, //<Date>
    val phone: String,
)

@Serializable
data class Items(
    val items: List<Item>? = null,
)