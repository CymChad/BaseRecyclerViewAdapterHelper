package com.chad.library.adapter.base

import android.animation.Animator
import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.animation.*
import com.chad.library.adapter.base.module.BaseDraggableModule
import com.chad.library.adapter.base.viewholder.EmptyLayoutVH

/**
 * Base Class
 * @param T : type of data, 数据类型
 * @param VH : BaseViewHolder
 * @constructor layoutId, data(Can null parameters, the default is empty data)
 */
abstract class BaseQuickAdapter<T, VH : RecyclerView.ViewHolder>(
    open var items: List<T> = emptyList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    /**
     * 拖拽模块
     */
    val draggableModule: BaseDraggableModule
        get() {
            checkNotNull(mDraggableModule) { "Please first implements DraggableModule" }
            return mDraggableModule!!
        }

    /********************************* Private property *****************************************/
    private var mLastPosition = -1
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null
    private val mOnItemChildClickArray = SparseArray<OnItemChildClickListener<T>>(3)
    private val mOnItemChildLongClickArray = SparseArray<OnItemChildLongClickListener<T>>(3)
    private var onViewAttachStateChangeListeners: MutableList<OnViewAttachStateChangeListener>? =
        null

    //    private var mUpFetchModule: BaseUpFetchModule? = null
    private var mDraggableModule: BaseDraggableModule? = null

    var recyclerViewOrNull: RecyclerView? = null
        private set

    val recyclerView: RecyclerView
        get() {
            checkNotNull(recyclerViewOrNull) {
                "Please get it after onAttachedToRecyclerView()"
            }
            return recyclerViewOrNull!!
        }

    val context: Context
        get() {
            return recyclerView.context
        }


    /**
     * 判断 ViewHolder 是否是 EmptyLayoutVH
     * @receiver RecyclerView.ViewHolder
     * @return Boolean
     */
    inline val RecyclerView.ViewHolder.isEmptyViewHolder: Boolean
        get() = this is EmptyLayoutVH

    /** 是否使用空布局 */
    var isEmptyViewEnable = true
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
     * 空视图，注意：[items]为空数组才会生效
     */
    var emptyView: View? = null
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
     * 是否打开动画
     */
    var animationEnable: Boolean = false

    /**
     * 动画是否仅第一次执行
     */
    var isAnimationFirstOnly = true

    /**
     * 设置自定义动画
     */
    var itemAnimation: BaseAnimation? = null
        set(value) {
            animationEnable = true
            field = value
        }

    init {
        checkModule()
    }

    /**
     * 检查模块
     */
    private fun checkModule() {
//        if (this is DraggableModule) {
//            mDraggableModule = this.addDraggableModule(this)
//        }
    }

    protected abstract fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): VH

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * 实现此方法，并使用 helper 完成 item 视图的操作
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: T)

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
    protected open fun onBindViewHolder(holder: VH, position: Int, item: T, payloads: List<Any>) {}

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
     * 不要重写此方法
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
     * 不要重写此方法
     *
     * @param position Int
     * @return Int
     */
    final override fun getItemViewType(position: Int): Int {
        if (displayEmptyView()) {
            return EMPTY_VIEW
        }

        return getItemViewType(position, items)
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == EMPTY_VIEW) {

            return EmptyLayoutVH(FrameLayout(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            })
        }

        return onCreateViewHolder(parent.context, parent, viewType).apply {
            bindViewClickListener(this, viewType)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EmptyLayoutVH) {
            holder.changeEmptyView(emptyView)
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

        if (holder is EmptyLayoutVH) {
            holder.changeEmptyView(emptyView)
            return
        }

        onBindViewHolder(holder as VH, position, getItem(position), payloads)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Called when a view created by this holder has been attached to a window.
     * simple to solve item will layout using all
     * [setStaggeredGridFullSpan]
     *
     * @param holder
     */
    @CallSuper
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (isFullSpanItem(getItemViewType(holder.bindingAdapterPosition))) {
            setStaggeredGridFullSpan(holder)
        } else {
            addAnimation(holder)
        }

        onViewAttachStateChangeListeners?.forEach {
            it.onViewAttachedToWindow(holder)
        }
    }

    @CallSuper
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        onViewAttachStateChangeListeners?.forEach {
            it.onViewDetachedFromWindow(holder)
        }
    }

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewOrNull = recyclerView

        mDraggableModule?.attachToRecyclerView(recyclerView)
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerViewOrNull = null
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


        for (i in 0 until mOnItemChildClickArray.size()) {
            val id = mOnItemChildClickArray.keyAt(i)

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


        for (i in 0 until mOnItemChildLongClickArray.size()) {
            val id = mOnItemChildLongClickArray.keyAt(i)

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

    /**
     * override this method if you want to override click event logic
     *
     * 如果你想重新实现 item 点击事件逻辑，请重写此方法
     * @param v
     * @param position
     */
    protected open fun onItemClick(v: View, position: Int) {
        mOnItemClickListener?.onItemClick(this, v, position)
    }

    /**
     * override this method if you want to override longClick event logic
     *
     * 如果你想重新实现 item 长按事件逻辑，请重写此方法
     * @param v
     * @param position
     * @return
     */
    protected open fun onItemLongClick(v: View, position: Int): Boolean {
        return mOnItemLongClickListener?.onItemLongClick(this, v, position) ?: false
    }

    protected open fun onItemChildClick(v: View, position: Int) {
        mOnItemChildClickArray.get(v.id)?.onItemChildClick(this, v, position)
    }

    protected open fun onItemChildLongClick(v: View, position: Int): Boolean {
        return mOnItemChildLongClickArray.get(v.id)?.onItemChildLongClick(this, v, position)
            ?: false
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected fun setStaggeredGridFullSpan(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }


    /**
     * Is full span item
     * 是否是完整跨度的item
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
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    fun getItem(@IntRange(from = 0) position: Int): T = items[position]

    /**
     * 如果返回 -1，表示不存在
     * @param item T?
     * @return Int
     */
    fun getItemPosition(item: T?): Int {
        return if (item != null && items.isNotEmpty()) items.indexOf(item) else -1
    }


    /**
     * Set empty view layout
     * 空视图的布局id
     *
     * @param layoutResId
     */
    fun setEmptyViewLayout(@LayoutRes layoutResId: Int) {
        recyclerViewOrNull?.let {
            val view = LayoutInflater.from(it.context).inflate(layoutResId, it, false)
            emptyView = view
        }
    }

    /**
     * 是否需要显示空状态布局
     */
    fun displayEmptyView/*?*/(list: List<T> = this.items): Boolean {
        if (emptyView == null) {
            return false
        }
        if (!isEmptyViewEnable) {
            return false
        }
        return list.isEmpty()
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (animationEnable) {
            if (!isAnimationFirstOnly || holder.layoutPosition > mLastPosition) {
                val animation: BaseAnimation = itemAnimation ?: AlphaInAnimation()
                animation.animators(holder.itemView).forEach {
                    startItemAnim(it, holder)
                }
                mLastPosition = holder.layoutPosition
            }
        }
    }

    /**
     * 开始执行动画方法
     * 可以重写此方法，实行更多行为
     *
     * @param anim
     * @param holder
     */
    protected open fun startItemAnim(anim: Animator, holder: RecyclerView.ViewHolder) {
        anim.start()
    }

    /**
     * 使用内置默认动画设置
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
     * setting up a new instance to data;
     *
     * 设置新的数据集合
     *
     * @param list 新数据集
     */
    open fun submitList(list: List<T>?) {
        if (list === this.items) {
            return
        }

        mLastPosition = -1

        val newList = list ?: emptyList()

        val oldDisplayEmptyLayout = displayEmptyView()
        val newDisplayEmptyLayout = displayEmptyView(newList)

        if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
            this.items = newList
            notifyItemRemoved(0)
            notifyItemRangeInserted(0, newList.size)
        } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
            notifyItemRangeRemoved(0, items.size)
            this.items = newList
            notifyItemInserted(0)
        } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
            this.items = newList
            notifyItemChanged(0, EMPTY_PAYLOAD)
        } else {
            this.items = newList
            notifyDataSetChanged()
        }
    }

    /**
     * change data
     * 改变某一位置数据
     */
    open operator fun set(@IntRange(from = 0) position: Int, data: T) {
        if (position >= this.items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (items is MutableList) {
            (items as MutableList<T>)[position] = data
        } else {
            items = ArrayList(items).apply { set(position, data) }
        }
        notifyItemChanged(position)
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    open fun add(@IntRange(from = 0) position: Int, data: T) {
        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (displayEmptyView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }

        if (items is MutableList) {
            (items as MutableList<T>).add(position, data)
        } else {
            items = ArrayList(items).apply { add(position, data) }
        }
        notifyItemInserted(position)

    }

    /**
     * add one new data
     * 添加一条新数据
     */
    open fun add(@NonNull data: T) {
        if (displayEmptyView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }

        if (items is MutableList) {
            (items as MutableList<T>).add(data)
        } else {
            items = ArrayList(items).apply { add(data) }
        }
        notifyItemInserted(items.size - 1)
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    open fun addAll(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (displayEmptyView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }

        if (items is MutableList) {
            (items as MutableList<T>).addAll(position, newData)
        } else {
            items = ArrayList(items).apply { addAll(position, newData) }
        }

        notifyItemRangeInserted(position, newData.size)
    }

    open fun addAll(@NonNull newData: Collection<T>) {
        if (displayEmptyView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }

        val oldSize = items.size
        if (items is MutableList) {
            (items as MutableList<T>).addAll(newData)
        } else {
            items = ArrayList(items).apply { addAll(newData) }
        }

        notifyItemRangeInserted(oldSize, newData.size)
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    open fun removeAt(@IntRange(from = 0) position: Int) {
        if (position >= items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (items is MutableList) {
            (items as MutableList<T>).removeAt(position)
        } else {
            items = ArrayList(items).apply { removeAt(position) }
        }

        notifyItemRemoved(position)

        // 处理空视图的情况
        if (displayEmptyView()) {
            notifyItemInserted(0)
        }
    }

    open fun remove(data: T) {
        val index = items.indexOf(data)
        if (index == -1) {
            return
        }
        removeAt(index)
    }

    /************************************** Set Listener ****************************************/


    fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        this.mOnItemClickListener = listener
    }

    fun getOnItemClickListener(): OnItemClickListener<T>? = mOnItemClickListener
    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>?) {
        this.mOnItemLongClickListener = listener
    }

    fun getOnItemLongClickListener(): OnItemLongClickListener<T>? = mOnItemLongClickListener
    fun addOnItemChildClickListener(@IdRes id: Int, listener: OnItemChildClickListener<T>) = apply {
        mOnItemChildClickArray[id] = listener
    }

    fun removeOnItemChildClickListener(@IdRes id: Int) = apply {
        mOnItemChildClickArray.remove(id)
    }

    fun addOnItemChildLongClickListener(@IdRes id: Int, listener: OnItemChildLongClickListener<T>) =
        apply {
            mOnItemChildLongClickArray[id] = listener
        }

    fun removeOnItemChildLongClickListener(@IdRes id: Int) = apply {
        mOnItemChildLongClickArray.remove(id)
    }

    fun addOnViewAttachStateChangeListener(listener: OnViewAttachStateChangeListener) {
        if (onViewAttachStateChangeListeners == null) {
            onViewAttachStateChangeListeners = ArrayList()
        }
        onViewAttachStateChangeListeners?.add(listener)
    }

    fun removeOnViewAttachStateChangeListener(listener: OnViewAttachStateChangeListener) {
        onViewAttachStateChangeListeners?.remove(listener)
    }

    fun clearOnViewAttachStateChangeListener() {
        onViewAttachStateChangeListeners?.clear()
    }

    /**
     * 内置默认动画类型
     */
    enum class AnimationType {
        AlphaIn, ScaleIn, SlideInBottom, SlideInLeft, SlideInRight
    }

    fun interface OnItemClickListener<T> {
        fun onItemClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int)
    }

    fun interface OnItemLongClickListener<T> {
        fun onItemLongClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int): Boolean
    }

    fun interface OnItemChildClickListener<T> {
        fun onItemChildClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int)
    }

    fun interface OnItemChildLongClickListener<T> {
        fun onItemChildLongClick(
            adapter: BaseQuickAdapter<T, *>,
            view: View,
            position: Int
        ): Boolean
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

    companion object {
        const val EMPTY_VIEW = 0x10000555

        internal const val EMPTY_PAYLOAD = 0
    }
}
