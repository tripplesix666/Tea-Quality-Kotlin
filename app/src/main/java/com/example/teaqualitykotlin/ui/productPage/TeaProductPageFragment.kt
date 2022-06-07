package com.example.teaqualitykotlin.ui.productPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.teaqualitykotlin.R
import com.example.teaqualitykotlin.databinding.FragmentProductPageBinding
import com.example.teaqualitykotlin.factory

class TeaProductPageFragment : Fragment() {

    private lateinit var binding: FragmentProductPageBinding
    private val viewModel: TeaProductPageViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadTea(requireArguments().getLong(ARG_TEA_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductPageBinding.inflate(inflater, container, false)

        viewModel.teaDetails.observe(viewLifecycleOwner, Observer {
            binding.teaNameTextView.text = it.tea.name
            binding.teaImageView.setImageResource(R.drawable.tea)
            binding.productDescription.text = it.details
            binding.teaPriceTextView.text = it.tea.price
        })

        binding.addToCartButton.setOnClickListener {
            viewModel.moveTeaToCart()
            Toast.makeText(context, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object {

        private const val ARG_TEA_ID = "ARG_TEA_ID"

        fun newInstance(teaId: Long): TeaProductPageFragment {
            val fragment = TeaProductPageFragment()
            fragment.arguments = bundleOf(ARG_TEA_ID to teaId)
            return fragment
        }
    }
}