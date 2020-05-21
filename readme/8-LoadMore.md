# LoadMore

## Adapter代码

`Adapter`类实现`LoadMoreModule`接口即可，无需做其他操作

代码如下：

```java
// adapter 实现接口 LoadMoreModule
public class LoadMoreAdapter extends BaseQuickAdapter<Status, BaseViewHolder> implements LoadMoreModule {
    ………………
}
```

### 调用方法

`Adapter`通过`getLoadMoreModule()`方法获取此模块。

示例如下：

```java
// 获取模块
mAdapter.getLoadMoreModule();

// 打开或关闭加载更多功能（默认为true）
mAdapter.getLoadMoreModule().setEnableLoadMore(false);

// 是否自定加载下一页（默认为true）
mAdapter.getLoadMoreModule().setAutoLoadMore(true);

// 当数据不满一页时，是否继续自动加载（默认为true）
mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

// 所有数据加载完成后，是否允许点击（默认为false）
mAdapter.getLoadMoreModule().setEnableLoadMoreEndClick(false);

// 是否处于加载中
mAdapter.getLoadMoreModule().isLoading();

// 预加载的位置（默认为1）
mAdapter.getLoadMoreModule().setPreLoadNumber(1);

// 设置加载更多监听事件
mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
    @Override
    public void onLoadMore() {
        loadMore();
    }
});

/******************************** 状态设置 ********************************/
// 当前这次数据加载完毕，调用此方法
mAdapter.getLoadMoreModule().loadMoreComplete();

// 当前这次数据加载错误，调用此方法
mAdapter.getLoadMoreModule().loadMoreFail();

// 所有数据加载完成，调用此方法
// 需要重置"加载完成"状态时，请调用 setNewData()
mAdapter.getLoadMoreModule().loadMoreEnd();

// 状态手动置为“加载中”，并且会调用加载更多监听
// 一般情况下，不需要自己设置'加载中'状态
mAdapter.getLoadMoreModule().loadMoreToLoading();
```



## 设置自定义的LoadMore View

默认自带了一套`LoadMore`视图，你可以根据需要，设置自己的视图。设置视图分为两种方式：

### 1、全局设置（优先度低）

代码如下：

```java
public class MyApplication extends Application {
  
    @Override
    public void onCreate() {
        // 在 Application 中配置全局自定义的 LoadMoreView
        LoadMoreModuleConfig.setDefLoadMoreView(new CustomLoadMoreView());
    }
}
```

### 2、Adapter单独设置（优先于全局设置）

代码如下：

```java
mAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
```

### 3、自定义LoadMoreView

自定义类集成于`BaseLoadMoreView`。

示例如下：

```java
public final class CustomLoadMoreView extends BaseLoadMoreView {

    @NotNull
    @Override
    public View getRootView(@NotNull ViewGroup parent) {
        // 整个 LoadMore 布局
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.view_load_more, parent, false);
    }

    @NotNull
    @Override
    public View getLoadingView(@NotNull BaseViewHolder holder) {
        // 布局中 “加载中”的View
        return holder.findView(R.id.load_more_loading_view);
    }

    @NotNull
    @Override
    public View getLoadComplete(@NotNull BaseViewHolder holder) {
        // 布局中 “当前一页加载完成”的View
        return holder.findView(R.id.load_more_load_complete_view);
    }

    @NotNull
    @Override
    public View getLoadEndView(@NotNull BaseViewHolder holder) {
        // 布局中 “全部加载结束，没有数据”的View
        return holder.findView(R.id.load_more_load_end_view);
    }

    @NotNull
    @Override
    public View getLoadFailView(@NotNull BaseViewHolder holder) {
        // 布局中 “加载失败”的View
        return holder.findView(R.id.load_more_load_fail_view);
    }
}
```

