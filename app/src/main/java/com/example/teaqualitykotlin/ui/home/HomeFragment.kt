package com.example.teaqualitykotlin.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.teaqualitykotlin.*
import com.example.teaqualitykotlin.adapters.AdapterHomeTeas
import com.example.teaqualitykotlin.adapters.TeaActionListener
import com.example.teaqualitykotlin.adapters.viewPager2Adapter
import com.example.teaqualitykotlin.databinding.FragmentHomeBinding
import java.util.ArrayList
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterHomeTeas: AdapterHomeTeas
    private lateinit var adapter: AdapterHomeTeas

    private lateinit var imageList: ArrayList<Int>
    private lateinit var handler : Handler
    private lateinit var adapterViewPager: viewPager2Adapter

    private val viewModel: HomeViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapterHomeTeas = AdapterHomeTeas(object : TeaActionListener {
            override fun onTeaDetails(tea: Tea) {
                navigator().showProductPage(tea)
            }

            override fun onMoreAction(tea: Tea) {
//                viewModel.deleteTea(tea)
                viewModel.moveTea(tea)
                Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
            }
        })
        Log.d(TAG, "onCreateView: ")

        viewModel.teas.observe(viewLifecycleOwner, Observer { adapterHomeTeas.teas = it })
//        viewModel.teas.observe(viewLifecycleOwner, Observer { adapter.teas = it })


        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewPopular.layoutManager = layoutManager
        binding.recyclerViewPopular.adapter = adapterHomeTeas



        binding.recyclerViewNovelty.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            true
        )
        binding.recyclerViewNovelty.adapter = adapterHomeTeas



        //viewPager
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.view_pager_one)
        imageList.add(R.drawable.view_pager_two)
        imageList.add(R.drawable.view_pager_three)

        adapterViewPager = viewPager2Adapter(imageList, binding.viewPager2)

        binding.viewPager2.adapter = adapterViewPager
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        setUpTransformer()

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })

        return binding.root
    }

    private val runnable = Runnable {
        binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.8f + r * 0.14f
        }
        binding.viewPager2.setPageTransformer(transformer)
    }
}