package com.example.teaqualitykotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teaqualitykotlin.R
import com.example.teaqualitykotlin.Tea
import com.example.teaqualitykotlin.databinding.ItemTeaHomeBinding

interface TeaActionListener {

    fun onTeaDetails(tea: Tea)

    fun onMoreAction(tea: Tea)
}


class AdapterHomeTeas(
    private val actionListener: TeaActionListener
) : RecyclerView.Adapter<AdapterHomeTeas.HomeTeasViewHolder>(), View.OnClickListener {

    var teas:List<Tea> = emptyList()
        set(newValue){
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val tea = v.tag as Tea
        when (v.id) {
            R.id.favoriteImageViewButton -> {
                actionListener.onMoreAction(tea)
            }
            else -> {
                actionListener.onTeaDetails(tea)
            }
        }
    }

//    private fun checkBoolean(view: View) {
//        val tea = view.tag as Tea
//        val isChecked = false
//        Toast.makeText(view.context, "add to favorite", Toast.LENGTH_SHORT).show()
//    }

    //Возвращает кол-во элементов в списке
    override fun getItemCount(): Int = teas.size

    //Используется когда создаётся новый элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTeasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTeaHomeBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.favoriteImageViewButton.setOnClickListener(this)

        return HomeTeasViewHolder(binding)
    }

    override fun onBindViewHolder(holderHome: HomeTeasViewHolder, position: Int) {
        val tea = teas[position]
        with(holderHome.binding) {
            holderHome.itemView.tag = tea
            favoriteImageViewButton.tag = tea
            teaNameTextView.text = tea.productName
            teaPriceTextView.text = tea.productPrice
            teaImageView.setImageResource(tea.productImage)
//            teaImageView.setImageResource(R.drawable.tea)
        }
    }

    class HomeTeasViewHolder(
        val binding: ItemTeaHomeBinding
    ) : RecyclerView.ViewHolder(binding.root)

}
