package com.skyboundapps.milkproducts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.skyboundapps.milkproducts.databinding.FragmentAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Log

class add : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private var imageUri: Uri? = null
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        // Set up the image upload button
        binding.uploadImageButton.setOnClickListener {
            pickImage()
        }

        // Set up the submit button to upload product details
        binding.saveProductButton.setOnClickListener {
            uploadProduct()
        }

        return binding.root
    }

    // Function to pick image from the gallery
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    // Launcher to handle image selection result
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            binding.productImage.setImageURI(imageUri)  // Display selected image in an ImageView
            Log.d("AddFragment", "Image URI selected: $imageUri")
            Toast.makeText(requireContext(), "Image selected successfully", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("AddFragment", "Image selection failed")
            Toast.makeText(requireContext(), "Image selection failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to upload product data to Realtime Database
    private fun uploadProduct() {
        val productName = binding.productNameEditText.text.toString()
        val productPrice = binding.productPrice.text.toString()
        val productPackageDate = binding.packageDate.text.toString()
        val productExpiryDate = binding.expiryDate.text.toString()

        if (productName.isEmpty() || productPrice.isEmpty() || imageUri == null) {
            Toast.makeText(requireContext(), "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("AddFragment", "Starting product data upload...")
        Toast.makeText(requireContext(), "Uploading product data...", Toast.LENGTH_SHORT).show()

        // Prepare product data
        val productData = hashMapOf(
            "name" to productName,
            "price" to productPrice,
            "imageUri" to imageUri.toString(), // Using imageUri here as an example
            "packageDate" to productPackageDate,
            "expiryDate" to productExpiryDate
        )

        // Upload product data to Realtime Database
        val productRef = database.child("products").push() // Generates a unique ID
        productRef.setValue(productData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Product uploaded successfully", Toast.LENGTH_SHORT).show()
                clearFields()
                Log.d("AddFragment", "Product data uploaded to Realtime Database successfully.")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to upload product: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("AddFragment", "Failed to upload product data to Realtime Database: ${exception.message}")
            }
    }

    // Function to clear the fields after successful upload
    private fun clearFields() {
        binding.productNameEditText.text.clear()
        binding.productPrice.text.clear()
        binding.packageDate.text.clear()
        binding.expiryDate.text.clear()
        binding.productImage.setImageURI(null)  // Clear the image
        imageUri = null  // Reset the image URI
        Toast.makeText(requireContext(), "Fields cleared", Toast.LENGTH_SHORT).show()
        Log.d("AddFragment", "All fields cleared.")
    }
}
