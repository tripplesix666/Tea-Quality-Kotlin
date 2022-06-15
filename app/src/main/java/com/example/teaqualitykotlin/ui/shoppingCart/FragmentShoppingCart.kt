package com.example.teaqualitykotlin.ui.shoppingCart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        var count = 0
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
        viewModel.refreshPrice()
        viewModel.calculatePrice()
        viewModel.calculateDiscount()
        viewModel.calculateTotalPrice()

        binding.addToCartButton.setOnClickListener {
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(firebaseAuth.uid.toString()).child(NODE_SALE).child(
                CHILD_SUM).get().addOnSuccessListener { count = it.value.toString().toInt() }

            count += viewModel.totalPrice
            REF_DATABASE_ROOT.child(NODE_USERS).child(firebaseAuth.uid.toString()).child(NODE_SALE).child(
                CHILD_SUM).setValue(count)
            Toast.makeText(context, "$count", Toast.LENGTH_SHORT).show()
        }

        binding.cardView.setOnClickListener {
            binding.amountOfProductsTv.text = viewModel.countPrice.toString()

        }
        binding.saleTv.text = viewModel.countOfDiscount.toString()
        binding.amountOfProductsTv.text = viewModel.countPrice.toString()
        binding.totalPriceTv.text = viewModel.totalPrice.toString()

        viewModel.teas.observe(viewLifecycleOwner, Observer { adapter.teasCart = it })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.layoutManager = layoutManager
        binding.cartRecyclerView.adapter = adapter

        return binding.root
    }
}