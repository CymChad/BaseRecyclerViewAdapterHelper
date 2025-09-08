package com.chad.baserecyclerviewadapterhelper.activity.scene

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.baserecyclerviewadapterhelper.R
import com.chad.baserecyclerviewadapterhelper.activity.scene.adapter.GroupAdapter
import com.chad.baserecyclerviewadapterhelper.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.baserecyclerviewadapterhelper.entity.GroupDemoEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import androidx.core.graphics.drawable.toDrawable


class GroupDemoActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    // 创建一个 ConcatAdapter，用来包裹 GroupAdapter
    private val concatAdapter = ConcatAdapter()


    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.titleBar) { view, insets ->
            val bar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bar.top)
            insets
        }

        window.setBackgroundDrawable(ContextCompat.getColor(this, R.color.bg).toDrawable())

        viewBinding.titleBar.title = "Group Scene（ConcatAdapter）"
        viewBinding.titleBar.setOnBackListener { finish() }

        viewBinding.rv.layoutManager = LinearLayoutManager(this)
        viewBinding.rv.adapter = concatAdapter

        /*
        往常，我们获得这样一个 List 嵌套 List 的结构时，需要展平数据成一个 List<Any>，
        然后用一个 Adapter 通过不同的ItemViewType 去做，非常繁琐，容易出错，而且不方便数据刷新

        现在我们不需要去对数据做处理了，直接使用！
         */
        val list: List<GroupDemoEntity> = jsonToList()


        // 循环 list，有多少个数组，就创建多少个 GroupAdapter，就这么简单
        list.forEach {
            val adapter = GroupAdapter()
            adapter.submitList(it.groupList)

            // 创建好以后，直接扔进 ConcatAdapter
            concatAdapter.addAdapter(adapter)
        }
    }

    /**
     * 解析 Json，不用关注
     */
    private fun jsonToList(): List<GroupDemoEntity> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, GroupDemoEntity::class.java)
        val jsonAdapter: JsonAdapter<List<GroupDemoEntity>> = moshi.adapter(type)
        return jsonAdapter.fromJson(JSON) ?: throw IllegalStateException("json 解析出错")
    }

    companion object {
        // 此种格式的json，在业务场景中比较常见
        private const val JSON = """
[{
		"group_name": "1",
		"group_list": [{
				"title": "patton",
				"content": "this is content"
			},
			{
				"title": "nicole",
				"content": "this is content"
			},
			{
				"title": "anthony",
				"content": "this is content"
			}
		]
	},
	{
		"group_name": "2",
		"group_list": [{
				"title": "zane",
				"content": "this is content"
			},
			{
				"title": "venus",
				"content": "this is content"
			},
			{
				"title": "yahya",
				"content": "this is content"
			},
			{
				"title": "starlight",
				"content": "this is content"
			},
			{
				"title": "twinkle",
				"content": "this is content"
			}
		]
	},
	{
		"group_name": "3",
		"group_list": [{
				"title": "esther",
				"content": "this is content"
			},
			{
				"title": "asta",
				"content": "this is content"
			},
			{
				"title": "gary",
				"content": "this is content"
			}
		]
	},
	{
		"group_name": "4",
		"group_list": [{
				"title": "peter",
				"content": "this is content"
			},
			{
				"title": "aldrich",
				"content": "this is content"
			}
		]
	},
	{
		"group_name": "5",
		"group_list": [{
				"title": "edgar",
				"content": "this is content"
			},
			{
				"title": "danika",
				"content": "this is content"
			},
			{
				"title": "clement",
				"content": "this is content"
			}
		]
	}
]
        """
    }
}