package ru.gb.vokruge.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.category_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.ui.subcategory.SubcategoryFragmentArgs
import ru.gb.vokruge.ui.utils.NavigationUtils

class CategoryFragment : Fragment(), CategoryAdapter.ClickListener {

    companion object {
        private val SPAN_COUNT: Int = 4                      // Кол-во столбцов
    }

    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showToolbar()                                   // Показываем тулбар
        initRecycler()                                  // Создаем список
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)

        viewModel.needShowSubcategories.observe(this, Observer {
            NavigationUtils.observerNavigation(
                view,
                it,
                viewModel.needShowSubcategories,
                R.id.action_categoryFragment_to_subcategoryFragment,
                SubcategoryFragmentArgs(viewModel.clickPosition).toBundle()
            )
        })

        viewModel.categories.observe(this, Observer {
            (category_list.adapter!! as CategoryAdapter).setElements(it)
        })

        viewModel.showElements()                 // Заполняем список
    }

    override fun onItemClick(position: Int) {
        viewModel.clickPosition = position              // Записали позицию клика
        viewModel.onCategoryItemClick()                 // Отметили событие клика
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

    private fun initRecycler() {
        val layoutManager = GridLayoutManager(context, SPAN_COUNT)
        category_list.layoutManager = layoutManager
        category_list.adapter = CategoryAdapter(this)
    }
}
