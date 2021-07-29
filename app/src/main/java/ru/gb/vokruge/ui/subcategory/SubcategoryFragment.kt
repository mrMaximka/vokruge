package ru.gb.vokruge.ui.subcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.subcategory_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.databinding.SubcategoryFragmentBinding

class SubcategoryFragment : Fragment(), SubcategoryAdapter.ClickListener {

    private lateinit var viewModel: SubcategoryViewModel
    private lateinit var binding: SubcategoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.subcategory_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showToolbar()

        viewModel = ViewModelProviders.of(this).get(SubcategoryViewModel::class.java)
        binding.model = viewModel

        val safeArguments: SubcategoryFragmentArgs by navArgs()
        viewModel.loadSubcategories(safeArguments.idCategory)

        viewModel.subcategories.observe(this, Observer {
            (rvSubcategories.adapter as SubcategoryAdapter).setElements(it)
        })

        viewModel.needShowCompaniesByCategory.observe(this, Observer {
            it?.let { value ->
                if (value) {
                    view?.let { view ->
                        Navigation.findNavController(view).navigate(
                            R.id.action_subcategoryFragment_to_mapFragment
                        )
                    }
                    viewModel.needShowCompaniesByCategory.value = false
                }
            }
        })

        rvSubcategories.layoutManager = LinearLayoutManager(context)
        rvSubcategories.adapter = SubcategoryAdapter(this)
    }

    override fun onItemClick(idSubcategory: Int) {
        viewModel.onSubcategoryClick(idSubcategory)
    }

    private fun showToolbar() {
        (activity as AppCompatActivity).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            //изменяем иконку "бокового меню" на "стрелку назад"
            toolbar.setNavigationIcon(R.drawable.ic_act_back)
            supportActionBar?.show()

            //обработка нажатия на иконку
            toolbar.setNavigationOnClickListener { onBackPressed() }

            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }
}
