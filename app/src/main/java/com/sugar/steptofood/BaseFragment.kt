package com.sugar.steptofood

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {
    protected var inflater: LayoutInflater? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        return inflater.inflate(getLayout(), null)
    }

    @LayoutRes
    abstract fun getLayout(): Int
}
