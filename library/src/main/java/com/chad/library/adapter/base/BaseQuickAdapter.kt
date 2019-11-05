package com.chad.library.adapter.base

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.loadmore.OnLoadMoreListener
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.chad.library.adapter.base.util.getItemView
import java.lang.ref.WeakReference
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * Base Class
 * @param T : type of data, 数据类型
 * @param VH : BaseViewHolder
 */
abstract class BaseQuickAdapter<T, VH : BaseViewHolder>(@LayoutRes val layoutResId: Int = 0,
                                                        data: MutableList<T>? = null)
    : RecyclerView.Adapter<VH>() {

    companion object {
        const val HEADER_VIEW = 0x00000111
        const val LOAD_MORE_VIEW = 0x00000222
        const val FOOTER_VIEW = 0x00000333
        const val EMPTY_VIEW = 0x00000555

        private var mLoadMoreView: BaseLoadMoreView = SimpleLoadMoreView()

        @JvmStatic
        fun setDefLoadMoreView(loadMoreView: BaseLoadMoreView) {
            mLoadMoreView = loadMoreView
        }
    }

    // constructor
    constructor(data: MutableList<T>?) : this(0, data)

    init {

    }

    /** data 数据 */
    var data: MutableList<T> = data ?: arrayListOf()
        private set
    /** 当显示空布局时，是否显示 Header */
    var headerWithEmptyEnable = false
    /** 当显示空布局时，是否显示 Foot */
    var footerWithEmptyEnable = false
    /** 是否使用空布局 */
    var isUseEmpty = true
    /** 加载完成后是否允许点击 */
    var enableLoadMoreEndClick = false
    /** 是否打开自动加载更多 */
    var isAutoLoadMore = true

    val isEnableLoadMoreIfNotFullPage = true

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    var headerViewAsFlow: Boolean = false
    var footerViewAsFlow: Boolean = false

    private lateinit var mHeaderLayout: LinearLayout
    private lateinit var mFooterLayout: LinearLayout
    private lateinit var mEmptyLayout: FrameLayout

    private var mSpanSizeLookup: SpanSizeLookup? = null

    private var mLoadMoreListener: OnLoadMoreListener? = null
    private var mNextLoadEnable = false
    private var mLoading = false
    private var mPreLoadNumber = 1
    private var mLastPosition = -1

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnItemChildClickListener: OnItemChildClickListener? = null
    private var mOnItemChildLongClickListener: OnItemChildLongClickListener? = null

    protected lateinit var context: Context
        private set

    protected lateinit var weakRecyclerView: WeakReference<RecyclerView>

    /******************************* RecyclerView Method ****************************************/

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun convert(helper: VH, item: T?)

    protected open fun convert(helper: VH, item: T?, payloads: List<Any>) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        this.context = parent.context
        val baseViewHolder: VH
        when (viewType) {
            LOAD_MORE_VIEW -> baseViewHolder = getLoadMoreViewHolder(parent)
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
                baseViewHolder = onCreateDefViewHolder(parent, viewType)
                bindViewClickListener(baseViewHolder)
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
            val loadMoreCount = if (hasLoadMoreView()) {
                1
            } else {
                0
            }
            return getHeaderLayoutCount() + data.size + getFooterLayoutCount() + loadMoreCount
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
        //Do not move position, need to change before LoadMoreView binding
        autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> mLoadMoreView.convert(holder)
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, getItem(position - getHeaderLayoutCount()))
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        //Do not move position, need to change before LoadMoreView binding
        autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> mLoadMoreView.convert(holder)
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, getItem(position - getHeaderLayoutCount()), payloads)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        weakRecyclerView = WeakReference(recyclerView)

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
                            mSpanSizeLookup!!.invoke(manager, position - getHeaderLayoutCount())
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
        return if (position >= 0 && position < data.size)
            data[position]
        else
            null
    }

    private val childClickViewIds = LinkedHashSet<Int>()

    fun getChildClickViewIds(): LinkedHashSet<Int> {
        return childClickViewIds
    }

    protected fun addItemChildClick(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childClickViewIds.add(viewId)
        }
    }

    /**
     * 绑定 item 点击事件
     * @param baseViewHolder VH
     */
    protected open fun bindViewClickListener(baseViewHolder: VH) {
        mOnItemClickListener?.let {
            baseViewHolder.itemView.setOnClickListener { v ->
                var position = baseViewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= getHeaderLayoutCount()
                setOnItemClick(v, position)
            }
        }
        mOnItemLongClickListener?.let {
            baseViewHolder.itemView.setOnLongClickListener { v ->
                var position = baseViewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= getHeaderLayoutCount()
                setOnItemLongClick(v, position)
            }
        }

        mOnItemChildClickListener?.let {
            for (id in getChildClickViewIds()) {
                baseViewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.setOnClickListener { v ->
                        var position = baseViewHolder.adapterPosition
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
            for (id in getChildClickViewIds()) {
                baseViewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isLongClickable) {
                        childView.isLongClickable = true
                    }
                    childView.setOnLongClickListener { v ->
                        var position = baseViewHolder.adapterPosition
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
     * @param v
     * @param position
     */
    open fun setOnItemClick(v: View, position: Int) {
        mOnItemClickListener?.invoke(this, v, position)
    }

    /**
     * override this method if you want to override longClick event logic
     *
     * @param v
     * @param position
     * @return
     */
    open fun setOnItemLongClick(v: View, position: Int): Boolean {
        return mOnItemLongClickListener?.invoke(this, v, position) ?: false
    }

    open fun setOnItemChildClick(v: View, position: Int) {
        mOnItemChildClickListener?.invoke(this, v, position)
    }

    open fun setOnItemChildLongClick(v: View, position: Int): Boolean {
        return mOnItemChildLongClickListener?.invoke(this, v, position) ?: false
    }

    protected open fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    protected open fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createBaseViewHolder(parent, layoutResId)
    }

    protected open fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): VH {
        return createBaseViewHolder(parent.getItemView(layoutResId))
    }

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
            createGenericKInstance(z, view)
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
    private fun createGenericKInstance(z: Class<*>, view: View): VH? {
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
        }
        mHeaderLayout.orientation = orientation
        mHeaderLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
            ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        } else {
            ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
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
        //Return to header view notify position
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

    /********************************************************************************************/
    /********************************* FooterView Method ****************************************/
    /********************************************************************************************/
    @JvmOverloads
    fun addFooterView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (!this::mFooterLayout.isInitialized) {
            mFooterLayout = LinearLayout(view.context)
        }
        mFooterLayout.orientation = orientation
        mFooterLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
            ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        } else {
            ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
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
    private fun getFooterLayoutCount(): Int =
            if (hasFooterLayout()) {
                1
            } else {
                0
            }

    /********************************************************************************************/
    /********************************** EmptyView Method ****************************************/
    /********************************************************************************************/
    fun setEmptyView(emptyView: View) {
        val oldItemCount = itemCount
        var insert = false
        if (!this::mEmptyLayout.isInitialized) {
            mEmptyLayout = FrameLayout(emptyView.context)
            val layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            emptyView.layoutParams?.let {
                layoutParams.width = it.width
                layoutParams.height = it.height
            }
            mEmptyLayout.layoutParams = layoutParams
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

    fun getEmptyView(): View? = if (this::mEmptyLayout.isInitialized) {
        mEmptyLayout
    } else {
        null
    }

    /********************************************************************************************/
    /******************************* LoadMoreView Method ****************************************/
    /********************************************************************************************/

    private fun getLoadMoreViewHolder(parent: ViewGroup): VH {
        val view = mLoadMoreView.getRootView(parent)
        val vh = createBaseViewHolder(view)
        vh.itemView.setOnClickListener {
            if (mLoadMoreView.loadMoreStatus == BaseLoadMoreView.Status.Fail) {
                loadMoreToLoading()
            } else if (mLoadMoreView.loadMoreStatus == BaseLoadMoreView.Status.Complete && !isAutoLoadMore)  {
                loadMoreToLoading()
            } else if (enableLoadMoreEndClick && mLoadMoreView.loadMoreStatus == BaseLoadMoreView.Status.End) {
                loadMoreToLoading()
            }
        }
        return vh
    }

    /**
     * The notification starts the callback and loads more
     */
    fun loadMoreToLoading() {
        if (mLoadMoreView.loadMoreStatus == BaseLoadMoreView.Status.Loading) {
            return
        }
        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Loading
//        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Complete
        notifyItemChanged(getLoadMoreViewPosition())
        invokeLoadMoreListener()
    }

    /**
     * Gets to load more locations
     *
     * @return
     */
    fun getLoadMoreViewPosition(): Int {
        return getHeaderLayoutCount() + data.size + getFooterLayoutCount()
    }

    var loadMoreEnable = false
        set(value) {
            val oldHasLoadMore = hasLoadMoreView()
            field = value
            val newHasLoadMore = hasLoadMoreView()

            if (oldHasLoadMore) {
                if (!newHasLoadMore) {
                    notifyItemRemoved(getLoadMoreViewPosition())
                }
            } else {
                if (newHasLoadMore) {
                    mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Complete
                    notifyItemInserted(getLoadMoreViewPosition())
                }
            }
        }

    fun hasLoadMoreView(): Boolean {
        if (mLoadMoreListener == null || !loadMoreEnable) {
            return false
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone) {
            return false
        }
        return data.isNotEmpty()
    }

    fun setPreLoadNumber(preLoadNumber: Int) {
        if (preLoadNumber > 1) {
            mPreLoadNumber = preLoadNumber
        }
    }

    private fun autoLoadMore(position: Int) {
        if (!isAutoLoadMore) {
            //如果不需要自动加载更多，直接返回
            return
        }
        if (!hasLoadMoreView()) {
            return
        }
        if (position < itemCount - mPreLoadNumber) {
            return
        }
        if (mLoadMoreView.loadMoreStatus != BaseLoadMoreView.Status.Complete) {
            return
        }
        invokeLoadMoreListener()
    }

    private fun invokeLoadMoreListener() {
        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Loading
        if (!mLoading) {
            mLoading = true
            weakRecyclerView.get()?.let {
                it.post { mLoadMoreListener?.invoke() }
            } ?: mLoadMoreListener?.invoke()
        }
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        this.mLoadMoreListener = listener
        mNextLoadEnable = true
        loadMoreEnable = true
        mLoading = false
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    @JvmOverloads
    fun loadMoreEnd(gone: Boolean = false) {
        if (!hasLoadMoreView()) {
            return
        }
        mLoading = false
        mNextLoadEnable = false
        mLoadMoreView.isLoadEndMoreGone = gone

        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.End
        if (gone) {
            notifyItemRemoved(getLoadMoreViewPosition())
        } else {
            notifyItemChanged(getLoadMoreViewPosition())
        }
    }

    /**
     * Refresh complete
     */
    fun loadMoreComplete() {
        if (!hasLoadMoreView()) {
            return
        }
        mLoading = false
        mNextLoadEnable = true
        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Complete
        notifyItemChanged(getLoadMoreViewPosition())
    }

    /**
     * Refresh failed
     */
    fun loadMoreFail() {
        if (!hasLoadMoreView()) {
            return
        }
        mLoading = false
        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Fail
        notifyItemChanged(getLoadMoreViewPosition())
    }


    /******* 设置数据相关 *******/


    fun setNewData(data: MutableList<T>?) {
        this.data = data ?: arrayListOf()
        if (mLoadMoreListener != null) {
            mNextLoadEnable = true
            loadMoreEnable = true
            mLoading = false
            mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Complete
        }
        mLastPosition = -1
        notifyDataSetChanged()
    }

    /**
     * add one new data in to certain location
     *
     * @param position
     */
    fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyItemInserted(position + getHeaderLayoutCount())
        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     */
    fun addData(@NonNull data: T) {
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
    fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        this.data.addAll(position, newData)
        notifyItemRangeInserted(position + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    fun addData(@NonNull newData: Collection<T>) {
        this.data.addAll(newData)
        notifyItemRangeInserted(this.data.size - newData.size + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    fun remove(@IntRange(from = 0) position: Int) {
        this.data.removeAt(position)
        val internalPosition = position + getHeaderLayoutCount()
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, this.data.size - internalPosition)
    }

    /**
     * change data
     */
    fun setData(@IntRange(from = 0) index: Int, data: T) {
        this.data[index] = data
        notifyItemChanged(index + getHeaderLayoutCount())
    }

    /**
     * use data to replace all item in mData. this method is different [setNewData],
     * it doesn't change the [BaseQuickAdapter.data] reference
     *
     * @param newData data collection
     */
    fun replaceData(newData: Collection<T>) {
        // 不是同一个引用才清空列表
        if (newData !== this.data) {
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
    private fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }

    //TODO disableLoadMoreIfNotFullPage
    fun disableLoadMoreIfNotFullPage() {

    }

    private fun isFullScreen(llm: LinearLayoutManager): Boolean {
        return (llm.findLastCompletelyVisibleItemPosition() + 1) != itemCount ||
                llm.findFirstCompletelyVisibleItemPosition() != 0
    }

    fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
        this.mSpanSizeLookup = spanSizeLookup
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.mOnItemLongClickListener = listener
    }

    fun setOnItemChildClickListener(listener: OnItemChildClickListener) {
        this.mOnItemChildClickListener = listener
    }

    fun setOnItemChildLongClickListener(listener: OnItemChildLongClickListener) {
        this.mOnItemChildLongClickListener = listener
    }

    fun getOnItemClickListener(): OnItemClickListener? = mOnItemClickListener

    fun getOnItemLongClickListener(): OnItemLongClickListener? = mOnItemLongClickListener

    fun getOnItemChildClickListener(): OnItemChildClickListener? = mOnItemChildClickListener

    fun getOnItemChildLongClickListener(): OnItemChildLongClickListener? = mOnItemChildLongClickListener
}

interface B {
    class  A {

    }

    fun getA(): A {
        return A()
    }
}

typealias SpanSizeLookup = (gridLayoutManager: GridLayoutManager, position: Int) -> Int

typealias OnItemClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit

typealias OnItemLongClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean

typealias OnItemChildClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit

typealias OnItemChildLongClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean