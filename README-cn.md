>RecyclerView作为Android最常用的控件，受益群体几乎是所有Android开发者，希望更多开发者能够一起来维护这个项目，把这个项目做得更好，帮助更多人。**Star我的项目可加Q群558178792，申请的时候把GitHub的账号名字备注上否则不予通过，谢谢配合。**中国有句古话叫“授人以鱼不如授人以渔”，不仅仅提供使用，还写了如何实现的原理：
- 「[RecyclerView.Adapter优化了吗？](http://www.jianshu.com/p/411ab861034f)」
- 「[BaseRecyclerAdapter之添加动画](http://www.jianshu.com/p/fa3f97c19263)」
- 「[BaseRecyclerAdapter之添加不同布局（头部尾部）](http://www.jianshu.com/p/9d75c22f0964)」
- 「[BaseRecyclerAdapter之添加不同布局（优化篇）](http://www.jianshu.com/p/cf29d4e45536)」
- 「[BaseRecyclerAdapter之分组功能原理分析](http://www.jianshu.com/p/87a49f732724)」
- 「[RecyclerView相关优秀文集](https://github.com/CymChad/CymChad.github.io)」  
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-BaseRecyclerViewAdapterHelper-green.svg?style=true)](https://android-arsenal.com/details/1/3644)
[![](https://jitpack.io/v/CymChad/BaseRecyclerViewAdapterHelper.svg)](https://jitpack.io/#CymChad/BaseRecyclerViewAdapterHelper)

# BaseRecyclerViewAdapterHelper
![logo](http://upload-images.jianshu.io/upload_images/972352-1d77e0a75a4a7c0a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
一个强大并且灵活的RecyclerViewAdapter，欢迎使用。（喜欢的可以**Star**一下）
## Google Play Demo

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.chad.baserecyclerviewadapterhelper)
#它能做什么？（[下载 apk](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/raw/master/demo_res/demo.apk)）
- **优化Adapter代码（减少百分之70%代码）**
- **添加点击item点击、长按事件、以及item子控件的点击事件**
- **添加加载动画（一行代码轻松切换5种默认动画）**
- **添加头部、尾部、下拉刷新、上拉加载（感觉又回到ListView时代）**
- **设置自定义的加载更多布局**
- **添加分组（随心定义分组头部）**
- **自定义不同的item类型（简单配置、无需重写额外方法）**
- **设置空布局（比Listview的setEmptyView还要好用！）**
- **添加拖拽item**

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
	        compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.9.7'
	}
```

#如何使用它来创建Adapter？
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/item_view.png)
```java
public class QuickAdapter extends BaseQuickAdapter<Status> {
    public QuickAdapter() {
        super(R.layout.tweet, DataServer.getSampleData());
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);
                 Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));
    }
}
```
# 如何添加 item 的点击事件
```java
  mRecyclerView.addOnItemTouchListener(new OnRecyclerViewItemClickListener(mRecyclerView,mQuickAdapter){
  
              @Override
              public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                  Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
              }
  
              @Override
              public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                  super.onItemChildClick(adapter, view, position);
                  switch (view.getId()){
                      case R.id.tweetAvatar:
                          Toast.makeText(RecyclerClickItemActivity.this, "R.id.tweetAvatar click" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                          break;
                      case R.id.tweetName:
                          Toast.makeText(RecyclerClickItemActivity.this, "R.id.tweetName click" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                          break;
                      default:
                          break;
                  }
              }
  
  
              @Override
              public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                  super.onItemLongClick(adapter, view, position);
                  Toast.makeText(RecyclerClickItemActivity.this, "ItemLongClick" + Integer.toString(position), Toast.LENGTH_SHORT).show();
  
              }
  
              @Override
              public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                  super.onItemChildLongClick(adapter, view, position);
                  Toast.makeText(RecyclerClickItemActivity.this, view.getId()+" getLongClick" + Integer.toString(position), Toast.LENGTH_SHORT).show();
  
              }
          });
```
![addOnItemTouchListener](http://7xs9qs.com1.z0.glb.clouddn.com/B001F6D5-E057-4202-9312-8953D9B60E18.png)
#如果需要给 childView 设置点击事件
```
 helper.addOnClickListener(R.id.tweetAvatar)
       .addOnClickListener(R.id.tweetName);
```
#如何添加item点击、长按事件
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/chlid_click.gif)
```java
mQuickAdapter.setOnRecyclerViewItemClickListener();
mQuickAdapter.setOnRecyclerViewItemLongClickListener();
```
#新增添加子布局多个控件的点击事件
Adapter
```java
 protected void convert(BaseViewHolder helper, Status item) {
    helper.setOnClickListener(R.id.tweetAvatar, new OnItemChildClickListener())
      .setOnClickListener(R.id.tweetName, new OnItemChildClickListener());
      }
```
Activity
```java
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
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/animation.gif)
```java
// 一行代码搞定（默认为渐显效果）
quickAdapter.openLoadAnimation();
```
不喜欢渐显动画可以这样更换
```java
// 默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）
quickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
```
还是没你喜欢的，你可以自定义
```java
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
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/header_footer.gif)
```java
// add
mQuickAdapter.addHeaderView(getView());
mQuickAdapter.addFooterView(getView());
// remove
removeHeaderView(getView);
removeFooterView(getView);
// or
removeAllHeaderView();
removeAllFooterView();
```
#使用它加载更多
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/load_more.gif)
```java
mQuickAdapter.openLoadMore(PAGE_SIZE, true);
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
```java
mQuickAdapter.setLoadingView(customView);
```
#使用分组
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/section_headers.gif)
```java
public class SectionAdapter extends BaseSectionQuickAdapter<MySection> {
     public SectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        helper.setImageUrl(R.id.iv, (String) item.t);
    }
    @Override
    protected void convertHead(BaseViewHolder helper,final MySection item) {
        helper.setText(R.id.header, item.header);
       
        helper.setOnClickListener(R.id.more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,item.header+"more..",Toast.LENGTH_LONG).show();
            }
        });
    }
```
#如何添加多种类型item？
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/multiple_item.gif)
```java
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.text_view);
        addItemType(MultipleItem.IMG, R.layout.image_view);
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
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/empty_view.gif)
```java
mQuickAdapter.setEmptyView(getView());
```
#使用拖拽与滑动删除
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/drag_item.gif)
```java
OnItemDragListener onItemDragListener = new OnItemDragListener() {
    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos){}
    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {}
    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {}
}

OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {}
    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {}
    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {}
};

public class ItemDragAdapter extends BaseItemDraggableAdapter<String> {
    public ItemDragAdapter(List data) {
        super(R.layout.item_draggable_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv, item);
    }
}

mAdapter = new ItemDragAdapter(mData);

ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
itemTouchHelper.attachToRecyclerView(mRecyclerView);

// 开启拖拽
mAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
mAdapter.setOnItemDragListener(onItemDragListener);

// 开启滑动删除
mAdapter.enableSwipeItem();
mAdapter.setOnItemSwipeListener(onItemSwipeListener);
```

>**持续更新!，所以推荐Star项目**

#感谢
[JoanZapata / base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)
