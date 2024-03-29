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
import com.example.teaqualitykotlin.adapters.AdapterHomeTeas
import com.example.teaqualitykotlin.adapters.TeaActionListener
import com.example.teaqualitykotlin.databinding.FragmentFavoriteBinding

class FragmentFavorite : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: AdapterHomeTeas

    private val viewModel: FragmentFavoriteViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        adapter = AdapterHomeTeas(object : TeaActionListener {
            override fun onTeaDetails(tea: Tea) {
                navigator().showProductPage(tea)
            }

            override fun onMoreAction(tea: Tea) {
                Toast.makeText(context, "Удалено из избранного", Toast.LENGTH_SHORT).show()
                viewModel.deleteTea(tea)
            }

        })

        viewModel.teas.observe(viewLifecycleOwner, Observer { adapter.teas = it })

//        val layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewFavorite.layoutManager = layoutManager
        binding.recyclerViewFavorite.adapter = adapter



        binding.countTextView.text = adapter.itemCount.toString()

//        viewModel.count.observe(viewLifecycleOwner) {
//            viewModel.count.value = adapter.itemCount.toString()
//            binding.countTextView.text = viewModel.count(adapter)
//        }
        binding.favoriteTextView.setOnClickListener {
            viewModel.populateBd()
        }



        binding.countTextView.setOnClickListener {
            Toast.makeText(context, "${adapter.itemCount}", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}