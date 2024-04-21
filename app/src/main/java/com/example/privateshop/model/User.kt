package com.example.privateshop.model

data class User(
    var name: String = "",
    var email: String = "",
    var cart: List<String> = listOf()
)