package com.example.privateshop

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.privateshop.databinding.ActivityDetailBinding
import com.example.privateshop.model.Product
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var product: Product
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        intent.extras?.getSerializable("PRODUCT")?.let {
            product = it as Product
            displayProductDetails()
        }

        binding.addToCartButton.setOnClickListener {
            MainActivity.cartItems.add(product)
            Toast.makeText(this, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayProductDetails() {
        binding.productName.text = product.name
        binding.productDescription.text = product.description
        binding.productPrice.text = String.format("$%.2f", product.price.toDouble())
        Picasso.get().load(product.image).into(binding.productImage)
    }
}
