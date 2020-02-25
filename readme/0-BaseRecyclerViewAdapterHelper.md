# BaseRecyclerViewAdapterHelper

![logo](http://upload-images.jianshu.io/upload_images/972352-1d77e0a75a4a7c0a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
一个强大并且灵活的RecyclerViewAdapter，欢迎使用。（喜欢的可以**Star**一下）

基于`AndroidX`的全新3.x版本现已发布beta版本，修复了众多的遗留问题，加强了对DataBinding的支持，多布局更加灵活。

如遇到问题，欢迎提交issus，旧版2.x不再进行新功能增加。

**注意：从2.x升级到3.x为不完全兼容升级！**

*<u>加载更多、向上加载、拖拽为模块，并不集成在某一个Adapter中，根据需要集成即可。</u>*

## 导入方式
### 将JitPack存储库添加到您的构建文件中(项目根目录下build.gradle文件)
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### 添加依赖项
[![](https://jitpack.io/v/CymChad/BaseRecyclerViewAdapterHelper.svg)](https://jitpack.io/#CymChad/BaseRecyclerViewAdapterHelper)
```
dependencies {
    implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.0'
}
```

（文档仍在补全当中……  代码是最好的老师，如遇问题，请先查看demo）

## Adapter 的使用

1、[BaseQuickAdapter](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/1-BaseQuickAdapter.md)

2、[BaseQuickAdapter 空布局\头部\脚部](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/2-BaseQuickAdapter%E7%A9%BA%E5%B8%83%E5%B1%80.md)

3、[DataBinding](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/3-DataBinding.md)

4、[多布局](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/4-%E5%A4%9A%E5%B8%83%E5%B1%80.md)

5、[BaseSectionQuickAdapter](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/5-BaseSectionQuickAdapter.md)

6、[BaseNodeAdapter（折叠\展开）](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/6-BaseNodeAdapter.md)

7、[DiffUtils](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/7-Diff.md)

## 模块的使用

8、[LoadMoew](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/8-LoadMore.md)

9、[UpFetch]()编写中...

10、[Draggable]()编写中...
