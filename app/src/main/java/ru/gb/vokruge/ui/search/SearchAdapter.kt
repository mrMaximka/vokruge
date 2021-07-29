package ru.gb.vokruge.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.vokruge.R

class SearchAdapter(private var clickListener: ClickListener) :
    RecyclerView.Adapter<SearchViewHolder>() {

    private var history: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(history[position], clickListener)
    }

    fun setElements(data: List<String>) {  // text - фильтр списка
        history = data
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(str: String)
    }
}