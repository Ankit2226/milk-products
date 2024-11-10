package com.skyboundapps.milkproducts.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skyboundapps.milkproducts.R
import com.skyboundapps.milkproducts.model.Product

class ProductAdapter(
    private val context: Context,
    private val productList: List<Product>,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        // Bind data to views
        holder.productName.text = product.name
        holder.productPrice.text = product.price
        holder.packageDate.text = product.packageDate
        holder.expiryDate.text = product.expiryDate

        // Parse and load image from URI
        val imageUri = Uri.parse(product.imageUrl)
        Glide.with(context)
            .load(imageUri)  // Use Uri.parse for content:// URIs
            .placeholder(R.drawable.upload)  // Placeholder image
            .error(R.drawable.milkproduct)  // Error image in case of failure
            .into(holder.productImage)

        // Set the click listener for each item
        holder.itemView.setOnClickListener { onClick(product) }
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val packageDate: TextView = itemView.findViewById(R.id.packageDate)
        val expiryDate: TextView = itemView.findViewById(R.id.expiryDate)
    }
}
