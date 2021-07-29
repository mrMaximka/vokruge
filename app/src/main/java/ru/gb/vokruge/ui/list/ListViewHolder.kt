package ru.gb.vokruge.ui.list

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_companies_list.view.*
import ru.gb.vokruge.model.models.CompanyInfo

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var model: CompanyInfo
    private var clickListener: ListAdapter.ClickListener? = null
    val container: ConstraintLayout = itemView.containerCompanyList

    init {
        itemView.setOnClickListener { clickListener?.onItemClick(model) }
    }

    fun bind(model: CompanyInfo, clickListener: ListAdapter.ClickListener) {
        this.model = model
        this.clickListener = clickListener
        itemView.tvItemCompanyName.text = model.name_company
        itemView.tvItemCompanyDescription.text = model.categoryString
        itemView.tvItemCompanyAddress.text = model.address
    }
}