package com.chinakingo.brvahktx

import android.support.v7.util.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * author : limuyang
 * e-mail : limuyang2@163.com
 * date   : 2019/07/28
 * desc   : About the kotlin extension of the adapter
 */

/**
 * this is kotlin Expansion, quick use async diff
 * @receiver BaseQuickAdapter<T, K>
 * @param baseQuickDiffCallback BaseQuickDiffCallback<T>
 * @param detectMoves Boolean
 */
suspend inline fun <T, K : BaseViewHolder> BaseQuickAdapter<T, K>.setNewDiffDataAsync(baseQuickDiffCallback: BaseQuickDiffCallback<T>, detectMoves: Boolean) {
    baseQuickDiffCallback.oldList = this.data
    val diffResult = withContext(Dispatchers.IO) {
        DiffUtil.calculateDiff(baseQuickDiffCallback, detectMoves)
    }
    this.setNewDiffData(diffResult, baseQuickDiffCallback.newList)
}