package com.allen.kotlinapp.entity

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * 文 件 名: MySection
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 14:18
 * 修改时间：
 * 修改备注：
 */
class MySection : SectionEntity<Video> {
    var isMore: Boolean = false

    constructor(isHeader: Boolean, header: String, isMroe: Boolean) : super(isHeader, header) {
        this.isMore = isMroe
    }

    constructor(t: Video) : super(t) {}
}
