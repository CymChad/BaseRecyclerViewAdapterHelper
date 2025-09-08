package com.chad.baserecyclerviewadapterhelper.entity

/**
 * @author LiMuYang
 * @date 2025/9/5
 * @description
 */
data class NodeEntity(
    val title: String,
    val childNode: List<Level2NodeEntity>?,
) {

    data class Level2NodeEntity(
        val title: String,
        val childNode: List<Level3NodeEntity>?,
    ) {

        data class Level3NodeEntity(
            val title: String,
        )
    }
}