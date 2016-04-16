# BaseRecyclerViewAdapterHelper（[中文版文档](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/README-cn.md)）
Powerful and flexible RecyclerAdapter 
Please feel free to use this.(Love can be a **Star**)
#Features（[download apk](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/raw/master/demo_res/demo.apk)）
- **Reduce lot of code.easily create RecyclerAdapter**
- **easily add RecyclerAdapter animations**
- **add HeadView and add FooterView**
- **add The drop-down refresh, load more**

![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/demo.gif)
# Get it
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
Add the dependency
```
	dependencies {
	        compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:v1.3'
	}
```

#Use it create RecyclerAdapter

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
#Use it add adaptar Animation

```

// Turn animation
quickAdapter.openLoadAnimation();
```
or
```
// Turn animation and set animate
quickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
```
or
```
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
#Use it add header and footer
```
mQuickAdapter.addHeaderView(getView());
mQuickAdapter.addFooterView(getView());
```
#Use it load more
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
#Method
## BaseQuickAdapter
* ```setOnRecyclerViewItemClickListener()```
* ```openLoadAnimation()```
* ```setFirstOnly()```
* ```addHeaderView()```
* ```addFooterView()```
* ```setOnLoadMoreListener()```

## BaseViewHolder
* ```setText()``` Calls ```setText(String)``` on any TextView.
* ```setAlpha()``` Calls ```setAlpha(float)``` on any View.
* ```setVisible()``` Calls ```setVisibility(int)``` on any View.
* ```linkify()``` Calls ```Linkify.addLinks(view, ALL)``` on any TextView.
* ```setTypeface()``` Calls ```setTypeface(Typeface)``` on any TextView.
* ```setProgress()``` Calls ```setProgress(int)``` on any ProgressBar.
* ```setMax()``` Calls ```setMax(int)``` on any ProgressBar.
* ```setRating()``` Calls ```setRating(int)``` on any RatingBar.
* ```setImageResource()``` Calls ```setImageResource(int)``` on any ImageView.
* ```setImageDrawable()``` Calls ```setImageDrawable(Drawable)``` on any ImageView.
* ```setImageBitmap()``` Calls ```setImageBitmap(Bitmap)``` on any ImageView.
* ```setImageUrl()``` Uses [Square's Picasso](http://square.github.io/picasso/) to download the image and put it in an ImageView.
* ```setImageBuilder()``` Associates a [Square's Picasso](http://square.github.io/picasso/) RequestBuilder to an ImageView.
* ```setOnClickListener()```
* ```setOnTouchListener()```
* ```setOnLongClickListener()```
* ```setTag()```
* ```setChecked()```
* ```setAdapter()```

#Thanks
[JoanZapata / base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)
