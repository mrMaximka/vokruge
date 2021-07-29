package ru.gb.vokruge.ui.search

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.search_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.databinding.SearchFragmentBinding
import ru.gb.vokruge.ui.utils.NavigationUtils

class SearchFragment : Fragment(), SearchAdapter.ClickListener {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding

    private lateinit var adapter: SearchAdapter         // Адаптер
    private var isNullTestRequest: Boolean = true               // Пустое поле. Для крестика

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        binding.model = viewModel

        closeAppBar()                                   // Закрывам тулбар

        viewModel.needShowSearch.observe(this, Observer {
            // Будет обработка клика
            NavigationUtils.observerNavigation(
                view,
                it,
                viewModel.needShowSearch,
                R.id.action_searchFragment_to_mapFragment
            )
        })

        viewModel.suitableStrings.observe(this, Observer {
            adapter.setElements(it)
        })

        viewModel.needShowHistoryList.observe(this, Observer {
            it?.let {
                if (it) {
                    btnClearHistory.visibility = View.VISIBLE
                    search_list_container.visibility = View.VISIBLE
                    ivEmptyHistory.visibility = View.GONE
                    tvEmptyHistory.visibility = View.GONE
                } else {
                    btnClearHistory.visibility = View.GONE
                    search_list_container.visibility = View.GONE
                    ivEmptyHistory.visibility = View.VISIBLE
                    tvEmptyHistory.visibility = View.VISIBLE
                }
            }
        })

        createList()                                    // Создаем список
        initSearchView()                                // Загрузка строки поиска

        viewModel.refreshSuitableStrings("")
    }

    override fun onItemClick(str: String) {
        hideKeyboard()                                  // Убираем клавиатуру
        viewModel.onClickShow(str)                         // Говорим, что по истории кликнули
    }

    private fun closeAppBar() {                         // Прячем тулбар
        (activity as AppCompatActivity).apply {
            supportActionBar?.hide()
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            )
        }
    }

    private fun createList() {                          // Создание списка
        adapter = SearchAdapter(this)
        search_list.adapter = adapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchView() {                       // Инициализация строки поиска
        search_back_btn.setOnTouchListener { p0, p1 ->
            // Слушатель на кнопку назад
            hideKeyboard()
            activity?.onBackPressed()
            false
        }

        search_clear_btn.setOnTouchListener { p0, p1 ->
            // На крестик
            hideKeyboard()
            clearEditText()
            false
        }

        search_edit_text.setOnKeyListener { p0, p1, p2 ->
            // На ввод запроса
            if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER) {
                val request: String = search_edit_text.text.toString()
                onItemClick(request)
            }
            false
        }

        search_edit_text.addTextChangedListener(object : TextWatcher { // На изменение текста
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isNullTestRequest && p0!!.isNotEmpty()) {
                    showClearBtn()                  // Показываем крестик, если есть буквы
                } else if (!isNullTestRequest && p0!!.isEmpty()) {
                    hideClearBtn()                 // Убираем его при пустом поле
                }
                viewModel.refreshSuitableStrings(p0.toString())
            }

        })
    }

    private fun hideKeyboard() {                                 // Свернуть клавиатуру
        val imm: InputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE)
                as InputMethodManager
        val view: View? = activity!!.currentFocus
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun clearEditText() {
        search_edit_text.text?.clear()
    }

    private fun showClearBtn() {
        isNullTestRequest = false
        search_clear_btn.visibility = View.VISIBLE
    }

    private fun hideClearBtn() {
        isNullTestRequest = true
        search_clear_btn.visibility = View.GONE
    }

}
