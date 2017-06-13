package com.allen.kotlinapp.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import com.allen.kotlinapp.BR
import com.allen.kotlinapp.R
import com.allen.kotlinapp.entity.Movie
import com.allen.kotlinapp.entity.MoviePresenter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 文 件 名: DataBindingUseAdapter
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:39
 * 修改时间：
 * 修改备注：
 */
class DataBindingUseAdapter(layoutResId: Int, data: List<Movie>) : BaseQuickAdapter<Movie, DataBindingUseAdapter.MovieViewHolder>(layoutResId, data) {

    private val mPresenter: MoviePresenter

    init {

        mPresenter = MoviePresenter()
    }

    override fun convert(helper: MovieViewHolder, item: Movie) {
        val binding = helper.binding
        binding.setVariable(BR.movie, item)
        binding.setVariable(BR.presenter, mPresenter)
        binding.executePendingBindings()
        when (helper.layoutPosition % 2) {
            0 -> helper.setImageResource(R.id.iv, R.mipmap.m_img1)
            1 -> helper.setImageResource(R.id.iv, R.mipmap.m_img2)
        }
    }

    /*  @Override
    protected MovieViewHolder createBaseViewHolder(View view) {
        return new MovieViewHolder(view);
    }
*/
    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false) ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    inner class MovieViewHolder(view: View) : BaseViewHolder(view) {

        val binding: ViewDataBinding
            get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }
}
