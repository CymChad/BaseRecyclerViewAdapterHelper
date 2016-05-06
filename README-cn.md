>RecyclerView作为Android最常用的控件，受益群体几乎是所有Android开发者，希望更多开发者能够一起来维护这个项目，把这个项目做得更好，帮助更多人。**Star我的项目可加Q群558178792，申请的时候把GitHub的账号名字备注上否则不予通过，谢谢配合。**中国有句古话叫“授人以鱼不如授人以渔”，不仅仅提供使用，还写了如何实现的原理：
- 「[RecyclerView.Adapter优化了吗？](http://www.jianshu.com/p/411ab861034f)」
- 「[BaseRecyclerAdapter之添加动画](http://www.jianshu.com/p/fa3f97c19263)」
- 「[BaseRecyclerAdapter之添加不同布局（头部尾部）](http://www.jianshu.com/p/9d75c22f0964)」
- 「[BaseRecyclerAdapter之添加不同布局（优化篇）](http://www.jianshu.com/p/cf29d4e45536)」


# BaseRecyclerViewAdapterHelper
一个强大并且灵活的RecyclerViewAdapter，欢迎使用。（喜欢的可以**Star**一下）
#它能做什么？（[下载 apk](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/raw/master/demo_res/demo.apk)）
- **它可以大量减少你Adapter写的代码（和正常的Adapter相比至少三分之二的）**
- **可以添加点击事件**
- **它可以很轻松的添加RecyclerView加载动画**
- **新增添加头部、添加尾部**
- **新增下拉刷新、上拉加载更多**
- **新增分组**
- **自定义item类型**
- **添加setEmptyView方法**
- **新增添加子布局多个控件的点击事件**

![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/demo.gif)
#如何使用它？
先在 build.gradle 的 repositories 添加:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
然后在dependencies添加:
```
	dependencies {
	        compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.6.2'
	}
```

#如何使用它来创建Adapter？

```
public class QuickAdapter extends BaseQuickAdapter<Status> {
    public QuickAdapter(Context context) {
        super(context, R.layout.tweet, DataServer.getSampleData());
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setImageUrl(R.id.tweetAvatar, item.getUserAvatar())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);
    }
}
```
**这么复杂的布局只需要15行代码即可！**
#如何添加item点击事件
```
mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //..
            }
        });
```
#如何使用它添加动画？

```
// 一行代码搞定（默认为渐显效果）
quickAdapter.openLoadAnimation();
```
不喜欢渐显动画可以这样更换
```
// 默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）
quickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
```
还是没你喜欢的，你可以自定义
```
// 自定义动画如此轻松
quickAdapter.openLoadAnimation(new BaseAnimation() {
                            @Override
                            public Animator[] getAnimators(View view) {
                                return new Animator[]{
                                        ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
                                        ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
                                };
                            }
                        });
```
#如何添加多种类型item？
```
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem> {

    public MultipleItemQuickAdapter(Context context, List data) {
        super(context, data);
        addItmeType(MultipleItem.TEXT, R.layout.text_view);
        addItmeType(MultipleItem.IMG, R.layout.image_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                helper.setImageUrl(R.id.iv, item.getContent());
                break;
        }
    }

}
```
#使用它添加头部添加尾部
```
mQuickAdapter.addHeaderView(getView());
mQuickAdapter.addFooterView(getView());
```
#使用它加载更多
```
mQuickAdapter.setOnLoadMoreListener(PAGE_SIZE, new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mQuickAdapter.isNextLoad(false);
                        }
                    });
                } else {
                    // reqData
                    mCurrentCounter = mQuickAdapter.getItemCount();
                    mQuickAdapter.isNextLoad(true);
                }
            }
        });
```
#使用分组
```
public class SectionAdapter extends BaseSectionQuickAdapter<MySection> {
     public SectionAdapter(Context context, int layoutResId, int sectionHeadResId, List data) {
        super(context, layoutResId, sectionHeadResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        helper.setImageUrl(R.id.iv, (String) item.t);
    }
    @Override
    protected void convertHead(BaseViewHolder helper,final MySection item) {
        helper.setText(R.id.header, item.header);
        if(!item.isMroe)helper.setVisible(R.id.more,false);
        else
        helper.setOnClickListener(R.id.more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,item.header+"more..",Toast.LENGTH_LONG).show();
            }
        });
    }
```
#使用setEmptyView
```
mQuickAdapter.setEmptyView(getView());
```
#新增添加子布局多个控件的点击事件
Adapter
```
 protected void convert(BaseViewHolder helper, Status item) {
    helper.setOnClickListener(R.id.tweetAvatar, new OnItemChildClickListener())
      .setOnClickListener(R.id.tweetName, new OnItemChildClickListener());
      }
```
Activity
```
mQuickAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String content = null;
                Status status = (Status) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tweetAvatar:
                        content = "img:" + status.getUserAvatar();
                        break;
                    case R.id.tweetName:
                        content = "name:" + status.getUserName();
                        break;
                }
                Toast.makeText(AnimationUseActivity.this, content, Toast.LENGTH_LONG).show();
            }
        });
```

>**持续更新!，所以推荐Star项目**

#感谢
[JoanZapata / base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)
