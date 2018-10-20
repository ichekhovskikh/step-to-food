package com.sugar.steptofood.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

abstract class BaseRecyclerAdapter<V, VH : RecyclerView.ViewHolder>(protected var context: Context) : RecyclerView.Adapter<VH>() {

    open val items: ArrayList<V> = arrayListOf()

    protected var inflater = LayoutInflater.from(context)

    open fun add(item: V) {
        add(item, items.size)
    }

    fun add(item: V, pos: Int) {
        items.add(pos, item)
        notifyItemInserted(pos)
    }

    @JvmOverloads fun addAll(items: List<V>, pos: Int = this.items.size) {
        val size = items.size
        this.items.addAll(pos, items)
        notifyItemRangeInserted(pos, size)
    }

    fun remove(obj: V): Int {
        for (i in items.indices) {
            if (obj == items[i]) {
                removeAt(i)
                return i
            }
        }
        return -1
    }

    fun removeAt(pos: Int): V {
        val removed = items.removeAt(pos)
        notifyItemRemoved(pos)
        return removed
    }

    fun update(pos: Int, item: V): V {
        val oldItem = items[pos]
        items[pos] = item
        notifyItemChanged(pos)
        return oldItem
    }

    override fun getItemCount() = items.size

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun isEmpty() = items.isEmpty()
}