# Diff

Diff现在使用更加简单方便快速了。

## 1、快速使用

### 实现 DiffUtil.ItemCallback

代码如下：

```java
public class DiffDemoCallback extends DiffUtil.ItemCallback<DiffUtilDemoEntity> {

    /**
     * 判断是否是同一个item
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areItemsTheSame(@NonNull DiffUtilDemoEntity oldItem, @NonNull DiffUtilDemoEntity newItem) {
        return oldItem.getId() == newItem.getId();
    }

    /**
     * 当是同一个item时，再判断内容是否发生改变
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areContentsTheSame(@NonNull DiffUtilDemoEntity oldItem, @NonNull DiffUtilDemoEntity newItem) {
        return oldItem.getTitle().equals(newItem.getTitle())
                && oldItem.getContent().equals(newItem.getContent())
                && oldItem.getDate().equals(newItem.getDate());
    }

    /**
     * 可选实现
     * 如果需要精确修改某一个view中的内容，请实现此方法。
     * 如果不实现此方法，或者返回null，将会直接刷新整个item。
     *
     * @param oldItem Old data
     * @param newItem New data
     * @return Payload info. if return null, the entire item will be refreshed.
     */
    @Override
    public Object getChangePayload(@NonNull DiffUtilDemoEntity oldItem, @NonNull DiffUtilDemoEntity newItem) {
        return null;
    }
}
```

### Adapter 设置 DiffUtil.ItemCallback

代码如下：

```java
// 设置Diff Callback
// 只需要设置一次就行了！建议初始化Adapter的时候就设置好。
mAdapter.setDiffCallback(new DiffDemoCallback());
```

### Diff 变化数据

```java
// 获取新数据
List<DiffUtilDemoEntity> newData = getNewList();
// 设置diff数据（默认就为异步Diff，不需要担心卡顿）
mAdapter.setDiffNewData(newData);

// 第二次改变数据
mAdapter.setDiffNewData(newData2);
```

## 2、自定义 Diff 配置（可选）

> 自定义配置不使用`setDiffCallback()`方法！

首先还是需要实现`DiffUtil.ItemCallback`.

目前提供自定义线程
```java
BrvahAsyncDifferConfig config =  new BrvahAsyncDifferConfig.Builder(new DiffDemoCallback())
                .setMainThreadExecutor(你的主线程)
                .setBackgroundThreadExecutor(你的工作线程)
                .build();
// 设置配置
mAdapter.setDiffConfig(config);

// 设置diff数据
mAdapter.setDiffNewData(newData);
```

