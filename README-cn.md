>RecyclerView作为Android最常用的控件，受益群体几乎是所有Android开发者，希望更多开发者能够一起来维护这个项目，把这个项目做得更好，帮助更多人。**Star我的项目可加Q群558178792，申请的时候把GitHub的账号名字备注上否则不予通过，谢谢配合。**中国有句古话叫“授人以鱼不如授人以渔”，不仅仅提供使用，还写了如何实现的原理：
- 「[RecyclerView.Adapter优化了吗？](http://blog.csdn.net/cym492224103/article/details/51113321)」
-  「[BaseRecyclerAdapter之添加动画](http://blog.csdn.net/cym492224103/article/details/51150108)」
-  「[BaseRecyclerAdapter之添加不同布局（头部尾部）](http://blog.csdn.net/cym492224103/article/details/51214362)」
-  「[BaseRecyclerAdapter之添加不同布局（优化篇）](http://blog.csdn.net/cym492224103/article/details/51222414)」
-  「[我的简书](http://www.jianshu.com/users/f958e66439f0/latest_articles)」

# BaseRecyclerViewAdapterHelper
![logo](http://upload-images.jianshu.io/upload_images/972352-1d77e0a75a4a7c0a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
一个强大并且灵活的RecyclerViewAdapter，欢迎使用。（喜欢的可以**Star**一下）
#它能做什么？（[下载 apk](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/raw/master/demo_res/demo.apk)）
- **优化Adapter代码（减少百分之70%代码）**
- **添加点击item点击、长按事件、以及item子控件的点击事件**
- **添加加载动画（一行代码轻松切换5种默认动画）**
- **添加头部、尾部、下拉刷新、上拉加载（感觉又回到ListView时代）**
- **设置自定义的加载更多布局**
- **添加分组（随心定义分组头部）**
- **自定义不同的item类型（简单配置、无需重写额外方法）**
- **设置空布局（比Listview的setEmptyView还要好用！）**


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
	        compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.7.2'
	}
```

#如何使用它来创建Adapter？
![demo](http://upload-images.jianshu.io/upload_images/972352-54bd17d3680a4cf9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
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
#如何添加item点击、长按事件
```
mQuickAdapter.setOnRecyclerViewItemClickListener();
mQuickAdapter.setOnRecyclerViewItemLongClickListener();
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
#使用它添加头部添加尾部
```
mQuickAdapter.addHeaderView(getView());
mQuickAdapter.addFooterView(getView());
```
#使用它加载更多
```
mQuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCounter >= TOTAL_COUNTER) {
                            mQuickAdapter.notifyDataChangedAfterLoadMore(false);
                        } else {
                            mQuickAdapter.notifyDataChangedAfterLoadMore(DataServer.getSampleData(PAGE_SIZE), true);
                            mCurrentCounter = mQuickAdapter.getItemCount();
                        }
                    }

                });
            }
        });
```
#设置自定义加载更多布局
```
mQuickAdapter.setLoadingView(customView);
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
#使用setEmptyView
```
mQuickAdapter.setEmptyView(getView());
```

>**持续更新!，所以推荐Star项目**

#感谢
[JoanZapata / base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)
