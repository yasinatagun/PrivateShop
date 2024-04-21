import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.privateshop.CartActivity
import com.example.privateshop.R
import com.example.privateshop.model.Product
import com.squareup.picasso.Picasso

class CartAdapter(private val items: MutableList<Product>, private val listener: CartActivity) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.product_image)
        val nameView: TextView = view.findViewById(R.id.product_name)
        val deleteButton: ImageButton = view.findViewById(R.id.btnDelete)
        val descriptionView: TextView = view.findViewById(R.id.product_description)
        val priceView: TextView = view.findViewById(R.id.product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = items[position]
        holder.nameView.text = product.name
        Picasso.get().load(product.image).into(holder.imageView)
        holder.descriptionView.text = product.description
        holder.priceView.text = "$ ${product.price}"


        holder.deleteButton.setOnClickListener {
            if (position < items.size) {
                items.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, items.size)
                listener.onItemRemoved()
            }
        }
    }

    override fun getItemCount() = items.size
}
