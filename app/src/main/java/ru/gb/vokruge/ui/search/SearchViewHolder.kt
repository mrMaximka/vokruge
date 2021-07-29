package ru.gb.vokruge.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_search.view.*

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var clickListener: SearchAdapter.ClickListener
    private lateinit var string: String

    init {
        itemView.setOnClickListener { clickListener.onItemClick(string) }
    }

    fun bind(string: String, clickListener: SearchAdapter.ClickListener) {
        this.clickListener = clickListener
        this.string = string
        itemView.history_text.text = string
    }
}