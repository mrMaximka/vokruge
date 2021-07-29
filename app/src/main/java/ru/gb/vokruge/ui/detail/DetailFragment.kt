package ru.gb.vokruge.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.detail_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.ui.utils.NavigationUtils


class DetailFragment : Fragment() {

    companion object {
        private val BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT: Int = 1
    }

    private lateinit var viewModel: DetailViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startLoading()

        viewModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java)

        viewModel.needShowMap.observe(this, Observer {
            NavigationUtils.observerNavigation(
                view,
                it,
                viewModel.needShowMap,
                R.id.action_detailFragment_to_mapFragment
            )
        })

        viewModel.needSetInfo.observe(this, Observer {
            if (viewModel.needSetInfo.value!!) {
                setInformation()
            }
        })

        arguments?.getInt("position")?.let { viewModel.loadCompany(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    private fun initTabLayout() {
        setupViewPager(detail_viewpager)
        detail_tab_layout.setupWithViewPager(detail_viewpager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(
            activity?.supportFragmentManager!!,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        adapter.addFragment(DetailInfoFragment(), getString(R.string.info_tab))
        adapter.addFragment(DetailReviewFragment(), getString(R.string.review_tab))
        adapter.addFragment(DetailMoreFragment(), getString(R.string.more_tab))
        viewPager.adapter = adapter
    }

    private fun setInformation() {
        viewModel.getCompanyInfo()?.let {
            detail_company_name.text = it.name_company
            detail_company_category.text = it.categoryString
        }
        stopLoading()

        viewModel.fillInfoFragment()
    }

    private fun startLoading() {
        detail_fragment_container.visibility = View.GONE
        detail_progress_bar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        detail_fragment_container.visibility = View.VISIBLE
        detail_progress_bar.visibility = View.GONE
    }

}
