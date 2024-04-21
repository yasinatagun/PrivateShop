package com.example.privateshop

import ProductAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.privateshop.databinding.ActivityMainBinding
import com.example.privateshop.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.example.privateshop.model.User

import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    companion object {
        var cartItems: MutableList<Product> = mutableListOf()
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var currentUserInfo: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // CODE FOR ADDING BATCH PRODUCTS TO DATABASE
//        val products = generateProducts()
//        addProductsToFirestore(products)

        setupRecyclerView()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUserData(currentUser.email!!)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun fetchUserData(email: String) {
        firestore.collection("users").document(email).get().addOnSuccessListener { document ->
            if (document.exists()) {
                try {
                    currentUserInfo = document.toObject(User::class.java)
                    currentUserInfo?.let {
                        updateUI(it)
                    } ?: run {
                        Toast.makeText(this, "Failed to parse user data", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to convert data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this,
                "Error fetching user details: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: User) {
        binding.tvUsername.text = "Welcome, ${user.name}!"
    }
    fun setupRecyclerView() {
        firestore.collection("products").get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.d("MainActivity", "No products found")
            }
            val products = result.toObjects(Product::class.java)
            Log.d("MainActivity", "Fetched products: ${products.size}")
            val layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.rvProductList.layoutManager = layoutManager
            val adapter = ProductAdapter(products, this) { product ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("PRODUCT_NAME", product.name)
                intent.putExtra("PRODUCT_PRICE", product.price)
                intent.putExtra("PRODUCT_IMAGE", product.image)
                startActivity(intent)
            }
            binding.rvProductList.adapter = adapter
        }.addOnFailureListener { exception ->
            Log.e("MainActivity", "Error fetching products", exception)
        }
    }

fun updateCartIcon() {
    if (cartItems.isNotEmpty()) {
        binding.btnCart.setBackgroundResource(R.drawable.circle_orange)
    } else {
        binding.btnCart.setBackgroundResource(R.drawable.circle_white)
    }
}
//    private fun generateProducts(): List<Product> {
//        val products = mutableListOf<Product>()
//        // Example of manually adding unique products
//        products.add(Product("0", "Nike Air Max 250", "Versatile and super comfortable running shoes with Air Max cushioning.", "3-5 working days", "url_to_image1", "140", "Men's Sports Shoes"))
//        products.add(Product("1", "Nike Air Max 270", "Versatile and super comfortable running shoes with Air Max cushioning.", "3-5 working days", "url_to_image1", "150", "Men's Sports Shoes"))
//        products.add(Product("2", "Adidas Ultraboost 21", "Responsive running shoes with precision-made, seamless zones combined with the BOOST technology.", "3-5 working days", "url_to_image2", "180", "Men's Sports Shoes"))
//        products.add(Product("3", "Asics Gel-Kayano 28", "High-mileage stability running shoe that improves comfort and breathability.", "3-5 working days", "url_to_image3", "160", "Men's Sports Shoes"))
//        products.add(Product("4", "Brooks Ghost 14", "A balance of softness and responsiveness with simplified midsole construction.", "3-5 working days", "url_to_image4", "130", "Men's Sports Shoes"))
//        products.add(Product("5", "New Balance Fresh Foam 1080v11", "Provides luxurious comfort for long runs, featuring a soft Fresh Foam midsole.", "3-5 working days", "url_to_image5", "150", "Men's Sports Shoes"))
//        products.add(Product("6", "Nike ZoomX Vaporfly", "Built for record-breaking speed with a light and fast feel.", "3-5 working days", "url_to_image6", "250", "Women's Sports Shoes"))
//        products.add(Product("7", "Adidas Solar Glide", "Shoes with stitched-in reinforcement for targeted support in the midfoot.", "3-5 working days", "url_to_image7", "140", "Women's Sports Shoes"))
//        products.add(Product("8", "Puma Deviate Nitro", "High-performance shoes offering superior cushioning for a comfortable run.", "3-5 working days", "url_to_image8", "160", "Women's Sports Shoes"))
//        products.add(Product("9", "Hoka One One Clifton 8", "Lightweight cushioning and a smoother ride from the world’s lightest brand.", "3-5 working days", "url_to_image9", "135", "Women's Sports Shoes"))
//        products.add(Product("10", "Mizuno Wave Rider 25", "Offers a super soft and bouncy feeling with MIZUNO ENERZY foam.", "3-5 working days", "url_to_image10", "120", "Women's Sports Shoes"))
//        products.add(Product("11", "Reebok Floatride Energy 3", "Lightweight, responsive running shoe ideal for daily runs.", "3-5 working days", "url_to_image11", "110", "Men's Sports Shoes"))
//        products.add(Product("12", "Under Armour HOVR Sonic 4", "Offers a 'zero gravity feel' with a soft, energy-returning midsole.", "3-5 working days", "url_to_image12", "140", "Men's Sports Shoes"))
//        products.add(Product("13", "Salomon Speedcross 5", "Trail running shoes with superior grip and durability.", "3-5 working days", "url_to_image13", "130", "Men's Sports Shoes"))
//        products.add(Product("14", "Nike Air Zoom Pegasus 38", "Durable and lightweight, ideal for everyday training.", "3-5 working days", "url_to_image14", "120", "Men's Sports Shoes"))
//        products.add(Product("15", "Altra Lone Peak 5", "Features a balanced cushioning that positions the heel and forefoot at the same distance.", "3-5 working days", "url_to_image15", "135", "Men's Sports Shoes"))
//        products.add(Product("16", "Merrell Vapor Glove 4", "Minimalist trail running shoe for a zero drop and barefoot feel.", "3-5 working days", "url_to_image16", "100", "Men's Sports Shoes"))
//        products.add(Product("17", "Saucony Guide 14", "Comfortable and sturdy, great for runners who need slight pronation support.", "3-5 working days", "url_to_image17", "130", "Men's Sports Shoes"))
//        products.add(Product("18", "La Sportiva Bushido II", "Technical trail runner designed for the world's most rugged conditions.", "3-5 working days", "url_to_image18", "145", "Women's Sports Shoes"))
//        products.add(Product("19", "Topo Athletic Fli-Lyte 3", "Offers a soft, lightweight ride with a roomy toe box.", "3-5 working days", "url_to_image19", "125", "Women's Sports Shoes"))
//        products.add(Product("20", "Scott Kinabalu 2.0", "Mountain running shoe that provides grip and protection.", "3-5 working days", "url_to_image20", "140", "Women's Sports Shoes"))
//        products.add(Product("21", "On Cloudswift", "Urban running shoe with Helion superfoam for high performance.", "3-5 working days", "url_to_image21", "150", "Women's Sports Shoes"))
//        products.add(Product("22", "Skechers GOrun MaxRoad 4 Hyper", "Lightweight and cushioned, designed for long distances on the road.", "3-5 working days", "url_to_image22", "110", "Women's Sports Shoes"))
//        products.add(Product("23", "Columbia Montrail F.K.T.", "Lightweight, rugged shoe designed for speed hiking and trail running.", "3-5 working days", "url_to_image23", "135", "Women's Sports Shoes"))
//        products.add(Product("24", "Vibram FiveFingers V-Trail 2.0", "A minimalist shoe for trail running with a glove-like fit.", "3-5 working days", "url_to_image24", "120", "Women's Sports Shoes"))
//        products.add(Product("25", "Newton Running Distance 9", "Built for speed and endurance with advanced cushioning technology.", "3-5 working days", "url_to_image25", "160", "Women's Sports Shoes"))
//
//        return products
//    }

    //    private fun addProductToFirestore(product: Product) {
//        firestore.collection("products").document(product.id).set(product)
//            .addOnSuccessListener {
//                // Handle successful addition of product
//                println("Product added successfully")
//            }
//            .addOnFailureListener { e ->
//                // Handle failed addition of product
//                println("Error adding product: ${e.message}")
//            }
//    }
//    private fun addProductsToFirestore(products: List<Product>) {
//        products.forEach { product ->
//            firestore.collection("products").document(product.name).set(product)
//                .addOnSuccessListener {
//                    // Log or handle successful addition
//                    println("Product added successfully: ${product.name}")
//                }
//                .addOnFailureListener { e ->
//                    // Log or handle failed addition
//                    println("Error adding product ${product.name}: ${e.message}")
//                }
//        }
//    }

}
