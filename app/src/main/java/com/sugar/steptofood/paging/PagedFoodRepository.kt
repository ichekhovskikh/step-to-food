package com.sugar.steptofood.paging

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.paging.factory.BaseRecipeFactory
import com.sugar.steptofood.paging.source.BaseRecipeSource

class PagedFoodRepository(private var sourceFactory: BaseRecipeFactory) {
    private val config: PagedList.Config
    private val dataSource: LiveData<BaseRecipeSource>
    private var pagedList: LiveData<PagedList<Food>>
    private val observers = HashMap<LifecycleOwner, MutableSet<Observer<PagedList<Food>>>>()

    init {
        config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build()

        dataSource = sourceFactory.currentDataSource
        pagedList = buildPagedList()
    }

    fun observe(owner: LifecycleOwner, observer: Observer<PagedList<Food>>) {
        if (!observerAlreadyAdded(owner, observer)){
            if(!observers.containsKey(owner))
                observers[owner] = mutableSetOf()
            observers[owner]?.add(observer)
        }
        pagedList.observe(owner, observer)
    }

    fun removeObservers(owner: LifecycleOwner) {
        if (observers.containsKey(owner)) {
            pagedList.removeObservers(owner)
            observers.remove(owner)
        }
    }

    fun refreshData(sourceFactory: BaseRecipeFactory) {
        this.sourceFactory = sourceFactory
        refreshPagedList()
    }

    fun removeItem(foodId: Int) = dataSource.value?.removeItem(foodId) {
        refreshPagedList()
    }

    private fun refreshPagedList() {
        pagedList.value?.dataSource?.invalidate()
        observers.keys.forEach { owner ->
            pagedList.removeObservers(owner)
        }
        pagedList =  buildPagedList()

        observers.keys.forEach { owner ->
            observers[owner]?.forEach { observer ->
                observe(owner, observer)
            }
        }
    }

    private fun buildPagedList() = LivePagedListBuilder(sourceFactory, config)
            .setBoundaryCallback(sourceFactory.getNetworkSwapCallback())
            .build()

    private fun observerAlreadyAdded(owner: LifecycleOwner, observer: Observer<PagedList<Food>>) =
            observers.containsKey(owner) && (!observers.containsKey(owner) || observers[owner]!!.contains(observer))

    companion object {
        const val PAGE_SIZE = 10
    }
}