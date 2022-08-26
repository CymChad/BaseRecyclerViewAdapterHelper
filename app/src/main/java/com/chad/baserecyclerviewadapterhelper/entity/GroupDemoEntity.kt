package com.chad.baserecyclerviewadapterhelper.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupDemoEntity(
    @Json(name = "group_name")
    val groupName: String,
    @Json(name = "group_list")
    val groupList: List<Group>,
) {

    @JsonClass(generateAdapter = true)
    data class Group(
        @Json(name = "title")
        val title: String,
        @Json(name = "content")
        val content: String,
    )
}