package com.example.teaqualitykotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.teaqualitykotlin.databinding.ActivityMainBinding
import com.example.teaqualitykotlin.ui.authorization.SignInActivity
import com.example.teaqualitykotlin.ui.favorite.FragmentFavorite
import com.example.teaqualitykotlin.ui.productPage.ProductPageActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.Serializable

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser == null){
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showProductPage(tea: Tea) {
        val intent = Intent(this, ProductPageActivity::class.java)
        intent.putExtra("EXTRA", tea as Serializable)
        startActivity(intent)
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun showFavorite() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.nav_host_fragment_activity_main, FragmentFavorite())
            .commit()
    }
}