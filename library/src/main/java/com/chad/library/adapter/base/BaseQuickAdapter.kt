package com.chad.library.adapter.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.chad.library.adapter.base.util.getItemView
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * Base Class
 * @param T : type of data, 数据类型
 * @param VH : BaseViewHolder
 */
abstract class BaseQuickAdapter2<T, VH : BaseViewHolder>(@LayoutRes val layoutResId: Int = 0,
                                                         data: List<T>? = null)
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

    /** data 数据 */
    var data: List<T> = data ?: arrayListOf()
        private set
    /** 当显示空布局时，是否显示 Header */
    var headerWithEmptyEnable = false
    /** 当显示空布局时，是否显示 Foot */
    var footerWithEmptyEnable = false
    /** 是否使用空布局 */
    var isUseEmpty = true
    /** 加载完成后是否允许点击 */
    var enableLoadMoreEndClick = false

    private lateinit var mHeaderLayout: LinearLayout
    private lateinit var mFooterLayout: LinearLayout
    private lateinit var mEmptyLayout: FrameLayout

    private var mLoadMoreListener: OnLoadMoreListener? = null
    private var mNextLoadEnable = false
    private var mLoading = false
    private var mPreLoadNumber = 1

    protected lateinit var context: Context
        private set

    protected var recyclerView: RecyclerView? = null
        private set
        get() {
            checkNotNull(field) { "please bind recyclerView first!" }
            return field
        }

    fun bindToRecyclerView(recyclerView: RecyclerView) {
        check(this.recyclerView != recyclerView) { "Don't bind twice" }
        this.recyclerView = recyclerView
        this.recyclerView!!.adapter = this
    }

    /******************************* RecyclerView Method ****************************************/

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun convert(helper: VH, item: T?)

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
                baseViewHolder = onCreateDefViewHolder(parent)
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
        when(holder.itemViewType) {
            LOAD_MORE_VIEW -> mLoadMoreView.convert(holder)
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, getItem(position - getHeaderLayoutCount()))
        }
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
            data.get(position)
        else
            null
    }

    protected fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    protected fun onCreateDefViewHolder(parent: ViewGroup): VH {
        return createBaseViewHolder(parent, layoutResId)
    }

    protected fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): VH {
        return createBaseViewHolder(parent.getItemView(layoutResId))
    }

    @Suppress("UNCHECKED_CAST")
    protected fun createBaseViewHolder(view: View): VH {
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

//    /**
//     *
//     * @receiver Context
//     * @param layoutResId ID for an XML layout resource to load
//     * @param parent Optional view to be the parent of the generated hierarchy or else simply an object that
//     *               provides a set of LayoutParams values for root of the returned
//     *               hierarchy
//     * @return View
//     */
//    protected fun Context.getItemView(@LayoutRes layoutResId: Int, parent: ViewGroup): View {
//        return LayoutInflater.from(this).inflate(layoutResId, parent, false)
//    }

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
    private fun getHeaderLayoutCount(): Int =
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
            val headerCount = if (hasHeaderLayout()) {
                1
            } else {
                0
            }
            return headerCount + data.size
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
            if (headerWithEmptyEnable && hasEmptyView()) {
                position++
            }
            if (itemCount > oldItemCount) {
                notifyItemInserted(position)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun setEmptyView(layoutResId: Int, context: Context) {
        val view = LayoutInflater.from(context).inflate(layoutResId, null, false)
        setEmptyView(view)
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
                notifyLoadMoreToLoading()
            } else if (enableLoadMoreEndClick && mLoadMoreView.loadMoreStatus == BaseLoadMoreView.Status.End) {
                notifyLoadMoreToLoading()
            }
        }
        return vh
    }

    /**
     * The notification starts the callback and loads more
     */
    fun notifyLoadMoreToLoading() {
        if (mLoadMoreView.loadMoreStatus == BaseLoadMoreView.Status.Loading) {
            return
        }
        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Default
        notifyItemChanged(getLoadMoreViewPosition())
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
            val oldHasLoadMoret = hasLoadMoreView()
            field = value
            val newHasLoadMore = hasLoadMoreView()

            if (oldHasLoadMoret) {
                if (!newHasLoadMore) {
                    notifyItemRemoved(getLoadMoreViewPosition())
                }
            } else {
                if (newHasLoadMore) {
                    mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Default
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
        if (!hasLoadMoreView()) {
            return
        }
        if (position < itemCount - mPreLoadNumber) {
            return
        }
        if (mLoadMoreView.loadMoreStatus != BaseLoadMoreView.Status.Default) {
            return
        }
        mLoadMoreView.loadMoreStatus = BaseLoadMoreView.Status.Loading
        if (!mLoading) {
            mLoading = true
            mLoadMoreListener?.invoke()
        }
    }


    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        this.mLoadMoreListener = listener
        mNextLoadEnable = true
        loadMoreEnable = true
        mLoading = false
    }

    //TODO disableLoadMoreIfNotFullPage

}
typealias OnLoadMoreListener = () -> Unit
