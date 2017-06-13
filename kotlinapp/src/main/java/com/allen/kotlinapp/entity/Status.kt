package com.allen.kotlinapp.entity

/**
 * 文 件 名: Status
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 14:02
 * 修改时间：
 * 修改备注：
 */

class Status {
    var isRetweet: Boolean = false
    var text: String? = null
    var userName: String? = null
    var userAvatar: String? = null
    var createdAt: String? = null

    override fun toString(): String {
        return "Status{" +
                "isRetweet=" + isRetweet +
                ", text='" + text + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}'
    }
}
