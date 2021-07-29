package ru.gb.vokruge.ui.subcategory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.subcategory_item.view.*
import ru.gb.vokruge.model.models.CompanyCategory

class SubcategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var model: CompanyCategory
    private lateinit var clickListener: SubcategoryAdapter.ClickListener

    init {
        itemView.setOnClickListener { clickListener.onItemClick(model.id) }
    }

    fun bind(model: CompanyCategory, clickListener: SubcategoryAdapter.ClickListener) {
        this.model = model
        this.clickListener = clickListener
        itemView.tvSubcategoryName.text = model.nameCategory
    }
}
