# BaseRecyclerViewAdapterHelper（[中文版文档](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/README-cn.md)）
BaseRecyclerViewAdapterHelper is an Android library that allows developers to easily create RecyclerView with animations and with RecyclerAdapter.
Please feel free to use this.
#Features
- **Reduce lot of code.easily create RecyclerAdapter**
- **easily add RecyclerAdapter animations**

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
![demo](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/demo_res/demo.png)

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
![Custom.gif](http://upload-images.jianshu.io/upload_images/972352-60dff17fc9b0491f.gif?imageMogr2/auto-orient/strip)
![AlphaIn_ScaleIn.gif](http://upload-images.jianshu.io/upload_images/972352-3613112a80016b61.gif?imageMogr2/auto-orient/strip)
![SlideInBottom_SlideInLeft.gif](http://upload-images.jianshu.io/upload_images/972352-59c9865417032c00.gif?imageMogr2/auto-orient/strip)
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
#Method
## BaseQuickAdapter
* ```setOnRecyclerViewItemClickListener()```
* ```openLoadAnimation()```
* ```setFirstOnly()```

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
