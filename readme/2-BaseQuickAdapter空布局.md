# BaseQuickAdapter 空布局\头部\脚部

## 1、空布局

说明：当`adapter`中数据不为空时，空布局不会生效

### 通过设置View为空布局

代码如下：

```java
View view = ...
adapter.setEmptyView(view);
```

### 通过设置LayoutResId为空布局

代码如下：

```java
adapter.setEmptyView(R.layout.loading_view);
```



### 设置是否使用空布局（默认为true）

代码如下：

```
// java
adapter.setUseEmpty(false);

// kotlin
adapter.isUseEmpty = false
```



## 2、头部

说明：此头部并不是item的头部，而是整个`Adapter`的头部

代码如下：

```java
View view = ...；
adapter.addHeaderView(view);

// 指定添加位置
adapter.addHeaderView(view, 1);

// 替换头部
adapter.setHeaderView(view);
//替换指定位置头部
adapter.setHeaderView(view, 0);

// 移除头部
adapter.removeHeaderView(view);
// 移除全部头部
adapter.removeAllHeaderView();
```

## 3、脚部

说明：此脚部不是item的脚部，而是整个`Adapter`的

代码如下：

```java
View view = ...；
adapter.addFooterView(view);

// 指定添加位置
adapter.addFooterView(view, 1);

// 替换脚部
adapter.setFooterView(view);
//替换指定位置脚部
adapter.setFooterView(view, 0);

// 移除脚部
adapter.removeFooterView(view);
// 移除全部脚部
adapter.removeAllFooterView();
```

