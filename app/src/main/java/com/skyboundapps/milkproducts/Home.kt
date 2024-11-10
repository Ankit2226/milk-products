package com.skyboundapps.milkproducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.skyboundapps.milkproducts.adapter.ProductAdapter
import com.skyboundapps.milkproducts.databinding.FragmentHomeBinding
import com.skyboundapps.milkproducts.model.Product

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().getReference("products") // Path in Realtime Database

        setupRecyclerView()
        fetchProducts()

        return binding.root
    }

    private fun setupRecyclerView() {
        // Passing the correct parameters (context, productList, onClick callback)
        productAdapter = ProductAdapter(requireContext(), productList) { product ->
            Toast.makeText(requireContext(), "Clicked: ${product.name}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }

    private fun fetchProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                productList.clear()
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let { productList.add(it) }
                }
                productAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch products: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
