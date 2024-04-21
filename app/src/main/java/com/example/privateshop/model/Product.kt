package com.example.privateshop.model

import java.io.Serializable

data class Product (
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var delivery: String = "",
    var image: String = "",
    var price: String = "",
    var type: String = ""
) : Serializable
