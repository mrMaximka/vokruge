package ru.gb.vokruge.ui.category

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_category.view.*
import ru.gb.vokruge.model.models.CompanyCategory
import ru.gb.vokruge.ui.utils.UrlCategory.Companion.getCatImageUrl

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var model: CompanyCategory
    private var clickListener: CategoryAdapter.ClickListener? = null

    init {
        itemView.setOnClickListener { clickListener?.onItemClick(model.id) }
    }

    fun bind(model: CompanyCategory, listener: CategoryAdapter.ClickListener) {
        this.model = model
        this.clickListener = listener
        itemView.item_category_name.text = model.nameCategory
        Glide.with(itemView).load(getCatImageUrl(model.id)).into(itemView.item_category_image)
    }
}