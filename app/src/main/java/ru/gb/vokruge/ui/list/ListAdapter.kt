package ru.gb.vokruge.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import ru.gb.vokruge.R
import ru.gb.vokruge.model.models.CompanyInfo

class ListAdapter(
    private val clickListener: ClickListener
) : RecyclerView.Adapter<ListViewHolder>() {

    private lateinit var context: Context
    private var companies: List<CompanyInfo>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        this.context = parent.context
        var view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_companies_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return companies?.size ?: 0
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        companies?.let {
            val listViewHolder: ListViewHolder = holder
            listViewHolder.bind(it[position], clickListener)
            val animation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down)
            listViewHolder.container.startAnimation(animation)
        }
    }

    fun setCompanies(companies: List<CompanyInfo>?) {
        this.companies = companies
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(company: CompanyInfo)
    }

}