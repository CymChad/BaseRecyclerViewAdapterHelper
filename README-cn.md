>由于最近用RecyclerView用的比较多所以最近写的一个开源项目，写了一个集成了RecyclerViewAdapter的常用功能的基类，希望能够帮助到大家，如果使用上遇到什么问题或想加入开发，把这个项目一起做的更好，**Star我的项目可加Q群558178792，申请的时候把GitHub的账号名字备注上否则不予通过，谢谢配合。**中国有句古话叫“授人以鱼不如授人以渔”，不仅仅提供使用，还写了如何实现的原理：「[RecyclerView.Adapter优化了吗？](http://blog.csdn.net/cym492224103/article/details/51113321)」「[BaseRecyclerAdapter之添加动画](http://blog.csdn.net/cym492224103/article/details/51150108)」

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

![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/demo.gif)
![分组](http://upload-images.jianshu.io/upload_images/972352-3e7ffedcf559cc9d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
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
	        compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.5'
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
#自定义item类型
```
public class XXXXXXAdapter extends BaseQuickAdapter { 
    /...
    @Override
    protected int getDefItemViewType(int position) {
        /...
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        /...
    }

    @Override
    protected void onBindDefViewHolder(RecyclerView.ViewHolder holder, final int position) {
        /...
    }

}
```
>**持续更新!，所以推荐Star项目**

#感谢
[JoanZapata / base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)
