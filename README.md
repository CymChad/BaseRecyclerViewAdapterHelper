![](https://user-images.githubusercontent.com/7698209/33198075-ef8f2230-d123-11e7-85a3-4cb9b22f877d.png)
[![](https://jitpack.io/v/CymChad/BaseRecyclerViewAdapterHelper.svg)](https://jitpack.io/#CymChad/BaseRecyclerViewAdapterHelper)![](https://travis-ci.org/CymChad/BaseRecyclerViewAdapterHelper.svg?branch=master)[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-BaseRecyclerViewAdapterHelper-green.svg?style=true)](https://android-arsenal.com/details/1/3644)[![CircleCI](https://circleci.com/gh/CymChad/BaseRecyclerViewAdapterHelper/tree/master.svg?style=svg)](https://circleci.com/gh/CymChad/BaseRecyclerViewAdapterHelper/tree/master)[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2302d0084d0048eaa0f9bac4350837a0)](https://www.codacy.com/app/CymChad/BaseRecyclerViewAdapterHelper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CymChad/BaseRecyclerViewAdapterHelper&amp;utm_campaign=Badge_Grade)[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E9%99%88%E5%AE%87%E6%98%8E-7AD6FD.svg)](https://www.zhihu.com/people/chen-yu-ming-98/activities)  
# BRVAH
http://www.recyclerview.org/  
Powerful and flexible RecyclerAdapter,
Please feel free to use this. (Welcome to **Star** and **Fork**)  

kotlin demo :[BRVAH_kotlin](https://github.com/AllenCoder/BRVAH_kotlin)

## [androidX stable version ](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/releases/tag/2.9.46-androidx)
# Document
- [English](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki)
- [中文](http://www.jianshu.com/p/b343fcff51b0)

## [UI](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/694)
## Demo

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.chad.baserecyclerviewadapterhelper)

[国内下载地址](https://fir.im/s91g)  

# proguard-rules.pro
```
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
-keepattributes InnerClasses
```

# Extension library
[PinnedSectionItemDecoration](https://github.com/oubowu/PinnedSectionItemDecoration)  
[EasyRefreshLayout](https://github.com/anzaizai/EasyRefreshLayout)  
[EasySwipeMenuLayout](https://github.com/anzaizai/EasySwipeMenuLayout)

# Thanks  
[JoanZapata / base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)

# License
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
