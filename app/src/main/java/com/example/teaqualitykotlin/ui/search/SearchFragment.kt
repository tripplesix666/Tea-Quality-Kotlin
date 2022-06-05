package com.example.teaqualitykotlin.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teaqualitykotlin.*
import com.example.teaqualitykotlin.adapters.AdapterHomeTeas
import com.example.teaqualitykotlin.adapters.TeaActionListener
import com.example.teaqualitykotlin.databinding.FragmentSearchBinding
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: AdapterHomeTeas

    private val viewModel: SearchViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapter = AdapterHomeTeas(object : TeaActionListener {
            override fun onTeaDetails(tea: Tea) {
                navigator().showProductPage(tea)
            }

            override fun onMoreAction(tea: Tea) {
                viewModel.moveTea(tea)
                Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
            }

        })
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                binding.searchView.clearFocus()

                Toast.makeText(context, "$newText", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                viewModel.searchTea(newText!!)
//                binding.recyclerView.adapter!!.notifyDataSetChanged()


                return true
            }

        })

        viewModel.teas.observe(viewLifecycleOwner, Observer { adapter.teas = it })

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        return binding.root
    }
}