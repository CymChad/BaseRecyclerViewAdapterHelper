package com.chad.library.adapter4

import android.animation.Animator
import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.animation.AlphaInAnimation
import com.chad.library.adapter4.animation.ItemAnimator
import com.chad.library.adapter4.animation.ScaleInAnimation
import com.chad.library.adapter4.animation.SlideInBottomAnimation
import com.chad.library.adapter4.animation.SlideInLeftAnimation
import com.chad.library.adapter4.animation.SlideInRightAnimation
import com.chad.library.adapter4.util.asStaggeredGridFullSpan
import com.chad.library.adapter4.viewholder.StateLayoutVH
import java.util.Collections

/**
 * Base Class
 * @param T : type of data, 数据类型
 * @param VH : BaseViewHolder
 * @constructor layoutId, data(Can null parameters, the default is empty data)
 */
abstract class BaseQuickAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private var _items: List<T> = emptyList(),
    config: AsyncDifferConfig<T>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : this(emptyList(), diffCallback)

    constructor(items: List<T>, diffCallback: DiffUtil.ItemCallback<T>) : this(
        items, AsyncDifferConfig.Builder(diffCallback).build(),
    )

    constructor(config: AsyncDifferConfig<T>) : this(emptyList(), config)

    /********************************* Private property *****************************************/
    private var mLastPosition = -1
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null
    private var mOnItemChildClickArray: SparseArray<OnItemChildClickListener<T>>? = null
    private var mOnItemChildLongClickArray: SparseArray<OnItemChildLongClickListener<T>>? = null
    private var mOnViewAttachStateChangeListeners: MutableList<OnViewAttachStateChangeListener>? =
        null

    private val mDiffer: AsyncListDiffer<T>? =
        if (config == null)
            null
        else
            AsyncListDiffer(AdapterListUpdateCallback(this), config)


    private var _recyclerView: RecyclerView? = null

    /**
     * Items data
     */
    var items: List<T>
        get() {
            return mDiffer?.currentList ?: _items
        }
        set(value) {
            if (mDiffer != null) {
                mDiffer.submitList(value, null)
            } else {
                val oldItems = _items
                _items = value
                onCurrentListChanged(oldItems, _items)
            }
        }

    /**
     * 绑定此 Adapter 的 RecyclerView
     */
    val recyclerView: RecyclerView
        get() {
            checkNotNull(_recyclerView) {
                "Please get it after onAttachedToRecyclerView()"
            }
            return _recyclerView!!
        }

    val context: Context
        get() {
            return recyclerView.context
        }


    /**
     * Function to judge if the viewHolder is EmptyLayoutVH.
     * 判断 ViewHolder 是否是 [StateLayoutVH]
     *
     * @receiver RecyclerView.ViewHolder
     * @return Boolean
     */
    inline val RecyclerView.ViewHolder.isEmptyViewHolder: Boolean
        get() = this is StateLayoutVH

    /**
     *  Whether to use empty layout.
     *  是否使用空布局。
     * */
    @Deprecated("使用 isStateViewEnable", ReplaceWith("isStateViewEnable"))
    var isEmptyViewEnable
        set(value) {
            isStateViewEnable = value
        }
        get() = isStateViewEnable

    /**
     *  Whether to use state layout.
     *  是否使用状态布局。
     * */
    var isStateViewEnable = false
        set(value) {
            val oldDisplayEmptyLayout = displayEmptyView()

            field = value

            val newDisplayEmptyLayout = displayEmptyView()

            if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
                notifyItemRemoved(0)
            } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
                notifyItemInserted(0)
            } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
                notifyItemChanged(0, EMPTY_PAYLOAD)
            }
        }

    /**
     * State view. Attention please: take effect when [items] is empty array.
     * 状态视图，注意：[items]为空数组才会生效
     */
    var stateView: View? = null
        set(value) {
            val oldDisplayEmptyLayout = displayEmptyView()

            field = value

            val newDisplayEmptyLayout = displayEmptyView()

            if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
                notifyItemRemoved(0)
            } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
                notifyItemInserted(0)
            } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
                notifyItemChanged(0, EMPTY_PAYLOAD)
            }
        }

    /**
     * 是否使用状态布局的尺寸。
     *
     * Whether to use the dimensions of the state layout.
     */
    var isUseStateViewSize = false

    /**
     * Empty view. Attention please: take effect when [items] is empty array.
     * 空视图，注意：[items]为空数组才会生效
     */
    @Deprecated("使用 stateView", ReplaceWith("stateView"))
    var emptyView: View?
        set(value) {
            stateView = value
        }
        get() = stateView

    /**
     * Whether enable animation.
     * 是否打开动画
     */
    var animationEnable: Boolean = false

    /**
     * Whether the animation executed only the first time.
     * 动画是否仅第一次执行
     */
    var isAnimationFirstOnly = true

    /**
     * Set custom animation.
     * 设置自定义动画
     */
    var itemAnimation: ItemAnimator? = null
        set(value) {
            animationEnable = true
            field = value
        }

    init {
        mDiffer?.let {
            mDiffer.addListListener { previousList, currentList ->
                val oldDisplayEmptyLayout = displayEmptyView(previousList)
                val newDisplayEmptyLayout = displayEmptyView(currentList)

                if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
                    notifyItemRemoved(0)
                    recyclerView.scrollToPosition(0)
                } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
                    notifyItemInserted(0)
                } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
                    notifyItemChanged(0, EMPTY_PAYLOAD)
                }

                this.onCurrentListChanged(previousList, currentList)
            }

            it.submitList(_items)
        }
    }



    protected abstract fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): VH

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * 实现此方法，并使用 [holder] 完成 item 视图的操作
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: T?)

    /**
     * Optional implementation this method and use the helper to adapt the view to the given item.
     * If use [payloads], will perform this method, Please implement this method for partial refresh.
     * If use [RecyclerView.Adapter.notifyItemChanged(Int, Object)] with payload,
     * Will execute this method.
     *
     * 可选实现，如果你是用了[payloads]刷新item，请实现此方法，进行局部刷新
     *
     * @param holder   A fully initialized helper.
     * @param item     The item that needs to be displayed.
     * @param payloads payload info.
     */
    protected open fun onBindViewHolder(holder: VH, position: Int, item: T?, payloads: List<Any>) {
        onBindViewHolder(holder, position, item)
    }

    /**
     * Override this method and return your item size.
     * 重写此方法，返回你的item数量。
     */
    protected open fun getItemCount(items: List<T>): Int {
        return items.size
    }

    /**
     * Override this method and return your ViewType.
     * 重写此方法，返回你的ViewType。
     */
    protected open fun getItemViewType(position: Int, list: List<T>): Int = 0

    /**
     * Don't override this method.
     *
     * 不要重写此方法.
     *
     * @return Int
     */
    final override fun getItemCount(): Int {
        return if (displayEmptyView()) {
            1
        } else {
            getItemCount(items)
        }
    }

    /**
     * Don't override this method.
     *
     * 不要重写此方法
     *
     * @param position Int
     * @return Int
     */
    final override fun getItemViewType(position: Int): Int {
        if (displayEmptyView()) return EMPTY_VIEW
        return getItemViewType(position, items)
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == EMPTY_VIEW) {
            return StateLayoutVH(parent, stateView, isUseStateViewSize)
        }

        return onCreateViewHolder(parent.context, parent, viewType).apply {
            bindViewClickListener(this, viewType)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StateLayoutVH) {
            holder.changeStateView(stateView, isUseStateViewSize)
            return
        }

        onBindViewHolder(holder as VH, position, getItem(position))
    }

    final override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        if (holder is StateLayoutVH) {
            holder.changeStateView(stateView, isUseStateViewSize)
            return
        }

        onBindViewHolder(holder as VH, position, getItem(position), payloads)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * 当 ViewHolder 视图已附加到窗口时调用。
     *
     * Called when a view created by this holder has been attached to a window.
     * simple to solve item will layout using all.
     *
     * [asStaggeredGridFullSpan]
     *
     * @param holder
     */
    @CallSuper
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (holder is StateLayoutVH || isFullSpanItem(getItemViewType(holder.bindingAdapterPosition))) {
            holder.asStaggeredGridFullSpan()
        } else {
            runAnimator(holder)
        }

        mOnViewAttachStateChangeListeners?.forEach {
            it.onViewAttachedToWindow(holder)
        }
    }

    @CallSuper
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        mOnViewAttachStateChangeListeners?.forEach {
            it.onViewDetachedFromWindow(holder)
        }
    }

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        _recyclerView = recyclerView
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        _recyclerView = null
    }


    /**
     * 绑定 item 点击事件
     * @param viewHolder VH
     */
    protected open fun bindViewClickListener(viewHolder: VH, viewType: Int) {
        mOnItemClickListener?.let {
            viewHolder.itemView.setOnClickListener { v ->
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                onItemClick(v, position)
            }
        }
        mOnItemLongClickListener?.let {
            viewHolder.itemView.setOnLongClickListener { v ->
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                onItemLongClick(v, position)
            }
        }

        mOnItemChildClickArray?.let {
            for (i in 0 until it.size()) {
                val id = it.keyAt(i)

                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    childView.setOnClickListener { v ->
                        val position = viewHolder.bindingAdapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        onItemChildClick(v, position)
                    }
                }
            }
        }

        mOnItemChildLongClickArray?.let {
            for (i in 0 until it.size()) {
                val id = it.keyAt(i)

                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    childView.setOnLongClickListener { v ->
                        val position = viewHolder.bindingAdapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        onItemChildLongClick(v, position)
                    }
                }
            }
        }

    }

    /**
     * override this method if you want to override click event logic.
     *
     * 如果你想重新实现 item 点击事件逻辑，请重写此方法.
     * @param v
     * @param position
     */
    protected open fun onItemClick(v: View, position: Int) {
        mOnItemClickListener?.onClick(this, v, position)
    }

    /**
     * override this method if you want to override longClick event logic.
     *
     * 如果你想重新实现 item 长按事件逻辑，请重写此方法.
     *
     * @param v
     * @param position
     * @return
     */
    protected open fun onItemLongClick(v: View, position: Int): Boolean {
        return mOnItemLongClickListener?.onLongClick(this, v, position) ?: false
    }

    protected open fun onItemChildClick(v: View, position: Int) {
        mOnItemChildClickArray?.get(v.id)?.onItemClick(this, v, position)
    }

    protected open fun onItemChildLongClick(v: View, position: Int): Boolean {
        return mOnItemChildLongClickArray?.get(v.id)?.onItemLongClick(this, v, position)
            ?: false
    }


    /**
     * Is full span item.
     *
     * 是否是完整跨度的item。
     *
     * @param itemType
     * @return
     */
    open fun isFullSpanItem(itemType: Int): Boolean {
        return itemType == EMPTY_VIEW
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * 获取与数据集中指定位置的数据项。如果未找到数据，则返回 null。
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    fun getItem(@IntRange(from = 0) position: Int): T? = items.getOrNull(position)

    /**
     * 获取对应首个匹配的 item 数据的索引。如果返回 -1，表示不存在
     * @param item T
     * @return Int
     */
    fun itemIndexOfFirst(item: T): Int {
        return items.indexOfFirst { item == it }
    }

    /**
     * Set state view layout.
     *
     * 状态视图的布局id.
     *
     * @param layoutResId
     */
    fun setStateViewLayout(context: Context, @LayoutRes layoutResId: Int) {
        stateView = LayoutInflater.from(context).inflate(layoutResId, FrameLayout(context), false)
    }

    /**
     * Set empty view layout.
     *
     * 空视图的布局id。
     *
     * @param layoutResId
     */
    @Deprecated("使用 setStateViewLayout()", replaceWith = ReplaceWith("setStateViewLayout(context, layoutResId)"))
    fun setEmptyViewLayout(context: Context, @LayoutRes layoutResId: Int) {
        setStateViewLayout(context, layoutResId)
    }

    /**
     * 判断是否能显示“空状态”布局
     */
    fun displayEmptyView(list: List<T> = items): Boolean {
        if (stateView == null || !isStateViewEnable) return false
        return list.isEmpty()
    }

    /**
     * Run animation when you want to show time.
     *
     * @param holder
     */
    private fun runAnimator(holder: RecyclerView.ViewHolder) {
        if (animationEnable) {
            if (!isAnimationFirstOnly || holder.layoutPosition > mLastPosition) {
                val animation: ItemAnimator = itemAnimation ?: AlphaInAnimation()
                animation.animator(holder.itemView).apply {
                    startItemAnimator(this, holder)
                }
                mLastPosition = holder.layoutPosition
            }
        }
    }

    /**
     * Start executing animation.
     * Override this method to execute more actions.
     *
     * 开始执行动画方法。
     * 可以重写此方法，实行更多行为。
     *
     * @param anim
     * @param holder
     */
    protected open fun startItemAnimator(anim: Animator, holder: RecyclerView.ViewHolder) {
        anim.start()
    }

    /**
     * use preset animations
     *
     * 使用内置默认动画设置
     *
     * @param animationType AnimationType
     */
    fun setItemAnimation(animationType: AnimationType) {
        itemAnimation = when (animationType) {
            AnimationType.AlphaIn -> AlphaInAnimation()
            AnimationType.ScaleIn -> ScaleInAnimation()
            AnimationType.SlideInBottom -> SlideInBottomAnimation()
            AnimationType.SlideInLeft -> SlideInLeftAnimation()
            AnimationType.SlideInRight -> SlideInRightAnimation()
        }
    }

    /**
     * (Optional) Override this method to monitor changes in the dataset before and after.
     *
     * （可选）重写此方法，监听前后数据集变化.
     *
     * @param previousList 原数据集
     * @param currentList 当前数据集
     */
    open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {}

    /**
     * setting up a new instance to data;
     *
     * 设置新的数据集合
     *
     * @param list 新数据集
     */
    @JvmOverloads
    open fun submitList(list: List<T>?, commitCallback: Runnable? = null) {
        if (mDiffer == null) {
            val newList = list ?: emptyList()

            mLastPosition = -1

            val oldDisplayEmptyLayout = displayEmptyView()
            val newDisplayEmptyLayout = displayEmptyView(newList)

            val oldItem = _items

            if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
                _items = newList
                notifyItemRemoved(0)
                notifyItemRangeInserted(0, newList.size)
            } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
                notifyItemRangeRemoved(0, _items.size)
                _items = newList
                notifyItemInserted(0)
            } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
                _items = newList
                notifyItemChanged(0, EMPTY_PAYLOAD)
            } else {
                _items = newList
                notifyDataSetChanged()
            }

            onCurrentListChanged(oldItem, _items)
            commitCallback?.run()
        } else {
            // Diff的逻辑
            mDiffer.submitList(list, commitCallback)
        }
    }

    /**
     * change data
     * 改变某一位置数据
     */
    open operator fun set(@IntRange(from = 0) position: Int, data: T) {
        set(position, data,null, null)
    }

    open fun set(
        @IntRange(from = 0) position: Int,
        data: T,
        payload: Any? = null,
        commitCallback: Runnable?,
    ) {
        if (position >= items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (mDiffer == null) {
            mutableItems[position] = data
            notifyItemChanged(position, payload)

            commitCallback?.run()
        } else {
            // Diff的逻辑
            mDiffer.currentList.toMutableList().also {
                it[position] = data
                mDiffer.submitList(it, commitCallback)
            }
        }
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    @JvmOverloads
    open fun add(@IntRange(from = 0) position: Int, data: T, commitCallback: Runnable? = null) {
        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (mDiffer == null) {
            if (displayEmptyView()) {
                // 如果之前在显示空布局，需要先移除
                notifyItemRemoved(0)
            }
            mutableItems.add(position, data)
            notifyItemInserted(position)

            commitCallback?.run()
        } else {
            // Diff的逻辑
            mDiffer.currentList.toMutableList().also {
                it.add(data)
                mDiffer.submitList(it, commitCallback)
            }
        }
    }

    /**
     * add one new data，not null.
     * 添加一条新数据，不可为 null。
     */
    @JvmOverloads
    open fun add(data: T, commitCallback: Runnable? = null) {
        if (mDiffer == null) {
            if (displayEmptyView()) {
                // 如果之前在显示空布局，需要先移除
                notifyItemRemoved(0)
            }
            if (mutableItems.add(data)) {
                notifyItemInserted(_items.size - 1)
            }

            commitCallback?.run()
        } else {
            // Diff的逻辑
            mDiffer.currentList.toMutableList().also {
                it.add(data)
                mDiffer.submitList(it, commitCallback)
            }
        }
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param collection  the new data collection
     */
    @JvmOverloads
    open fun addAll(@IntRange(from = 0) position: Int, collection: Collection<T>, commitCallback: Runnable? = null) {
        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (collection.isEmpty()) {
            commitCallback?.run()
            return
        }

        if (mDiffer == null) {
            if (displayEmptyView()) {
                // 如果之前在显示空布局，需要先移除
                notifyItemRemoved(0)
            }
            if (mutableItems.addAll(position, collection)) {
                notifyItemRangeInserted(position, collection.size)
            }

            commitCallback?.run()
        } else {
            // Diff的逻辑
            mDiffer.currentList.toMutableList().also {
                it.addAll(position, collection)
                mDiffer.submitList(it, commitCallback)
            }
        }
    }

    /**
     * 添加一组数据，不可为 null。
     */
    @JvmOverloads
    open fun addAll(collection: Collection<T>, commitCallback: Runnable? = null) {
        if (collection.isEmpty()) {
            commitCallback?.run()
            return
        }

        if (mDiffer == null) {
            if (displayEmptyView()) {
                // 如果之前在显示空布局，需要先移除
                notifyItemRemoved(0)
            }

            val oldSize = _items.size
            if (mutableItems.addAll(collection)) {
                notifyItemRangeInserted(oldSize, collection.size)
            }

            commitCallback?.run()
        } else {
            // Diff的逻辑
            mDiffer.currentList.toMutableList().also {
                it.addAll(collection)
                mDiffer.submitList(it, commitCallback)
            }
        }
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    @JvmOverloads
    open fun removeAt(@IntRange(from = 0) position: Int, commitCallback: Runnable? = null) {
        if (position >= items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (mDiffer == null) {
            mutableItems.removeAt(position)
            notifyItemRemoved(position)

            // 处理空视图的情况
            if (displayEmptyView()) {
                notifyItemInserted(0)
            }

            commitCallback?.run()
        } else {
            mDiffer.currentList.toMutableList().also {
                it.removeAt(position)
                mDiffer.submitList(it, commitCallback)
            }
        }
    }

    /**
     * 删除数据
     *
     * @param data
     */
    @JvmOverloads
    open fun remove(data: T, commitCallback: Runnable? = null) {
        if (mDiffer == null) {
            val index = _items.indexOf(data)
            if (index == -1) return
            removeAt(index)

            commitCallback?.run()
        } else {
            mDiffer.currentList.toMutableList().also {
                it.remove(data)
                mDiffer.submitList(it, commitCallback)
            }
        }
    }

    /**
     * 删除给定范围内的数据
     *
     * @param range Int 索引范围
     */
    @JvmOverloads
    open fun removeAtRange(range: kotlin.ranges.IntRange, commitCallback: Runnable? = null) {
        if (range.isEmpty()) {
            return
        }
        if (range.first >= items.size) {
            throw IndexOutOfBoundsException("Range first position: ${range.first} - last position: ${range.last}. size:${items.size}")
        }

        val last = if (range.last >= items.size) {
            items.size - 1
        } else {
            range.last
        }

        if (mDiffer == null) {
            for (it in last downTo range.first) {
                mutableItems.removeAt(it)
            }

            notifyItemRangeRemoved(range.first, last - range.first + 1)

            // 处理空视图的情况
            if (displayEmptyView()) {
                notifyItemInserted(0)
            }

            commitCallback?.run()
        } else {
            val list = mDiffer.currentList.toMutableList()
            for (it in last downTo  range.first) {
                list.removeAt(it)
            }
            mDiffer.submitList(list, commitCallback)
        }
    }

    /**
     * Item swap
     * 数据位置交换。这里单纯的只是两个数据交换位置。（注意⚠️，这里移动后的数据顺序与 [move] 不同)
     *
     * @param fromPosition
     * @param toPosition
     */
    @JvmOverloads
    open fun swap(fromPosition: Int, toPosition: Int, commitCallback: Runnable? = null) {
        if (mDiffer == null) {
            if (fromPosition in _items.indices && toPosition in _items.indices) {
                Collections.swap(_items, fromPosition, toPosition)
                notifyItemChanged(fromPosition)
                notifyItemChanged(toPosition)

                commitCallback?.run()
            }
        } else {
            val list = mDiffer.currentList
            if (fromPosition in list.indices || toPosition in list.indices) {
                list.toMutableList().also {
                    Collections.swap(it, fromPosition, toPosition)
                    mDiffer.submitList(it, commitCallback)
                }
            }
        }
    }

    /**
     * Move Item
     * item 位置的移动。（注意⚠️，这里移动后的数据顺序与 [swap] 不同)
     *
     * @param fromPosition
     * @param toPosition
     */
    @JvmOverloads
    open fun move(fromPosition: Int, toPosition: Int, commitCallback: Runnable? = null) {
        if (mDiffer == null) {
            if (fromPosition in _items.indices && toPosition in _items.indices) {
                val e = mutableItems.removeAt(fromPosition)
                mutableItems.add(toPosition, e)
                notifyItemMoved(fromPosition, toPosition)

                commitCallback?.run()
            }
        } else {
            val list = mDiffer.currentList
            if (fromPosition in list.indices || toPosition in list.indices) {
                list.toMutableList().also {
                    val e = it.removeAt(fromPosition)
                    it.add(toPosition, e)
                    mDiffer.submitList(it, commitCallback)
                }
            }
        }
    }

    /**
     * _items 转化为 MutableList
     */
    private val mutableItems: MutableList<T>
        get() {
            return when (_items) {
                is java.util.AbstractList -> {
                    _items as java.util.AbstractList
                }
                is MutableList -> {
                    _items as MutableList
                }
                else -> {
                    _items.toMutableList().apply { _items = this }
                }
            }
        }

    /************************************** Set Listener ****************************************/

    fun setOnItemClickListener(listener: OnItemClickListener<T>?) = apply {
        this.mOnItemClickListener = listener
    }

    fun getOnItemClickListener(): OnItemClickListener<T>? = mOnItemClickListener

    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>?)  = apply {
        this.mOnItemLongClickListener = listener
    }

    fun getOnItemLongClickListener(): OnItemLongClickListener<T>? = mOnItemLongClickListener

    fun addOnItemChildClickListener(@IdRes id: Int, listener: OnItemChildClickListener<T>) = apply {
        mOnItemChildClickArray =
            (mOnItemChildClickArray ?: SparseArray<OnItemChildClickListener<T>>(2)).apply {
                put(id, listener)
            }
    }

    fun removeOnItemChildClickListener(@IdRes id: Int) = apply {
        mOnItemChildClickArray?.remove(id)
    }

    fun addOnItemChildLongClickListener(@IdRes id: Int, listener: OnItemChildLongClickListener<T>) =
        apply {
            mOnItemChildLongClickArray = (mOnItemChildLongClickArray ?: SparseArray<OnItemChildLongClickListener<T>>(2)).apply {
                put(id, listener)
            }
        }

    fun removeOnItemChildLongClickListener(@IdRes id: Int) = apply {
        mOnItemChildLongClickArray?.remove(id)
    }

    fun addOnViewAttachStateChangeListener(listener: OnViewAttachStateChangeListener) = apply {
        mOnViewAttachStateChangeListeners =
            (mOnViewAttachStateChangeListeners ?: ArrayList()).apply {
                if (!this.contains(listener)) {
                    this += listener
                }
            }
    }

    fun removeOnViewAttachStateChangeListener(listener: OnViewAttachStateChangeListener) {
        mOnViewAttachStateChangeListeners?.remove(listener)
    }

    fun clearOnViewAttachStateChangeListener() {
        mOnViewAttachStateChangeListeners?.clear()
    }

    /**
     * 内置默认动画类型
     */
    enum class AnimationType {
        AlphaIn, ScaleIn, SlideInBottom, SlideInLeft, SlideInRight
    }

    interface OnViewAttachStateChangeListener {

        /**
         * Called when a view is attached to the RecyclerView.
         */
        fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder)

        /**
         * Called when a view is detached from RecyclerView.
         */
        fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder)
    }


    fun interface OnItemClickListener<T : Any> {
        fun onClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int)
    }

    fun interface OnItemLongClickListener<T : Any> {
        fun onLongClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int): Boolean
    }

    fun interface OnItemChildClickListener<T : Any> {
        fun onItemClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int)
    }

    fun interface OnItemChildLongClickListener<T : Any> {
        fun onItemLongClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int): Boolean
    }


    companion object {
        val EMPTY_VIEW = R.id.BaseQuickAdapter_empty_view

        internal const val EMPTY_PAYLOAD = 0
    }
}
