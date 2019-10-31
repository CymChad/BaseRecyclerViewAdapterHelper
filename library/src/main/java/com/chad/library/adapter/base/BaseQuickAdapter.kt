package com.chad.library.adapter.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter.*
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

    /** data 数据 */
    var data: List<T> = data ?: arrayListOf()
        private set
    /** 当显示空布局时，是否显示 Header */
    var headerWithEmptyEnable = false
    /** 当显示空布局时，是否显示 Foot */
    var footWithEmptyEnable = false

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

    override fun getItemCount(): Int {
        var count: Int
        if (1 == getEmptyViewCount()) {
            count = 1
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++
            }
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++
            }
        } else {
            count = getHeaderLayoutCount() + data.size + getFooterLayoutCount() + getLoadMoreViewCount()
        }
        return count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        this.context = parent.context
        val baseViewHolder: VH
        when (viewType) {
            LOADING_VIEW -> baseViewHolder = getLoadingView(parent)
            HEADER_VIEW -> {
                val headerLayoutVp = mHeaderLayout.getParent()
                if (headerLayoutVp is ViewGroup) {
                    (headerLayoutVp as ViewGroup).removeView(mHeaderLayout)
                }

                baseViewHolder = createBaseViewHolder(mHeaderLayout)
            }
            EMPTY_VIEW -> {
                val emptyLayoutVp = mEmptyLayout.getParent()
                if (emptyLayoutVp is ViewGroup) {
                    (emptyLayoutVp as ViewGroup).removeView(mEmptyLayout)
                }

                baseViewHolder = createBaseViewHolder(mEmptyLayout)
            }
            FOOTER_VIEW -> {
                val footerLayoutVp = mFooterLayout.getParent()
                if (footerLayoutVp is ViewGroup) {
                    (footerLayoutVp as ViewGroup).removeView(mFooterLayout)
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

    protected fun onCreateDefViewHolder(parent: ViewGroup): VH {
        return createBaseViewHolder(parent, layoutResId)
    }

    protected fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): VH {
        return createBaseViewHolder(context.getItemView(layoutResId, parent))
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

    /**
     *
     * @receiver Context
     * @param layoutResId ID for an XML layout resource to load
     * @param parent Optional view to be the parent of the generated hierarchy or else simply an object that
     *               provides a set of LayoutParams values for root of the returned
     *               hierarchy
     * @return View
     */
    protected fun Context.getItemView(@LayoutRes layoutResId: Int, parent: ViewGroup): View {
        return LayoutInflater.from(this).inflate(layoutResId, parent, false)
    }

    fun hasEmptyView(): Boolean {

    }
}

