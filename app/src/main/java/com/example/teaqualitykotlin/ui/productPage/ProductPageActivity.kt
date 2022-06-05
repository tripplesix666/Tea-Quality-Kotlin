package com.example.teaqualitykotlin.ui.productPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.teaqualitykotlin.R
import com.example.teaqualitykotlin.Tea
import com.example.teaqualitykotlin.databinding.ActivityProductPageBinding

class ProductPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tea = intent.extras!!.get("EXTRA") as Tea

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, TeaProductPageFragment.newInstance(tea.id))
                .commit()
        }

    }
}