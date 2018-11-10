package com.sugar.steptofood.paging

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.paging.factory.BaseRecipeFactory

class PagedRecipeRepository(private var sourceFactory: BaseRecipeFactory) {
    private val config: PagedList.Config
    private var pagedList: LiveData<PagedList<Recipe>>
    private val observers = HashMap<LifecycleOwner, MutableSet<Observer<PagedList<Recipe>>>>()

    init {
        config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build()
        pagedList = buildPagedList()
    }

    fun observe(owner: LifecycleOwner, observer: Observer<PagedList<Recipe>>) {
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

    fun refreshData(sourceFactory: BaseRecipeFactory = this.sourceFactory) {
        this.sourceFactory = sourceFactory
        refreshPagedList()
    }

    private fun refreshPagedList() {
        observers.keys.forEach { owner ->
            pagedList.removeObservers(owner)
        }
        pagedList = buildPagedList()

        observers.keys.forEach { owner ->
            observers[owner]?.forEach { observer ->
                observe(owner, observer)
            }
        }
        sourceFactory.currentDataSource.value?.invalidate()
    }

    private fun buildPagedList() = LivePagedListBuilder(sourceFactory, config)
            .setBoundaryCallback(sourceFactory.getNetworkSwapCallback())
            .build()

    private fun observerAlreadyAdded(owner: LifecycleOwner, observer: Observer<PagedList<Recipe>>) =
            observers.containsKey(owner) && (!observers.containsKey(owner) || observers[owner]!!.contains(observer))

    companion object {
        const val PAGE_SIZE = 10
    }
}