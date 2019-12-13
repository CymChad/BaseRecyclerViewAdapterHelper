package com.chad.library.adapter.base

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.animation.*
import com.chad.library.adapter.base.diff.BrvahAsyncDiffer
import com.chad.library.adapter.base.diff.BrvahAsyncDifferConfig
import com.chad.library.adapter.base.diff.BrvahListUpdateCallback
import com.chad.library.adapter.base.listener.*
import com.chad.library.adapter.base.module.*
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.ref.WeakReference
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * 获取模块
 */
private interface BaseQuickAdapterModuleImp {
    /**
     * 重写此方法，返回自定义模块
     * @param baseQuickAdapter BaseQuickAdapter<*, *>
     * @return BaseLoadMoreModule
     */
    fun addLoadMoreModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseLoadMoreModule {
        return BaseLoadMoreModule(baseQuickAdapter)
    }

    /**
     * 重写此方法，返回自定义模块
     * @param baseQuickAdapter BaseQuickAdapter<*, *>
     * @return BaseUpFetchModule
     */
    fun addUpFetchModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseUpFetchModule {
        return BaseUpFetchModule(baseQuickAdapter)
    }

    /**
     * 重写此方法，返回自定义模块
     * @param baseQuickAdapter BaseQuickAdapter<*, *>
     * @return BaseExpandableModule
     */
    fun addDraggableModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseDraggableModule {
        return BaseDraggableModule(baseQuickAdapter)
    }
}

/**
 * Base Class
 * @param T : type of data, 数据类型
 * @param VH : BaseViewHolder
 * @constructor layoutId, data(Can null parameters, the default is empty data)
 */
abstract class BaseQuickAdapter<T, VH : BaseViewHolder>
@JvmOverloads constructor(@LayoutRes private val layoutResId: Int,
                          data: MutableList<T>? = null)
    : RecyclerView.Adapter<VH>(), BaseQuickAdapterModuleImp, BaseListenerImp {

    companion object {
        const val HEADER_VIEW = 0x10000111
        const val LOAD_MORE_VIEW = 0x10000222
        const val FOOTER_VIEW = 0x10000333
        const val EMPTY_VIEW = 0x10000555
    }

    /***************************** Public property settings *************************************/
    /**
     * data, Only allowed to get.
     *
     * 数据, 只允许 get。
     */
    var data: MutableList<T> = data ?: arrayListOf()
        internal set
    /**
     * 当显示空布局时，是否显示 Header
     */
    var headerWithEmptyEnable = false
    /** 当显示空布局时，是否显示 Foot */
    var footerWithEmptyEnable = false
    /** 是否使用空布局 */
    var isUseEmpty = true

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    var headerViewAsFlow: Boolean = false
    var footerViewAsFlow: Boolean = false

    var animationEnable: Boolean = false
    var isAnimationFirstOnly = true
    var adapterAnimation: BaseAnimation? = null
        set(value) {
            animationEnable = true
            field = value
        }

    /**
     * 加载更多模块
     */
    var loadMoreModule: BaseLoadMoreModule? = null
        private set
    /**
     * 向上加载模块
     */
    var upFetchModule: BaseUpFetchModule? = null
        private set
    /**
     * 拖拽模块
     */
    var draggableModule: BaseDraggableModule? = null
        private set

    /********************************* Private property *****************************************/
    private var mDiffHelper: BrvahAsyncDiffer<T>? = null

    private lateinit var mHeaderLayout: LinearLayout
    private lateinit var mFooterLayout: LinearLayout
    private lateinit var mEmptyLayout: FrameLayout

    private var mLastPosition = -1

    private var mSpanSizeLookup: GridSpanSizeLookup? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnItemChildClickListener: OnItemChildClickListener? = null
    private var mOnItemChildLongClickListener: OnItemChildLongClickListener? = null

    protected lateinit var context: Context
        private set

    lateinit var weakRecyclerView: WeakReference<RecyclerView>

    /******************************* RecyclerView Method ****************************************/

    init {
        checkModule()
    }

    private fun checkModule() {
        if (this is LoadMoreModule) {
            loadMoreModule = this.addLoadMoreModule(this)
        }
        if (this is UpFetchModule) {
            upFetchModule = this.addUpFetchModule(this)
        }
        if (this is DraggableModule) {
            draggableModule = this.addDraggableModule(this)
        }
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * 实现此方法，并使用 helper 完成 item 视图的操作
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun convert(helper: VH, item: T?)

    /**
     * Optional implementation this method and use the helper to adapt the view to the given item.
     * If use [payloads], will perform this method, Please implement this method for partial refresh.
     * If use [RecyclerView.Adapter.notifyItemChanged] with payload,
     * Will execute this method.
     *
     * 可选实现，如果你是用了[payloads]刷新item，请实现此方法，进行局部刷新
     *
     * @param helper   A fully initialized helper.
     * @param item     The item that needs to be displayed.
     * @param payloads payload info.
     */
    protected open fun convert(helper: VH, item: T?, payloads: List<Any>) {}

    /**
     * （可选重写）当 item 的 ViewHolder创建完毕后，执行此方法。
     * 可在此对 ViewHolder 进行处理，例如进行 DataBinding 绑定 view
     *
     * @param helper VH
     */
    protected open fun onItemViewHolderCreated(viewHolder: VH, viewType: Int) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val baseViewHolder: VH
        when (viewType) {
            LOAD_MORE_VIEW -> {
                val view = loadMoreModule!!.loadMoreView.getRootView(parent)
                baseViewHolder = createBaseViewHolder(view)
                loadMoreModule!!.setupViewHolder(baseViewHolder)
            }
            HEADER_VIEW -> {
                val headerLayoutVp = mHeaderLayout.parent
                if (headerLayoutVp is ViewGroup) {
                    headerLayoutVp.removeView(mHeaderLayout)
                }

                baseViewHolder = createBaseViewHolder(mHeaderLayout)
            }
            EMPTY_VIEW -> {
                val emptyLayoutVp = mEmptyLayout.parent
                if (emptyLayoutVp is ViewGroup) {
                    emptyLayoutVp.removeView(mEmptyLayout)
                }

                baseViewHolder = createBaseViewHolder(mEmptyLayout)
            }
            FOOTER_VIEW -> {
                val footerLayoutVp = mFooterLayout.parent
                if (footerLayoutVp is ViewGroup) {
                    footerLayoutVp.removeView(mFooterLayout)
                }

                baseViewHolder = createBaseViewHolder(mFooterLayout)
            }
            else -> {
                val viewHolder = onCreateDefViewHolder(parent, viewType)
                bindViewClickListener(viewHolder, viewType)
                draggableModule?.initView(viewHolder)
                onItemViewHolderCreated(viewHolder, viewType)
                baseViewHolder = viewHolder
            }
        }

        return baseViewHolder
    }

    override fun getItemCount(): Int {
        if (hasEmptyView()) {
            var count = 1
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                count++
            }
            if (footerWithEmptyEnable && hasFooterLayout()) {
                count++
            }
            return count
        } else {
            val loadMoreCount = if (loadMoreModule?.hasLoadMoreView() == true) {
                1
            } else {
                0
            }
            return getHeaderLayoutCount() + getDefItemCount() + getFooterLayoutCount() + loadMoreCount
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (hasEmptyView()) {
            val header = headerWithEmptyEnable && hasHeaderLayout()
            return when (position) {
                0 -> if (header) {
                    HEADER_VIEW
                } else {
                    EMPTY_VIEW
                }
                1 -> if (header) {
                    EMPTY_VIEW
                } else {
                    FOOTER_VIEW
                }
                2 -> FOOTER_VIEW
                else -> EMPTY_VIEW
            }
        }

        val hasHeader = hasHeaderLayout()
        if (hasHeader && position == 0) {
            return HEADER_VIEW
        } else {
            var adjPosition = if (hasHeader) {
                position - 1
            } else {
                position
            }
            val dataSize = data.size
            return if (adjPosition < dataSize) {
                getDefItemViewType(adjPosition)
            } else {
                adjPosition -= dataSize
                val numFooters = if (hasFooterLayout()) {
                    1
                } else {
                    0
                }
                if (adjPosition < numFooters) {
                    FOOTER_VIEW
                } else {
                    LOAD_MORE_VIEW
                }
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        //Add up fetch logic, almost like load more, but simpler.
        upFetchModule?.autoUpFetch(position)
        //Do not move position, need to change before LoadMoreView binding
        loadMoreModule?.autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> loadMoreModule?.loadMoreView?.convert(holder, position)
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, data.getOrNull(position - getHeaderLayoutCount()))
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        //Add up fetch logic, almost like load more, but simpler.
        upFetchModule?.autoUpFetch(position)
        //Do not move position, need to change before LoadMoreView binding
        loadMoreModule?.autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> loadMoreModule?.loadMoreView?.convert(holder, position)
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, data.getOrNull(position - getHeaderLayoutCount()), payloads)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * [setFullSpan]
     *
     * @param holder
     */
    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOAD_MORE_VIEW) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        weakRecyclerView = WeakReference(recyclerView)
        this.context = recyclerView.context
        draggableModule?.attachToRecyclerView(recyclerView)

        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val defSpanSizeLookup = manager.spanSizeLookup
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    if (type == HEADER_VIEW && headerViewAsFlow) {
                        return 1
                    }
                    if (type == FOOTER_VIEW && footerViewAsFlow) {
                        return 1
                    }
                    return if (mSpanSizeLookup == null) {
                        if (isFixedViewType(type)) manager.spanCount else defSpanSizeLookup.getSpanSize(position)
                    } else {
                        if (isFixedViewType(type))
                            manager.spanCount
                        else
                            mSpanSizeLookup!!.getSpanSize(manager, type, position - getHeaderLayoutCount())
                    }
                }

            }
        }
    }

    protected open fun isFixedViewType(type: Int): Boolean {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOAD_MORE_VIEW
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    fun getItem(@IntRange(from = 0) position: Int): T? {
        return data.getOrNull(position)
    }

    internal fun getItemPosition(item: T?): Int {
        return if (item != null && data.isNotEmpty()) data.indexOf(item) else -1
    }

    /**
     * 用于保存需要设置点击事件的 item
     */
    private val childClickViewIds = LinkedHashSet<Int>()

    fun getChildClickViewIds(): LinkedHashSet<Int> {
        return childClickViewIds
    }

    /**
     * 设置需要点击事件的子view
     * @param viewIds IntArray
     */
    fun addChildClickViewIds(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childClickViewIds.add(viewId)
        }
    }

    /**
     * 用于保存需要设置长按点击事件的 item
     */
    private val childLongClickViewIds = LinkedHashSet<Int>()

    fun getChildLongClickViewIds(): LinkedHashSet<Int> {
        return childLongClickViewIds
    }

    /**
     * 设置需要长按点击事件的子view
     * @param viewIds IntArray
     */
    fun addChildLongClickViewIds(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childLongClickViewIds.add(viewId)
        }
    }

    /**
     * 绑定 item 点击事件
     * @param viewHolder VH
     */
    protected open fun bindViewClickListener(viewHolder: VH, viewType: Int) {
        mOnItemClickListener?.let {
            viewHolder.itemView.setOnClickListener { v ->
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= getHeaderLayoutCount()
                setOnItemClick(v, position)
            }
        }
        mOnItemLongClickListener?.let {
            viewHolder.itemView.setOnLongClickListener { v ->
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= getHeaderLayoutCount()
                setOnItemLongClick(v, position)
            }
        }

        mOnItemChildClickListener?.let {
            for (id in getChildClickViewIds()) {
                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.setOnClickListener { v ->
                        var position = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        position -= getHeaderLayoutCount()
                        setOnItemChildClick(v, position)
                    }
                }
            }
        }
        mOnItemChildLongClickListener?.let {
            for (id in getChildLongClickViewIds()) {
                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isLongClickable) {
                        childView.isLongClickable = true
                    }
                    childView.setOnLongClickListener { v ->
                        var position = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        position -= getHeaderLayoutCount()
                        setOnItemChildLongClick(v, position)
                    }
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
    protected open fun setOnItemClick(v: View, position: Int) {
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
    protected open fun setOnItemLongClick(v: View, position: Int): Boolean {
        return mOnItemLongClickListener?.onItemLongClick(this, v, position) ?: false
    }

    protected open fun setOnItemChildClick(v: View, position: Int) {
        mOnItemChildClickListener?.onItemChildClick(this, v, position)
    }

    protected open fun setOnItemChildLongClick(v: View, position: Int): Boolean {
        return mOnItemChildLongClickListener?.onItemChildLongClick(this, v, position) ?: false
    }

    protected open fun getDefItemCount(): Int {
        return data.size
    }

    protected open fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    protected open fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createBaseViewHolder(parent, layoutResId)
    }

    protected open fun createBaseViewHolder(parent: ViewGroup, @LayoutRes layoutResId: Int): VH {
        return createBaseViewHolder(parent.getItemView(layoutResId))
    }

    /**
     * 创建 ViewHolder。可以重写
     *
     * @param view View
     * @return VH
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun createBaseViewHolder(view: View): VH {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        // 泛型擦除会导致z为null
        val vh: VH? = if (z == null) {
            BaseViewHolder(view) as VH
        } else {
            createBaseGenericKInstance(z, view)
        }
        return vh ?: BaseViewHolder(view) as VH
    }

    /**
     * get generic parameter VH
     *
     * @param z
     * @return
     */
    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                if (temp is Class<*>) {
                    if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                        return temp
                    }
                } else if (temp is ParameterizedType) {
                    val rawType = temp.rawType
                    if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(rawType)) {
                        return rawType
                    }
                }
            }
        }
        return null
    }

    /**
     * try to create Generic VH instance
     *
     * @param z
     * @param view
     * @return
     */
    @Suppress("UNCHECKED_CAST")
    private fun createBaseGenericKInstance(z: Class<*>, view: View): VH? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this, view) as VH
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as VH
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }


    /********************************************************************************************/
    /********************************* HeaderView Method ****************************************/
    /********************************************************************************************/
    @JvmOverloads
    fun addHeaderView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (!this::mHeaderLayout.isInitialized) {
            mHeaderLayout = LinearLayout(view.context)
            mHeaderLayout.orientation = orientation
            mHeaderLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
                RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }

        val childCount = mHeaderLayout.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mHeaderLayout.addView(view, mIndex)
        if (mHeaderLayout.childCount == 1) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    @JvmOverloads
    fun setHeaderView(view: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (!this::mHeaderLayout.isInitialized || mHeaderLayout.childCount <= index) {
            addHeaderView(view, index, orientation)
        } else {
            mHeaderLayout.removeViewAt(index)
            mHeaderLayout.addView(view, index)
            index
        }
    }

    /**
     * 是否有 HeaderLayout
     * @return Boolean
     */
    fun hasHeaderLayout(): Boolean {
        if (this::mHeaderLayout.isInitialized && mHeaderLayout.childCount > 0) {
            return true
        }
        return false
    }

    fun removeHeaderView(header: View) {
        if (!hasHeaderLayout()) return

        mHeaderLayout.removeView(header)
        if (mHeaderLayout.childCount == 0) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllHeaderView() {
        if (!hasHeaderLayout()) return

        mHeaderLayout.removeAllViews()
        val position = getHeaderViewPosition()
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    private fun getHeaderViewPosition(): Int {
        if (hasEmptyView()) {
            if (headerWithEmptyEnable) {
                return 0
            }
        } else {
            return 0
        }
        return -1
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    fun getHeaderLayoutCount(): Int =
            if (hasHeaderLayout()) {
                1
            } else {
                0
            }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    /********************************************************************************************/
    /********************************* FooterView Method ****************************************/
    /********************************************************************************************/
    @JvmOverloads
    fun addFooterView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (!this::mFooterLayout.isInitialized) {
            mFooterLayout = LinearLayout(view.context)
            mFooterLayout.orientation = orientation
            mFooterLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
                RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }

        val childCount = mFooterLayout.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mFooterLayout.addView(view, mIndex)
        if (mFooterLayout.childCount == 1) {
            val position = getFooterViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    @JvmOverloads
    fun setFooterView(view: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (!this::mFooterLayout.isInitialized || mFooterLayout.childCount <= index) {
            addHeaderView(view, index, orientation)
        } else {
            mFooterLayout.removeViewAt(index)
            mFooterLayout.addView(view, index)
            index
        }
    }

    fun hasFooterLayout(): Boolean {
        if (this::mFooterLayout.isInitialized && mFooterLayout.childCount > 0) {
            return true
        }
        return false
    }

    fun removeFooterView(footer: View) {
        if (!hasFooterLayout()) return

        mFooterLayout.removeView(footer)
        if (mFooterLayout.childCount == 0) {
            val position = getFooterViewPosition()
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllFooterView() {
        if (!hasFooterLayout()) return

        mFooterLayout.removeAllViews()
        val position = getFooterViewPosition()
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    private fun getFooterViewPosition(): Int {
        //Return to footer view notify position
        if (hasEmptyView()) {
            var position = 1
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                position++
            }
            if (footerWithEmptyEnable) {
                return position
            }
        } else {
            return getHeaderLayoutCount() + data.size
        }
        return -1
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    fun getFooterLayoutCount(): Int =
            if (hasFooterLayout()) {
                1
            } else {
                0
            }

    /********************************************************************************************/
    /********************************** EmptyView Method ****************************************/
    /********************************************************************************************/
    /**
     * 设置空布局视图，注意：[data]必须为空数组
     * @param emptyView View
     */
    fun setEmptyView(emptyView: View) {
        val oldItemCount = itemCount
        var insert = false
        if (!this::mEmptyLayout.isInitialized) {
            mEmptyLayout = FrameLayout(emptyView.context)

            mEmptyLayout.layoutParams = emptyView.layoutParams?.let {
                return@let ViewGroup.LayoutParams(it.width, it.height)
            } ?: ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

            insert = true
        }
        mEmptyLayout.removeAllViews()
        mEmptyLayout.addView(emptyView)
        isUseEmpty = true
        if (insert && hasEmptyView()) {
            var position = 0
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                position++
            }
            if (itemCount > oldItemCount) {
                notifyItemInserted(position)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun setEmptyView(layoutResId: Int) {
        weakRecyclerView.get()?.let {
            val view = LayoutInflater.from(it.context).inflate(layoutResId, it, false)
            setEmptyView(view)
        }
    }

    fun hasEmptyView(): Boolean {
        if (!this::mEmptyLayout.isInitialized || mEmptyLayout.childCount == 0) {
            return false
        }
        if (!isUseEmpty) {
            return false
        }
        return data.isEmpty()
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    fun getEmptyLayout(): FrameLayout? = if (this::mEmptyLayout.isInitialized) {
        mEmptyLayout
    } else {
        null
    }

    /*************************** Animation ******************************************/

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (animationEnable) {
            if (!isAnimationFirstOnly || holder.layoutPosition > mLastPosition) {
                val animation: BaseAnimation = adapterAnimation?.let {
                    it
                } ?: AlphaInAnimation()
                for (anim in animation.animators(holder.itemView)) {
                    startAnim(anim, holder.layoutPosition)
                }
                mLastPosition = holder.layoutPosition
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected open fun startAnim(anim: Animator, index: Int) {
        anim.start()
    }

    /**
     * 动画类型枚举
     */
    enum class AnimationType {
        AlphaIn, ScaleIn, SlideInBottom, SlideInLeft, SlideInRight
    }

    /**
     * 使用内置默认动画设置
     * @param animationType AnimationType
     */
    fun setAnimationWithDefault(animationType: AnimationType) {
        adapterAnimation = when (animationType) {
            AnimationType.AlphaIn -> AlphaInAnimation()
            AnimationType.ScaleIn -> ScaleInAnimation()
            AnimationType.SlideInBottom -> SlideInBottomAnimation()
            AnimationType.SlideInLeft -> SlideInLeftAnimation()
            AnimationType.SlideInRight -> SlideInRightAnimation()
        }
    }

    /*************************** 设置数据相关 ******************************************/

    /**
     * setting up a new instance to data;
     *
     * 设置新的数据实例
     * @param data
     */
    open fun setNewData(data: MutableList<T>?) {
        if (data == this.data) {
            return
        }
        this.data = data ?: arrayListOf()
        loadMoreModule?.reset()
        mLastPosition = -1
        notifyDataSetChanged()
    }

    /**
     * add one new data in to certain location
     *
     * @param position
     */
    open fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyItemInserted(position + getHeaderLayoutCount())
        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     */
    open fun addData(@NonNull data: T) {
        this.data.add(data)
        notifyItemInserted(this.data.size + getHeaderLayoutCount())
        compatibilityDataSizeChanged(1)
    }

    /**
     * add new data in to certain location
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    open fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        this.data.addAll(position, newData)
        notifyItemRangeInserted(position + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    open fun addData(@NonNull newData: Collection<T>) {
        this.data.addAll(newData)
        notifyItemRangeInserted(this.data.size - newData.size + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    open fun remove(@IntRange(from = 0) position: Int) {
        if (position >= data.size) {
            return
        }
        this.data.removeAt(position)
        val internalPosition = position + getHeaderLayoutCount()
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, this.data.size - internalPosition)
    }

    /**
     * change data
     */
    open fun setData(@IntRange(from = 0) index: Int, data: T) {
        this.data[index] = data
        notifyItemChanged(index + getHeaderLayoutCount())
    }

    /**
     * use data to replace all item in mData. this method is different [setNewData],
     * it doesn't change the [BaseQuickAdapter.data] reference
     *
     * @param newData data collection
     */
    open fun replaceData(newData: Collection<T>) {
        // 不是同一个引用才清空列表
        if (newData != this.data) {
            this.data.clear()
            this.data.addAll(newData)
        }
        notifyDataSetChanged()
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    protected fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }

    /**
     * 设置Diff Callback，用于快速生成 Diff Config。
     *
     * @param diffCallback ItemCallback<T>
     */
    fun setDiffCallback(diffCallback: DiffUtil.ItemCallback<T>) {
        this.setDiffConfig(BrvahAsyncDifferConfig.Builder(diffCallback).build())
    }

    /**
     * 设置Diff Config。如需自定义线程，请使用此方法。
     * 在使用 [setDiffNewData] 前，必须设置此方法
     * @param config BrvahAsyncDifferConfig<T>
     */
    fun setDiffConfig(config: BrvahAsyncDifferConfig<T>) {
        mDiffHelper = BrvahAsyncDiffer(this, config)
    }

    fun getDiffHelper(): BrvahAsyncDiffer<T> {
        checkNotNull(mDiffHelper) {
            "Please use setDiffCallback() or setDiffConfig() first!"
        }
        return mDiffHelper!!
    }

    /**
     * 此方法为异步Diff，无需考虑性能问题
     *
     * use DiffResult setting up a new instance to data.
     * This method is asynchronous.
     *
     * @param newData MutableList<T>?
     */
    open fun setDiffNewData(newData: MutableList<T>?) {
        if (hasEmptyView()) { // If the current view is an empty view, set the new data directly without diff
            setNewData(newData)
            return
        }
        mDiffHelper?.submitList(newData)
    }

    /**
     * 使用 DiffResult 设置新实例
     * use DiffResult setting up a new instance to data
     *
     * @param diffResult DiffResult
     * @param newData New Data
     */
    open fun setDiffNewData(@NonNull diffResult: DiffUtil.DiffResult, newData: MutableList<T>) {
        if (hasEmptyView()) {
            // If the current view is an empty view, set the new data directly without diff
            setNewData(newData)
            return
        }
        diffResult.dispatchUpdatesTo(BrvahListUpdateCallback(this))
        this.data = newData
    }

    /************************************** Set Listener ****************************************/

    override fun setGridSpanSizeLookup(spanSizeLookup: GridSpanSizeLookup?) {
        this.mSpanSizeLookup = spanSizeLookup
    }

    override fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.mOnItemClickListener = listener
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.mOnItemLongClickListener = listener
    }

    override fun setOnItemChildClickListener(listener: OnItemChildClickListener?) {
        this.mOnItemChildClickListener = listener
    }

    override fun setOnItemChildLongClickListener(listener: OnItemChildLongClickListener?) {
        this.mOnItemChildLongClickListener = listener
    }

    fun getOnItemClickListener(): OnItemClickListener? = mOnItemClickListener

    fun getOnItemLongClickListener(): OnItemLongClickListener? = mOnItemLongClickListener

    fun getOnItemChildClickListener(): OnItemChildClickListener? = mOnItemChildClickListener

    fun getOnItemChildLongClickListener(): OnItemChildLongClickListener? = mOnItemChildLongClickListener
}
