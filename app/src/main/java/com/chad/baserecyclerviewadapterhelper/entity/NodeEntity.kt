package com.chad.baserecyclerviewadapterhelper.entity

data class NodeEntity(
    val title: String,
    val childList: List<FirstNode>
) {
    data class FirstNode(
        val content: String,
        val childList: List<SecondNode>
    ) {
        data class SecondNode(
            val content: String
        )
    }
}