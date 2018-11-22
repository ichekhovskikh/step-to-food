package com.sugar.steptofood.paging

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.sugar.steptofood.utils.NetworkState

data class Listing<T>(val pagedList: LiveData<PagedList<T>>,
                      val initialLoadState: LiveData<NetworkState>,
                      val additionalLoadState: LiveData<NetworkState>,
                      val refresh: () -> Unit = {})