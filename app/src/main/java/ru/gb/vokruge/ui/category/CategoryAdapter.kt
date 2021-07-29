package ru.gb.vokruge.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.vokruge.R
import ru.gb.vokruge.model.models.CompanyCategory

class CategoryAdapter(private var clickListener: ClickListener) :
    RecyclerView.Adapter<CategoryViewHolder>() {

    private var categories: List<CompanyCategory> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], clickListener)
    }

    fun setElements(categories: List<CompanyCategory>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }
}