package com.example.teaqualitykotlin.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teaqualitykotlin.*
import com.example.teaqualitykotlin.adapters.AdapterMainTeas
import com.example.teaqualitykotlin.adapters.TeaActionListener
import com.example.teaqualitykotlin.databinding.FragmentFavoriteBinding

class FragmentFavorite : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: AdapterMainTeas

    private val viewModel: FragmentFavoriteViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        adapter = AdapterMainTeas(object : TeaActionListener {
            override fun onTeaDetails(tea: Tea) {
                navigator().showProductPage(tea)
            }

            override fun onMoreAction(tea: Tea) {
                Toast.makeText(context, "Удалено из избранного", Toast.LENGTH_SHORT).show()
                viewModel.deleteTea(tea)
            }

        })

        viewModel.teas.observe(viewLifecycleOwner, Observer { adapter.teas = it })


        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewFavorite.layoutManager = layoutManager
        binding.recyclerViewFavorite.adapter = adapter

        binding.countTextView.text = "${adapter.itemCount}шт"

        binding.favoriteTextView.setOnClickListener {
            viewModel.populateDb()
        }


        binding.countTextView.setOnClickListener {
            Toast.makeText(context, "${adapter.itemCount}шт", Toast.LENGTH_SHORT).show()
            binding.countTextView.text = "${adapter.itemCount}шт"
        }

        return binding.root
    }


}