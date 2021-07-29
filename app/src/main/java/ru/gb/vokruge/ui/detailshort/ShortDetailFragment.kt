package ru.gb.vokruge.ui.detailshort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.short_detail_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.databinding.ShortDetailFragmentBinding
import ru.gb.vokruge.model.models.CompanyInfo
import ru.gb.vokruge.ui.detail.DetailFragmentArgs
import timber.log.Timber

class ShortDetailFragment : Fragment() {

    private lateinit var viewModel: ShortDetailViewModel
    private lateinit var binding: ShortDetailFragmentBinding

    val company = MutableLiveData<CompanyInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.short_detail_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShortDetailViewModel::class.java)
        binding.model = viewModel

        viewModel.needShowFullDetail.observe(this, Observer {
            it?.let {
                if (it) {
                    view?.let { view ->
                        try {
                            viewModel.needShowFullDetail.value = false
                            viewModel.currentCompany().value?.let {
                                Navigation.findNavController(view).navigate(
                                    R.id.action_mapFragment_to_detailFragment,
                                    DetailFragmentArgs(it.id).toBundle()
                                )
                            }
                        } catch (e: Exception) {
                            Timber.e(e)
                        }

                    }
                }
            }
        })

        viewModel.currentCompany().observe(this, Observer {
            if (it != null) {
                tvShortCompanyName.text = it.name_company
                tvShortAddress.text = it.address
                tvShortCategory.text = it.categoryString
                tvShortDistance.text = "-"
            } else {
                tvShortCompanyName.text = ""
                tvShortAddress.text = ""
                tvShortCategory.text = ""
                tvShortDistance.text = ""
            }
        })

    }

}
