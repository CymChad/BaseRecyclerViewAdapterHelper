package com.chad.library.adapter.base

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * 请勿使用，暂未定版
 *
 * @constructor
 *
 * @param config
 */
class QuickNodeHelper private constructor(config: ConcatAdapter.Config) {

    private val concatAdapter = ConcatAdapter(config)

    private val clazzMap = HashMap<Class<*>, OnNodeListener<Any>>()

    val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder> get() = concatAdapter

    fun submitList(list: List<*>, isExpand: Boolean = false) {
        concatAdapter.adapters.forEach {
            concatAdapter.removeAdapter(it)
        }

        if (isExpand) {
            for (any in list) {
                if (any == null) continue

                clazzMap[any::class.java]?.let {
                    concatAdapter.addAdapter(it.createAdapter(any))

                    val childList = it.nodeChildren(any)

                    if (!childList.isNullOrEmpty()) {
                        submitList(childList, true)
                    }
                }
            }
        } else {
            for (any in list) {
                if (any == null) continue

                clazzMap[any::class.java]?.let {
                    concatAdapter.addAdapter(it.createAdapter(any))
                }
            }
        }
    }


    fun expand(item: Any, itemAdapter: RecyclerView.Adapter<*>) {
        val index = concatAdapter.adapters.indexOfFirst { it == itemAdapter }
        if (index == -1) return

        val parentListener = clazzMap[item::class.java] ?: return

        val startIndex = index + 1
        parentListener.nodeChildren(item)?.forEachIndexed { i, any ->
            if (any != null){
                clazzMap[any::class.java]?.let {
                    concatAdapter.addAdapter(startIndex + i, it.createAdapter(any))
                }
            }
        }
    }

    fun collapse(parentAdapter: RecyclerView.Adapter<*>) {
        val adapters = concatAdapter.adapters
        val index = adapters.indexOfFirst { it == parentAdapter }

        if (index == -1) return

        val adapter = adapters.getOrNull(index + 1) ?: return

        concatAdapter.removeAdapter(adapter)
    }

    inline fun <reified T> register(adapter: OnNodeListener<T>) =
        register(T::class.java, adapter)


    fun <T> register(clazz: Class<T>, adapter: OnNodeListener<T>) = apply {
        clazzMap[clazz] = adapter as OnNodeListener<Any>
    }

    fun unregister(clazz: Class<*>) = apply {
        clazzMap.remove(clazz)
    }

    interface OnNodeListener<T> {
        fun createAdapter(item: T): RecyclerView.Adapter<*>

        fun nodeChildren(item: T): List<*>?
    }

    class Builder {

        private var config: ConcatAdapter.Config = ConcatAdapter.Config.DEFAULT

        fun setConfig(config: ConcatAdapter.Config) = apply {
            this.config = config
        }

        fun build(): QuickNodeHelper {
            return QuickNodeHelper(config)
        }
    }
}