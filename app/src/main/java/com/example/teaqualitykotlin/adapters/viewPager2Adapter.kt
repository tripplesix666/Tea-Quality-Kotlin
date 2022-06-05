package com.example.teaqualitykotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.teaqualitykotlin.databinding.ViewPagerContainerBinding
import java.util.ArrayList

class viewPager2Adapter(
    private val imageList : ArrayList<Int>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<viewPager2Adapter.ImageViewHolder>() {

    class ImageViewHolder(
        val binding: ViewPagerContainerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewPagerContainerBinding.inflate(inflater, parent, false)

        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        with(holder.binding) {
            imageView.setImageResource(imageList[position])
            if (position == imageList.size - 1) {
                viewPager2.post(runnable)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}