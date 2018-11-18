package com.sugar.steptofood.paging

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.utils.LoadState

class RecipePagedListWithLoadState(val value: LiveData<PagedList<Recipe>>,
                                   val initialLoadState: LiveData<LoadState>,
                                   val additionalLoadState: LiveData<LoadState>)