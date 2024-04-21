package com.example.privateshop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.privateshop.databinding.ActivityBuyingBinding
import java.util.*

class BuyingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewInsets()

        val totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0)
        binding.tvTotalPrice.text = "Total Price: $%.2f".format(totalPrice)
        binding.tvUsername.text = "Buy Them All"
        binding.btnBuy.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, SucceedActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupViewInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.etPhone.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidCreditCard(binding.etCreditCardNumber.text.toString().trim())) {
            Toast.makeText(this, "Invalid credit card number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.etCvv.text.toString().length != 3) {
            Toast.makeText(this, "CVV must be 3 digits", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidCreditCard(number: String): Boolean {
        return number.length in 13..16
    }

    private fun isValidExpiryDate(expiryDate: String): Boolean {
        return try {
            val parts = expiryDate.split("/")
            val month = parts[0].toInt()
            val year = parts[1].toInt() + 2000
            val expiry = Calendar.getInstance()
            expiry.set(Calendar.YEAR, year)
            expiry.set(Calendar.MONTH, month - 1)
            val current = Calendar.getInstance()
            expiry.after(current)
        } catch (e: Exception) {
            false
        }
    }
}
