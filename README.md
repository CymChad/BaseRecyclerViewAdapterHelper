[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-BaseRecyclerViewAdapterHelper-green.svg?style=true)](https://android-arsenal.com/details/1/3644)
[![](https://jitpack.io/v/CymChad/BaseRecyclerViewAdapterHelper.svg)](https://jitpack.io/#CymChad/BaseRecyclerViewAdapterHelper)
# BaseRecyclerViewAdapterHelper（[中文版文档](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/README-cn.md)）
![Paste_Image.png](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/develop/demo_res/logo.jpg)  
Powerful and flexible RecyclerAdapter 
Please feel free to use this.(Welcome to **Star** and **Fork**)
## Google Play Demo

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.chad.baserecyclerviewadapterhelper)
#Features（[download apk](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/raw/master/demo_res/demo.apk)）
- **Reduce lot of code.easily create RecyclerAdapter**
- **add item click and add item long click and item chlid click**
- **easily add RecyclerAdapter animations**
- **add HeadView and add FooterView**
- **add The drop-down refresh, load more**
- **set custom loading view**
- **easily create section headers**
- **custom item view type**
- **add setEmptyView methods**
- **add drag item**
- **Expandable Item**

#Extension library
[PinnedSectionItemDecoration](https://github.com/oubowu/PinnedSectionItemDecoration)
# Get it
Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
Add the dependency
```groovy
dependencies {
        compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.9.7'
}
```

#Use it create RecyclerAdapter
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
#Use it item click and item chlid click
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/chlid_click.gif)
#Use it item  click 
Adapter
```java
mRecyclerView.addOnItemTouchListener(new OnItemClickListener( ){

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                
            }
        });
        
```
#Use it item child click
first you should register child view id 
``` 
 @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .addOnClickListener(R.id.tweetAvatar)
                .addOnClickListener(R.id.tweetName)
                .addOnLongClickListener(R.id.tweetText)
                .linkify(R.id.tweetText);
        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getView(R.id.tweetAvatar));
    }
```
and then

```java
   mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener( ) {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });
```
#Use it item long click
```java
 mRecyclerView.addOnItemTouchListener(new OnItemLongClickListener( ) {
            @Override
            public void SimpleOnItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });
```
#use it item child long click
Adapter
``` 
 @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .addOnClickListener(R.id.tweetAvatar)
                .addOnClickListener(R.id.tweetName)
                .addOnLongClickListener(R.id.tweetText)
                .linkify(R.id.tweetText);
        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getView(R.id.tweetAvatar));
    }
```
Activity
```java
 mRecyclerView.addOnItemTouchListener(new OnItemChildLongClickListener( ) {
            @Override
            public void SimpleOnItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
```
#if you wish to implement various forms of click
Activity
```java
 mRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
``` 



#Use it add adaptar Animation
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/animation.gif)
```java
// Turn animation
quickAdapter.openLoadAnimation();
```
or
```java
// Turn animation and set animate
quickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
```
or
```java
// Turn animation and set custom animate
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
#Use it custom item view type
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
#Use it add header and footer
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
#Use it load more
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/load_more.gif)
setOnLoadMoreListener
```java
mQuickAdapter.openLoadMore(PAGE_SIZE, true);
mQuickAdapter.setOnLoadMoreListener(this);
```
Override onLoadMoreRequested()
```java
@Override
public void onLoadMoreRequested() {
        mRecyclerView.post(new Runnable() {
        @Override
        public void run() {
        if (mCurrentCounter >= TOTAL_COUNTER) {
                    mQuickAdapter.notifyDataChangedAfterLoadMore(false);
                }
        } else {
                    mQuickAdapter.notifyDataChangedAfterLoadMore(DataServer.getSampleData(PAGE_SIZE),true);
                    mCurrentCounter = mQuickAdapter.getItemCount();
        }});
    }
```
#Set custom loading view
```java
mQuickAdapter.setLoadingView(customView);
```
#Use it create section headers
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
#Use it setEmptyView
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/empty_view.gif)
```java
mQuickAdapter.setEmptyView(getView());
```
#Use it drag and swipe item
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

// enable drag items
mAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
mAdapter.setOnItemDragListener(onItemDragListener);

// enable swipe items
mAdapter.enableSwipeItem();
mAdapter.setOnItemSwipeListener(onItemSwipeListener);
```

#Expandable Item
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/expandable_item.gif)
```Java
// if you don't want to extent a class, you can also use the interface IExpandable.
// AbstractExpandableItem is just a helper class.
public class Level0Item extends AbstractExpandableItem<Level1Item> {...}
public class Level1Item extends AbstractExpandableItem<Person> {...}
public class Person {...}
```
in adapter code
```Java

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity> { 
    public ExpandableItemAdapter(List<MultiItemEntity> data) {    
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);   
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);    
        addItemType(TYPE_PERSON, R.layout.item_text_view);
    }
    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
        case TYPE_LEVEL_0:
            ....
            //set view content
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int pos = holder.getAdapterPosition();
                   if (lv0.isExpanded()) { 
                       collapse(pos);
                   } else {
                       expand(pos);
                   }
           }});
           break;
        case TYPE_LEVEL_1:
           // similar with level 0
           break;
        case TYPE_PERSON:
           //just set the content
           break;
    }
}
```
In activity code
```Java
public class ExpandableUseActivity extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        ...
        ArrayList<MultiItemEntity> list = generateData();
        ExpandableItemAdapter adapter = new ExpandableItemAdapter(list);
        mRecyclerView.setAdapter(adapter);
    }

    private ArrayList<MultiItemEntity> generateData() {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            Level0Item lv0 = new Level0Item(...);
            for (int j = 0; j < lv1Count; j++) {
                Level1Item lv1 = new Level1Item(...);
                for (int k = 0; k < personCount; k++) {
                    lv1.addSubItem(new Person());
                }
                lv0.addSubItem(lv1);
            }
            res.add(lv0);
        }
        return res;
    }
}
```

#Thanks
[JoanZapata / base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)

#License
```
Copyright 2016 陈宇明

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
