package com.sugar.steptofood.paging.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.sugar.steptofood.R
import com.sugar.steptofood.utils.NetworkState
import com.sugar.steptofood.utils.Status

class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

    fun bindTo(networkState: NetworkState?) {
        progressBar.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
    }
}