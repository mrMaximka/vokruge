package ru.gb.vokruge.ui.subcategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.vokruge.R
import ru.gb.vokruge.model.models.CompanyCategory

class SubcategoryAdapter(private var clickListener: ClickListener) :
    RecyclerView.Adapter<SubcategoryViewHolder>() {

    private var subcategories = emptyList<CompanyCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.subcategory_item, parent, false)
        return SubcategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subcategories.size
    }

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        holder.bind(subcategories[position], clickListener)
    }

    fun setElements(subcategories: List<CompanyCategory>) {
        this.subcategories = subcategories
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(idSubcategory: Int)
    }

}
