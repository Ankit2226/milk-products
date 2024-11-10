package com.skyboundapps.milkproducts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.skyboundapps.milkproducts.databinding.ActivityMainBinding
import com.skyboundapps.milkproducts.databinding.ActivityMainContentBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var contentBinding: ActivityMainContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bind the main layout (activity_main.xml) containing the DrawerLayout
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)  // Set mainBinding as the root layout

        // Bind the main content layout (activity_main_content.xml) to access menubtn
        contentBinding = ActivityMainContentBinding.inflate(layoutInflater)

        // Attach the main content to the drawer layout
        mainBinding.drawerlayout.addView(contentBinding.root)

        // Set up the menu button to open the drawer
        contentBinding.menubtn.setOnClickListener {
            mainBinding.drawerlayout.openDrawer(GravityCompat.START)
        }

        // Set up bottom navigation view to switch fragments based on item selection
        contentBinding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replacefragment(Home())
                R.id.add -> replacefragment(add())  // Ensure 'Add' Fragment is defined
                R.id.person -> replacefragment(person())  // Ensure 'Person' Fragment is defined
                else -> {
                    // Handle other cases if needed
                }
            }
            true  // Return true to indicate item selection handled
        }
    }

    // Method to replace fragments
    private fun replacefragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()
    }
}
