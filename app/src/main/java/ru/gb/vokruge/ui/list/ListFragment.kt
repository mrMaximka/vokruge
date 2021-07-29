package ru.gb.vokruge.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.list_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.model.models.CompanyInfo


class ListFragment : Fragment(), ListAdapter.ClickListener {

    private lateinit var viewModel: ListViewModel
    private var listAdapter: ListAdapter? = null

    val needShowDetail = MutableLiveData<Boolean>().apply { value = false }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCompanyList.layoutManager = LinearLayoutManager(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        viewModel.needShowProgress.observe(this, Observer {
            it?.apply {
                if (it) {
                    pbLoadingCompanies.visibility = View.VISIBLE
                } else {
                    pbLoadingCompanies.visibility = View.GONE
                }
            }
        })

        viewModel.getCurrentCompanies().observe(this, Observer {
            listAdapter?.setCompanies(it)
        })

        listAdapter = ListAdapter(this)
        rvCompanyList.adapter = listAdapter

        viewModel.createList()
    }

    override fun onItemClick(company: CompanyInfo) {   // Вызывается при клике по элементу
        viewModel.setCurrentCompany(company)
        needShowDetail.value = true
    }
}
