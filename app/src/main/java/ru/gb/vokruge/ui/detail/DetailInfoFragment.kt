package ru.gb.vokruge.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.detail_info_fragment.*
import ru.gb.vokruge.R

class DetailInfoFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startLoading()

        viewModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java)

        viewModel.getInfo().observe(this, Observer {
            if (viewModel.needShowInfoFragment.value!!){
                showInfo()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_info_fragment, container, false)
    }

    private fun showInfo() {
        val company = viewModel.getCompanyInfo()
        detail_info_address.text = company!!.address
        if (company.email!!.isNotEmpty()){
            detail_info_email.text = company.email
        }
        if (company.siteLink!!.isNotEmpty()){
            detail_info_site.text = company.siteLink
        }
        fillPhoneNumbers(company.phoneNumbers)

        stopLoading()
    }

    private fun fillPhoneNumbers(phoneNumbers: List<String?>?) {
        if (phoneNumbers!!.isNotEmpty()){
            var text = ""
            for (i in phoneNumbers.indices){
                text += "${phoneNumbers[i]}"
                if (i != phoneNumbers.size-1){
                    text += "\n"
                }
            }
            detail_info_phone.text = text
        }
    }


    private fun startLoading(){
        detail_info_progress_bar.visibility = View.VISIBLE
        detail_info_address_container.visibility = View.GONE
        detail_info_schedule_container.visibility = View.GONE
        detail_info_contacts_container.visibility = View.GONE
        detail_info_social_container.visibility = View.GONE
        detail_info_stocks_container.visibility = View.GONE
        detail_info_photos_container.visibility = View.GONE
        detail_info_error_btn.visibility = View.GONE
    }

    private fun stopLoading(){
        detail_info_progress_bar.visibility = View.GONE
        detail_info_address_container.visibility = View.VISIBLE
        detail_info_schedule_container.visibility = View.VISIBLE
        detail_info_contacts_container.visibility = View.VISIBLE
        detail_info_social_container.visibility = View.VISIBLE
        detail_info_stocks_container.visibility = View.VISIBLE
        detail_info_photos_container.visibility = View.VISIBLE
        detail_info_error_btn.visibility = View.VISIBLE
    }
}