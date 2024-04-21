import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.privateshop.DetailActivity
import com.example.privateshop.MainActivity
import com.example.privateshop.R
import com.squareup.picasso.Picasso
import com.example.privateshop.model.Product
import java.io.Serializable

class ProductAdapter(private val productList: List<Product>, private val context: Context, private val onProductClicked: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.product_image)
        val priceView: TextView = view.findViewById(R.id.product_price)
        val nameView: TextView = view.findViewById(R.id.product_name)
        val btnAddCart: ImageButton = view.findViewById(R.id.add_to_cart_button)
        val itemLayout: View = view.findViewById(R.id.item_layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.nameView.text = product.name
        Picasso.get().load(product.image).into(holder.imageView)
        holder.priceView.text = "$ ${product.price}"
        holder.btnAddCart.setOnClickListener {
            MainActivity.cartItems.add(product)
            Toast.makeText(holder.itemView.context, "Added to cart: ${product.name}", Toast.LENGTH_SHORT).show()
            (holder.itemView.context as MainActivity).updateCartIcon()
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("PRODUCT", product as Serializable)
            }
            context.startActivity(intent)
        }
    }


    override fun getItemCount() = productList.size
}
