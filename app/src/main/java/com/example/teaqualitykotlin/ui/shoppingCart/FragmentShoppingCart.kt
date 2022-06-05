package com.example.teaqualitykotlin.ui.shoppingCart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teaqualitykotlin.*
import com.example.teaqualitykotlin.databinding.FragmentShoppingCartBinding

class FragmentShoppingCart : Fragment() {

    private lateinit var binding: FragmentShoppingCartBinding
    private lateinit var adapter: AdapterCartTeas

    private val viewModel: FragmentShoppingCartViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        adapter = AdapterCartTeas(object : TeaCartActionListener {
            override fun onTeaDetails(tea: Tea) {
                navigator().showProductPage(tea)
            }

            override fun onTeaDelete(tea: Tea) {
                viewModel.deleteTeaCart(tea)
            }

            override fun onTeaCountPlus(tea: Tea) {
                TODO("Not yet implemented")
            }

            override fun onTeaCountMinus(tea: Tea) {
                TODO("Not yet implemented")
            }

        })

        viewModel.teas.observe(viewLifecycleOwner, Observer { adapter.teasCart = it })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.layoutManager = layoutManager
        binding.cartRecyclerView.adapter = adapter

        return binding.root
    }
}