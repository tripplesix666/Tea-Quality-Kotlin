package com.example.teaqualitykotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teaqualitykotlin.databinding.ItemTeaCartBinding
import com.squareup.picasso.Picasso

interface TeaCartActionListener {

    fun onTeaDetails(tea: Tea)

    fun onTeaDelete(tea: Tea)

    fun onTeaCountPlus(tea: Tea)

    fun onTeaCountMinus(tea: Tea)
}

class AdapterCartTeas(
    private val actionListener: TeaCartActionListener
) : RecyclerView.Adapter<AdapterCartTeas.CartTeasViewHolder>(), View.OnClickListener {

    var teasCart: List<Tea> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    override fun onClick(v: View) {
        val tea = v.tag as Tea
        when (v.id) {
            R.id.plusImageViewButton -> {
                actionListener.onTeaCountPlus(tea)
            }
            R.id.minusImageViewButton -> {
                actionListener.onTeaCountMinus(tea)
            }
            R.id.closeImageViewButton -> {
                actionListener.onTeaDelete(tea)
            }
            else -> {
                actionListener.onTeaDetails(tea)
            }
        }
    }
    override fun getItemCount(): Int = teasCart.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartTeasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTeaCartBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.closeImageViewButton.setOnClickListener(this)
        binding.minusImageViewButton.setOnClickListener(this)
        binding.plusImageViewButton.setOnClickListener(this)

        return CartTeasViewHolder(binding)
    }

    override fun onBindViewHolder(holderHome: CartTeasViewHolder, position: Int) {
        val tea = teasCart[position]
        with(holderHome.binding) {
            holderHome.itemView.tag = tea
            closeImageViewButton.tag = tea
            plusImageViewButton.tag = tea
            minusImageViewButton.tag = tea
            teaNameTextView.text = tea.name
            teaPriceTextView.text = tea.price
            Picasso.get()
                .load(tea.image)
                .into(teaImageView)
//            teaImageView.setImageResource(tea.image)
        }
    }

    class CartTeasViewHolder(
        val binding: ItemTeaCartBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
