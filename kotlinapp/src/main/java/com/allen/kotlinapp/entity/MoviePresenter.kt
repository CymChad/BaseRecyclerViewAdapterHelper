package com.allen.kotlinapp.entity

import android.view.View
import android.widget.Toast

/**
 * 文 件 名: MoviePresenter
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 15:14
 * 修改时间：
 * 修改备注：
 */
class MoviePresenter {
    fun buyTicket(view: View, movie: Movie) {
        Toast.makeText(view.context, "buy ticket: " + movie.name, Toast.LENGTH_SHORT).show()
    }
}
