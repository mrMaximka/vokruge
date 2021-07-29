package ru.gb.vokruge.ui.categoryshort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.short_category_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.databinding.ShortCategoryFragmentBinding
import ru.gb.vokruge.ui.subcategory.SubcategoryFragmentArgs
import ru.gb.vokruge.ui.utils.NavigationUtils
import ru.gb.vokruge.ui.utils.UrlCategory.Companion.getCatImageUrl

class ShortCategoryFragment : Fragment() {

    private lateinit var viewModel: ShortCategoryViewModel
    private lateinit var binding: ShortCategoryFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.short_category_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShortCategoryViewModel::class.java)
        binding.model = viewModel

        viewModel.needShowAllCategory.observe(this, Observer {
            NavigationUtils.observerNavigation(                     // Пока просто открывает фрагмент
                view,
                it,
                viewModel.needShowAllCategory,
                R.id.action_mapFragment_to_categoryFragment
            )
        })

        viewModel.needShowSubCategory.observe(this, Observer {
            it?.let { value ->
                if (value > 0) {
                    view?.let { view ->
                        Navigation.findNavController(view).navigate(
                            R.id.action_mapFragment_to_subcategoryFragment,
                            SubcategoryFragmentArgs(value).toBundle()
                        )
                    }
                    viewModel.needShowSubCategory.value = 0
                }
            }
        })

        Glide.with(this).load(getCatImageUrl(24)).into(ivCat24)
        Glide.with(this).load(getCatImageUrl(7)).into(ivCat7)
        Glide.with(this).load(getCatImageUrl(3)).into(ivCat3)
    }

}
