package com.chad.library.adapter4

import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import java.util.*

/**
 * Deprecated.
 *
 * Base differ adapter.
 *
 * 使用 Differ 的父类。异步执行 Diff 计算，不会有性能问题。
 *
 * @param T 数据类型
 * @param VH ViewHolder 类型
 */
@Deprecated("请直接使用 BaseQuickAdapter。Please use BaseQuickAdapter directly.")
abstract class BaseDifferAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    config: AsyncDifferConfig<T>, items: List<T>
) : BaseQuickAdapter<T, VH>(items, config) {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : this(diffCallback, emptyList())

    constructor(diffCallback: DiffUtil.ItemCallback<T>, items: List<T>) : this(
        AsyncDifferConfig.Builder(diffCallback).build(), items
    )

    constructor(config: AsyncDifferConfig<T>) : this(config, emptyList())
}