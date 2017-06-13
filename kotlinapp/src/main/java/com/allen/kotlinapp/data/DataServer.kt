package com.allen.kotlinapp.data

import com.allen.kotlinapp.entity.MultipleItem
import com.allen.kotlinapp.entity.MySection
import com.allen.kotlinapp.entity.Status
import com.allen.kotlinapp.entity.Video
import java.util.*

/**
 * 文 件 名: DataServer
 * 创 建 人: Allen
 * 创建日期: 2017/6/13 14:04
 * 修改时间：
 * 修改备注：
 */
open class DataServer private constructor(){

    companion object{
        private val HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK = "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"
        private val CYM_CHAD = "CymChad"
        fun getSampleData(lenth: Int): List<Status> {
            val list = ArrayList<Status>()
            for (i in 0..lenth - 1) {
                val status = Status()
                status.userName=("Chad" + i)
                status.createdAt=("04/05/" + i)
                status.isRetweet=(i % 2 == 0)
                status.userAvatar=("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460")
                status.text=("BaseRecyclerViewAdpaterHelper https://www.recyclerview.org")
                list.add(status)
            }
            return list
        }

        fun addData(list: MutableList<Status>, dataSize: Int): List<Status> {
            for (i in 0..dataSize - 1) {
                val status = Status()
                status.userName=("Chad" + i)
                status.createdAt=("04/05/" + i)
                status.isRetweet=(i % 2 == 0)
                status.userAvatar=("https://avatars1.githubusercontent.com/u/7698209?v=3&s=460")
                status.text=("Powerful and flexible RecyclerAdapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper")
                list.add(status)
            }

            return list
        }

        fun getSampleData(): List<MySection> {
            val list = ArrayList<MySection>()
            list.add(MySection(true, "Section 1", true))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 2", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 3", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 4", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 5", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            return list
        }

        fun getStrData(): List<String> {
            val list = ArrayList<String>()
            for (i in 0..19) {
                var str = HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK
                if (i % 2 == 0) {
                    str = CYM_CHAD
                }
                list.add(str)
            }
            return list
        }

        fun getMultipleItemData(): List<MultipleItem> {
            val list = ArrayList<MultipleItem>()
            for (i in 0..4) {
                list.add(MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE))
                list.add(MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, CYM_CHAD))
                list.add(MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE))
                list.add(MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN))
                list.add(MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN))
            }

            return list
        }

    }



}
