package com.example.privateshop

import CartAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.privateshop.databinding.ActivityCartBinding
import com.example.privateshop.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
interface CartInteractionListener {
    fun onItemRemoved()
}
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var currentUserInfo: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        updateTotalPrice()
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnBuy.setOnClickListener {
            goToBuyingActivity()
        }



    }
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.layoutManager = layoutManager
        // Pass 'this' as a listener to the adapter
        binding.rvCartItems.adapter = CartAdapter(MainActivity.cartItems, this)
    }
    private fun updateTotalPrice() {
        val total = MainActivity.cartItems.sumOf { product ->
            try {
                product.price.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        }

        binding.tvTotalPrice.text = "Total Price: $${"%.2f".format(total)}"
    }
    private fun goToBuyingActivity() {
        if (calculateTotalPrice() > 0){
            val intent = Intent(this, BuyingActivity::class.java)
            val totalPrice = calculateTotalPrice()
            intent.putExtra("TOTAL_PRICE", totalPrice)
            startActivity(intent)
        }else{
            Toast.makeText(this,"NO PRODUCT HERE", Toast.LENGTH_LONG).show()
        }
    }

    private fun calculateTotalPrice(): Double {
        return MainActivity.cartItems.sumOf { product ->
            try {
                product.price.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        }
    }
    override fun onResume() {
        super.onResume()
        updateTotalPrice()
    }

     fun onItemRemoved() {
        updateTotalPrice()
    }
}