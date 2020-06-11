package com.chad.library.adapter.base

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewParent
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
import com.jakewharton.rxbinding4.view.clicks
import java.lang.ref.WeakReference
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

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
    var adapterAnimation: BaseAnimation? = null
        set(value) {
            animationEnable = true
            field = value
        }

    /**
     * 加载更多模块
     */
    val loadMoreModule: BaseLoadMoreModule
        get() {
            checkNotNull(mLoadMoreModule) { "Please first implements LoadMoreModule" }
            return mLoadMoreModule!!
        }

    /**
     * 向上加载模块
     */
    val upFetchModule: BaseUpFetchModule
        get() {
            checkNotNull(mUpFetchModule) { "Please first implements UpFetchModule" }
            return mUpFetchModule!!
        }

    /**
     * 拖拽模块
     */
    val draggableModule: BaseDraggableModule
        get() {
            checkNotNull(mDraggableModule) { "Please first implements DraggableModule" }
            return mDraggableModule!!
        }

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
    private var mUpFetchModule: BaseUpFetchModule? = null
    private var mDraggableModule: BaseDraggableModule? = null
    internal var mLoadMoreModule: BaseLoadMoreModule? = null
    private var throttleTime: Long = 1000

    protected lateinit var context: Context
        private set

    @Deprecated("Please use recyclerView", replaceWith = ReplaceWith("recyclerView"))
    lateinit var weakRecyclerView: WeakReference<RecyclerView>

    internal var mRecyclerView: RecyclerView? = null

    var recyclerView: RecyclerView
        set(value) {
            mRecyclerView = value
        }
        get() {
            checkNotNull(mRecyclerView) {
                "Please get it after onAttachedToRecyclerView()"
            }
            return mRecyclerView!!
        }

    /******************************* RecyclerView Method ****************************************/

    init {
        checkModule()
    }

    /**
     * 检查模块
     */
    private fun checkModule() {
        if (this is LoadMoreModule) {
            mLoadMoreModule = this.addLoadMoreModule(this)
        }
        if (this is UpFetchModule) {
            mUpFetchModule = this.addUpFetchModule(this)
        }
        if (this is DraggableModule) {
            mDraggableModule = this.addDraggableModule(this)
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
    protected abstract fun convert(holder: VH, item: T)

    /**
     * Optional implementation this method and use the helper to adapt the view to the given item.
     * If use [payloads], will perform this method, Please implement this method for partial refresh.
     * If use [RecyclerView.Adapter.notifyItemChanged(Int, Object)] with payload,
     * Will execute this method.
     *
     * 可选实现，如果你是用了[payloads]刷新item，请实现此方法，进行局部刷新
     *
     * @param helper   A fully initialized helper.
     * @param item     The item that needs to be displayed.
     * @param payloads payload info.
     */
    protected open fun convert(holder: VH, item: T, payloads: List<Any>) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val baseViewHolder: VH
        when (viewType) {
            LOAD_MORE_VIEW -> {
                val view = mLoadMoreModule!!.loadMoreView.getRootView(parent)
                baseViewHolder = createBaseViewHolder(view)
                mLoadMoreModule!!.setupViewHolder(baseViewHolder)
            }
            HEADER_VIEW -> {
                val headerLayoutVp: ViewParent? = mHeaderLayout.parent
                if (headerLayoutVp is ViewGroup) {
                    headerLayoutVp.removeView(mHeaderLayout)
                }

                baseViewHolder = createBaseViewHolder(mHeaderLayout)
            }
            EMPTY_VIEW -> {
                val emptyLayoutVp: ViewParent? = mEmptyLayout.parent
                if (emptyLayoutVp is ViewGroup) {
                    emptyLayoutVp.removeView(mEmptyLayout)
                }

                baseViewHolder = createBaseViewHolder(mEmptyLayout)
            }
            FOOTER_VIEW -> {
                val footerLayoutVp: ViewParent? = mFooterLayout.parent
                if (footerLayoutVp is ViewGroup) {
                    footerLayoutVp.removeView(mFooterLayout)
                }

                baseViewHolder = createBaseViewHolder(mFooterLayout)
            }
            else -> {
                val viewHolder = onCreateDefViewHolder(parent, viewType)
                bindViewClickListener(viewHolder, viewType)
                mDraggableModule?.initView(viewHolder)
                onItemViewHolderCreated(viewHolder, viewType)
                baseViewHolder = viewHolder
            }
        }

        return baseViewHolder
    }

    /**
     * Don't override this method. If need, please override [getDefItemCount]
     * 不要重写此方法，如果有需要，请重写[getDefItemCount]
     * @return Int
     */
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
            val loadMoreCount = if (mLoadMoreModule?.hasLoadMoreView() == true) {
                1
            } else {
                0
            }
            return headerLayoutCount + getDefItemCount() + footerLayoutCount + loadMoreCount
        }
    }

    /**
     * Don't override this method. If need, please override [getDefItemViewType]
     * 不要重写此方法，如果有需要，请重写[getDefItemViewType]
     *
     * @param position Int
     * @return Int
     */
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
        mUpFetchModule?.autoUpFetch(position)
        //Do not move position, need to change before LoadMoreView binding
        mLoadMoreModule?.autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> {
                mLoadMoreModule?.let {
                    it.loadMoreView.convert(holder, position, it.loadMoreStatus)
                }
            }
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, getItem(position - headerLayoutCount))
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        //Add up fetch logic, almost like load more, but simpler.
        mUpFetchModule?.autoUpFetch(position)
        //Do not move position, need to change before LoadMoreView binding
        mLoadMoreModule?.autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> {
                mLoadMoreModule?.let {
                    it.loadMoreView.convert(holder, position, it.loadMoreStatus)
                }
            }
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, getItem(position - headerLayoutCount), payloads)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Called when a view created by this holder has been attached to a window.
     * simple to solve item will layout using all
     * [setFullSpan]
     *
     * @param holder
     */
    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (isFixedViewType(type)) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        weakRecyclerView = WeakReference(recyclerView)
        mRecyclerView = recyclerView

        this.context = recyclerView.context
        mDraggableModule?.attachToRecyclerView(recyclerView)

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
                            mSpanSizeLookup!!.getSpanSize(manager, type, position - headerLayoutCount)
                    }
                }

            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
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
    open fun getItem(@IntRange(from = 0) position: Int): T {
        return data[position]
    }

    open fun getItemOrNull(@IntRange(from = 0) position: Int): T? {
        return data.getOrNull(position)
    }

    /**
     * 如果返回 -1，表示不存在
     * @param item T?
     * @return Int
     */
    open fun getItemPosition(item: T?): Int {
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
     * set a millisecond,itemClick or itemChildClick Execute only once in tTime millisecond
     *
     * 设置一个毫秒时间tTime ，是的在改时间诶点击事件只响应一次
     */
    fun addThrottleTime(@NonNull tTime: Long) {
        throttleTime = tTime
    }

    /**
     * 绑定 item 点击事件
     * @param viewHolder VH
     */
    protected open fun bindViewClickListener(viewHolder: VH, viewType: Int) {
        mOnItemClickListener?.let {
            with(viewHolder.itemView) {
                clicks()
                        .throttleFirst(throttleTime, TimeUnit.MILLISECONDS)
                        .subscribe {
                            var position = viewHolder.adapterPosition
                            if (position == RecyclerView.NO_POSITION) {
                                return@subscribe
                            }
                            position -= headerLayoutCount
                            setOnItemClick(this, position)
                        }
            }
//            viewHolder.itemView.setOnClickListener { v ->
//                var position = viewHolder.adapterPosition
//                if (position == RecyclerView.NO_POSITION) {
//                    return@setOnClickListener
//                }
//                position -= headerLayoutCount
//                setOnItemClick(v, position)
//            }
        }
        mOnItemLongClickListener?.let {
            viewHolder.itemView.setOnLongClickListener { v ->
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headerLayoutCount
                setOnItemLongClick(v, position)
            }
        }

        mOnItemChildClickListener?.let {
            for (id in getChildClickViewIds()) {
                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.clicks()
                            .throttleFirst(throttleTime, TimeUnit.MILLISECONDS)
                            .subscribe {
                                var position = viewHolder.adapterPosition
                                if (position == RecyclerView.NO_POSITION) {
                                    return@subscribe
                                }
                                position -= headerLayoutCount
                                setOnItemChildClick(childView, position)
                            }

//                            .setOnClickListener { v ->
//                                var position = viewHolder.adapterPosition
//                                if (position == RecyclerView.NO_POSITION) {
//                                    return@setOnClickListener
//                                }
//                                position -= headerLayoutCount
//                                setOnItemChildClick(v, position)
//                            }
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
                        position -= headerLayoutCount
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

    /**
     * （可选重写）当 item 的 ViewHolder创建完毕后，执行此方法。
     * 可在此对 ViewHolder 进行处理，例如进行 DataBinding 绑定 view
     *
     * @param viewHolder VH
     * @param viewType Int
     */
    protected open fun onItemViewHolderCreated(viewHolder: VH, viewType: Int) {}

    /**
     * Override this method and return your data size.
     * 重写此方法，返回你的数据数量。
     */
    protected open fun getDefItemCount(): Int {
        return data.size
    }

    /**
     * Override this method and return your ViewType.
     * 重写此方法，返回你的ViewType。
     */
    protected open fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    /**
     * Override this method and return your ViewHolder.
     * 重写此方法，返回你的ViewHolder。
     */
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
        try {
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
        } catch (e: java.lang.reflect.GenericSignatureFormatError) {
            e.printStackTrace()
        } catch (e: TypeNotPresentException) {
            e.printStackTrace()
        } catch (e: java.lang.reflect.MalformedParameterizedTypeException) {
            e.printStackTrace()
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

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected open fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    /**
     * get the specific view by position,e.g. getViewByPosition(2, R.id.textView)
     *
     * bind [RecyclerView.setAdapter] before use!
     */
    fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
        val recyclerView = mRecyclerView ?: return null
        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as BaseViewHolder?
                ?: return null
        return viewHolder.getViewOrNull(viewId)
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
            val position = headerViewPosition
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
            val position = headerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllHeaderView() {
        if (!hasHeaderLayout()) return

        mHeaderLayout.removeAllViews()
        val position = headerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    val headerViewPosition: Int
        get() {
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
    val headerLayoutCount: Int
        get() {
            return if (hasHeaderLayout()) {
                1
            } else {
                0
            }
        }


    /**
     * 获取头布局
     */
    val headerLayout: LinearLayout?
        get() {
            return if (this::mHeaderLayout.isInitialized) {
                mHeaderLayout
            } else {
                null
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
            val position = footerViewPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    @JvmOverloads
    fun setFooterView(view: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (!this::mFooterLayout.isInitialized || mFooterLayout.childCount <= index) {
            addFooterView(view, index, orientation)
        } else {
            mFooterLayout.removeViewAt(index)
            mFooterLayout.addView(view, index)
            index
        }
    }

    fun removeFooterView(footer: View) {
        if (!hasFooterLayout()) return

        mFooterLayout.removeView(footer)
        if (mFooterLayout.childCount == 0) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllFooterView() {
        if (!hasFooterLayout()) return

        mFooterLayout.removeAllViews()
        val position = footerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    fun hasFooterLayout(): Boolean {
        if (this::mFooterLayout.isInitialized && mFooterLayout.childCount > 0) {
            return true
        }
        return false
    }

    val footerViewPosition: Int
        get() {
            if (hasEmptyView()) {
                var position = 1
                if (headerWithEmptyEnable && hasHeaderLayout()) {
                    position++
                }
                if (footerWithEmptyEnable) {
                    return position
                }
            } else {
                return headerLayoutCount + data.size
            }
            return -1
        }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    val footerLayoutCount: Int
        get() {
            return if (hasFooterLayout()) {
                1
            } else {
                0
            }
        }

    /**
     * 获取脚布局
     * @return LinearLayout?
     */
    val footerLayout: LinearLayout?
        get() {
            return if (this::mFooterLayout.isInitialized) {
                mFooterLayout
            } else {
                null
            }
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
        } else {
            emptyView.layoutParams?.let {
                val lp = mEmptyLayout.layoutParams
                lp.width = it.width
                lp.height = it.height
                mEmptyLayout.layoutParams = lp
            }
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
        mRecyclerView?.let {
            val view = LayoutInflater.from(it.context).inflate(layoutResId, it, false)
            setEmptyView(view)
        }
    }

    fun removeEmptyView() {
        if (this::mEmptyLayout.isInitialized) {
            mEmptyLayout.removeAllViews()
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
    val emptyLayout: FrameLayout?
        get() {
            return if (this::mEmptyLayout.isInitialized) {
                mEmptyLayout
            } else {
                null
            }
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
                val animation: BaseAnimation = adapterAnimation ?: AlphaInAnimation()
                animation.animators(holder.itemView).forEach {
                    startAnim(it, holder.layoutPosition)
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
     * @param index
     */
    protected open fun startAnim(anim: Animator, index: Int) {
        anim.start()
    }

    /**
     * 内置默认动画类型
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
     * 设置新的数据实例
     *
     * @param data
     */
    @Deprecated("Please use setNewInstance(), This method will be removed in the next version", replaceWith = ReplaceWith("setNewInstance(data)"))
    open fun setNewData(data: MutableList<T>?) {
        setNewInstance(data)
    }

    /**
     * setting up a new instance to data;
     * 设置新的数据实例，替换原有内存引用。
     * 通常情况下，如非必要，请使用[setList]修改内容
     *
     * @param list
     */
    open fun setNewInstance(list: MutableList<T>?) {
        if (list === this.data) {
            return
        }

        this.data = list ?: arrayListOf()
        mLoadMoreModule?.reset()
        mLastPosition = -1
        notifyDataSetChanged()
        mLoadMoreModule?.checkDisableLoadMoreIfNotFullPage()
    }

    /**
     * use data to replace all item in mData. this method is different [setList],
     * it doesn't change the [BaseQuickAdapter.data] reference
     * Deprecated, Please use [setList]
     *
     * @param newData data collection
     */
    @Deprecated("Please use setData()", replaceWith = ReplaceWith("setData(newData)"))
    open fun replaceData(newData: Collection<T>) {
        setList(newData)
    }

    /**
     * 使用新的数据集合，改变原有数据集合内容。
     * 注意：不会替换原有的内存引用，只是替换内容
     *
     * @param list Collection<T>?
     */
    open fun setList(list: Collection<T>?) {
        if (list !== this.data) {
            this.data.clear()
            if (!list.isNullOrEmpty()) {
                this.data.addAll(list)
            }
        } else {
            if (!list.isNullOrEmpty()) {
                val newList = ArrayList(list)
                this.data.clear()
                this.data.addAll(newList)
            } else {
                this.data.clear()
            }
        }
        mLoadMoreModule?.reset()
        mLastPosition = -1
        notifyDataSetChanged()
        mLoadMoreModule?.checkDisableLoadMoreIfNotFullPage()
    }

    /**
     * change data
     * 改变某一位置数据
     */
    open fun setData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.data.size) {
            return
        }
        this.data[index] = data
        notifyItemChanged(index + headerLayoutCount)
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    open fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyItemInserted(position + headerLayoutCount)
        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     * 添加一条新数据
     */
    open fun addData(@NonNull data: T) {
        this.data.add(data)
        notifyItemInserted(this.data.size + headerLayoutCount)
        compatibilityDataSizeChanged(1)
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    open fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        this.data.addAll(position, newData)
        notifyItemRangeInserted(position + headerLayoutCount, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    open fun addData(@NonNull newData: Collection<T>) {
        this.data.addAll(newData)
        notifyItemRangeInserted(this.data.size - newData.size + headerLayoutCount, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    @Deprecated("Please use removeAt()", replaceWith = ReplaceWith("removeAt(position)"))
    open fun remove(@IntRange(from = 0) position: Int) {
        removeAt(position)
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    open fun removeAt(@IntRange(from = 0) position: Int) {
        if (position >= data.size) {
            return
        }
        this.data.removeAt(position)
        val internalPosition = position + headerLayoutCount
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, this.data.size - internalPosition)
    }

    open fun remove(data: T) {
        val index = this.data.indexOf(data)
        if (index == -1) {
            return
        }
        removeAt(index)
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

    @Deprecated("User getDiffer()", replaceWith = ReplaceWith("getDiffer()"))
    fun getDiffHelper(): BrvahAsyncDiffer<T> {
        return getDiffer()
    }

    fun getDiffer(): BrvahAsyncDiffer<T> {
        checkNotNull(mDiffHelper) {
            "Please use setDiffCallback() or setDiffConfig() first!"
        }
        return mDiffHelper!!
    }

    /**
     * 使用 Diff 设置新实例.
     * 此方法为异步Diff，无需考虑性能问题.
     * 使用之前请先设置 [setDiffCallback] 或者 [setDiffConfig].
     *
     * Use Diff setting up a new instance to data.
     * This method is asynchronous.
     *
     * @param list MutableList<T>?
     */
    open fun setDiffNewData(list: MutableList<T>?) {
        if (hasEmptyView()) {
            // If the current view is an empty view, set the new data directly without diff
            setNewInstance(list)
            return
        }
        mDiffHelper?.submitList(list)
    }

    /**
     * 使用 DiffResult 设置新实例.
     * Use DiffResult setting up a new instance to data.
     *
     * @param diffResult DiffResult
     * @param list New Data
     */
    open fun setDiffNewData(@NonNull diffResult: DiffUtil.DiffResult, list: MutableList<T>) {
        if (hasEmptyView()) {
            // If the current view is an empty view, set the new data directly without diff
            setNewInstance(list)
            return
        }
        diffResult.dispatchUpdatesTo(BrvahListUpdateCallback(this))
        this.data = list
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
