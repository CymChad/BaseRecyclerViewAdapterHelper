package com.chad.library.adapter.base.entity.node

import java.util.*

abstract class BaseNode {
    abstract val childNode: MutableList<BaseNode>?



    open fun nodeTag() : String = UUID.randomUUID().toString()


}