package com.chad.baserecyclerviewadapterhelper.data

import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.entity.DiffEntity
import com.chad.baserecyclerviewadapterhelper.entity.Status

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
object DataServer {
    const val HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK =
        "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"
    const val CYM_CHAD = "CymChad"
    const val CHAY_CHAN = "ChayChan"
    fun getSampleData(lenth: Int): MutableList<Status> {
        val list: MutableList<Status> = ArrayList()
        for (i in 0 until lenth) {
            val status = Status()
            status.userName = "Chad$i"
            status.createdAt = "04/05/$i"
            status.isRetweet = i % 2 == 0


            status.userAvatar = when (i % 3) {
                0 -> R.mipmap.animation_img1
                1 -> R.mipmap.animation_img2
                else -> R.mipmap.animation_img3
            }
            status.text = "BaseRecyclerViewAdpaterHelper https://www.recyclerview.org"
            list.add(status)
        }
        return list
    }



    val strData: List<String>
        get() {
            val list: MutableList<String> = ArrayList()
            for (i in 0..19) {
                var str = HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK
                if (i % 2 == 0) {
                    str = CYM_CHAD
                }
                list.add(str)
            }
            return list
        }
    @JvmStatic
    val diffUtilDemoEntities: List<DiffEntity>
        get() {
            val list: MutableList<DiffEntity> = ArrayList()
            for (i in 0..9) {
                list.add(
                    DiffEntity(
                        i,
                        "Item $i",
                        "This item $i content",
                        "06-12"
                    )
                )
            }
            return list
        }
}