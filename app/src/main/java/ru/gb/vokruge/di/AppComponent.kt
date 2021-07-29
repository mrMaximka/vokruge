package ru.gb.vokruge.di

import dagger.Component
import ru.gb.vokruge.ui.activity.OwnerViewModel
import ru.gb.vokruge.ui.category.CategoryViewModel
import ru.gb.vokruge.ui.detail.DetailViewModel
import ru.gb.vokruge.ui.detailshort.ShortDetailViewModel
import ru.gb.vokruge.ui.list.ListViewModel
import ru.gb.vokruge.ui.map.MapViewModel
import ru.gb.vokruge.ui.search.SearchViewModel
import ru.gb.vokruge.ui.subcategory.SubcategoryViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, DbModule::class])
interface AppComponent {

    fun inject(viewModel: ListViewModel)

    fun inject(viewModel: SubcategoryViewModel)

    fun inject(viewModel: CategoryViewModel)

    fun inject(viewModel: ShortDetailViewModel)

    fun inject(viewModel: DetailViewModel)

    fun inject(viewModel: SearchViewModel)

    fun inject(viewModel: MapViewModel)

    fun inject(viewModel: OwnerViewModel)

}
