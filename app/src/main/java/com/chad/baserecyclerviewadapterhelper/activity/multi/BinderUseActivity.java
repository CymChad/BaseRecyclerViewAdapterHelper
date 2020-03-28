package com.chad.baserecyclerviewadapterhelper.activity.multi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityMultipleItemUseBinding;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemImgTextViewBinding;
import com.chad.baserecyclerviewadapterhelper.databinding.ItemMovieBinding;
import com.chad.baserecyclerviewadapterhelper.entity.ContentEntity;
import com.chad.baserecyclerviewadapterhelper.entity.ImageEntity;
import com.chad.baserecyclerviewadapterhelper.entity.Movie;
import com.chad.baserecyclerviewadapterhelper.entity.MoviePresenter;
import com.chad.baserecyclerviewadapterhelper.entity.Video;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseBinderAdapter;
import com.chad.library.adapter.base.binder.BaseItemBinder;
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder;
import com.chad.library.adapter.base.binder.QuickItemBinder;
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: limuyang
 * @date: 2019-12-04
 * @Description:
 */
public class BinderUseActivity extends BaseActivity {

    // 可以直接快速使用，也可以继承BaseBinderAdapter类，重写自己的相关方法
    private BaseBinderAdapter adapter = new BaseBinderAdapter();

    private ActivityMultipleItemUseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultipleItemUseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("BaseMultiItemQuickAdapter");
        setBackBtn();

        initRv();
    }

    private void initRv() {

        // 添加 itemBinder, 各种创建方式如下
        adapter.addItemBinder(ImageEntity.class, new ImageItemBinder()) // QuickItemBinder
                .addItemBinder(Video.class, new ImageTextItemBinder(), new ImageTextItemBinder.Differ()) // QuickViewBindingItemBinder, 并且注册了 Diff
                .addItemBinder(Movie.class, new MovieItemBinder()) // QuickDataBindingItemBinder
                .addItemBinder(ContentEntity.class, new ContentItemBinder()); // BaseItemBinder

        View headView = getLayoutInflater().inflate(R.layout.head_view, null, false);
        headView.findViewById(R.id.iv).setVisibility(View.GONE);
        headView.setOnClickListener(v -> Tips.show("HeaderView"));
        adapter.setHeaderView(headView);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Random random = new Random();

        // 模拟设置数据，数据顺序随意组合
        List<Object> data = new ArrayList<>();
        data.add(new ImageEntity());
        data.add(new ImageEntity());
        data.add(new Video(1, "", "Video 1"));
        data.add(new Video(2, "", "Video 2"));
        data.add(new Movie("Chad 1", 0, random.nextInt(5) + 10, "He was one of Australia's most distinguished artistes"));
        data.add(new ImageEntity());
        data.add(new Movie("Chad 2", 0, random.nextInt(5) + 10, "This movie is exciting"));
        data.add(new ImageEntity());
        data.add(new Video(3, "", "Video 3"));
        data.add(new Video(4, "", "Video 4"));
        data.add(new ContentEntity("Content 1"));
        data.add(new ContentEntity("Content 2"));

        // 设置新数据
        adapter.setList(data);

        // 延迟3秒以后执行，模拟Diff刷新数据
        binding.rvList.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 模拟需要 Diff 的新数据
                // 先拷贝出数据
                List<Object> data = new ArrayList<>(adapter.getData());

                // 修改数据
                data.add(0, new ImageEntity());
                data.add(2, new Video(8, "", "new Video 1.1"));
                data.add(new ImageEntity());


                // 使用 diif 刷新数据
                adapter.setDiffNewData(data);
            }
        }, 3000);
    }

    /**
     * 使用 layout，快速创建Binder
     */
    private final static class ImageItemBinder extends QuickItemBinder<ImageEntity> {

        @Override
        public int getLayoutId() {
            return R.layout.item_image_view;
        }

        @Override
        public void convert(@NotNull BaseViewHolder holder, ImageEntity data) {
        }

        @Override
        public void onClick(@NotNull BaseViewHolder holder, @NotNull View view, ImageEntity data, int position) {
            Toast.makeText(getContext(), "click index: " + position, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用 ViewBinding，快速创建Binder
     */
    private final static class ImageTextItemBinder extends QuickViewBindingItemBinder<Video, ItemImgTextViewBinding> {

        @NotNull
        @Override
        public ItemImgTextViewBinding onCreateViewBinding(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup parent, int viewType) {
            return ItemImgTextViewBinding.inflate(layoutInflater, parent, false);
        }

        @Override
        public void convert(@NotNull BinderVBHolder<ItemImgTextViewBinding> holder, Video data) {
            holder.getViewBinding().tv.setText("(ViewBinding) " + data.getName());
        }

        /**
         * 如果需要 Diff，可以自行实现如下内容
         */
        public static class Differ extends DiffUtil.ItemCallback<Video> {
            @Override
            public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
                return oldItem.getName().equals(newItem.getName());
            }
        }
    }

    /**
     * 使用 DataBinding，快速创建Binder
     */
    private final static class MovieItemBinder extends QuickDataBindingItemBinder<Movie, ItemMovieBinding> {

        private MoviePresenter mPresenter = new MoviePresenter();

        @NotNull
        @Override
        public ItemMovieBinding onCreateDataBinding(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup parent, int viewType) {
            return ItemMovieBinding.inflate(layoutInflater, parent, false);
        }

        @Override
        public void convert(@NotNull BinderDataBindingHolder<ItemMovieBinding> holder, Movie data) {
            ItemMovieBinding binding = holder.getDataBinding();
            binding.setMovie(data);
            binding.setPresenter(mPresenter);
            binding.executePendingBindings();
        }
    }

    /**
     * 使用最基础的 BaseItemBinder 创建 Binder
     */
    private static class ContentItemBinder extends BaseItemBinder<ContentEntity, BaseViewHolder> {
        @NotNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_view, parent, false);
            return new BaseViewHolder(view);
        }

        @Override
        public void convert(@NotNull BaseViewHolder holder, ContentEntity data) {
            holder.setText(R.id.tv, data.getContent());
        }
    }
}
