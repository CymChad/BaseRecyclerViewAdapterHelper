package com.chad.baserecyclerviewadapterhelper.adapter.diffUtil;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.entity.DiffUtilDemoEntity;
import com.chad.library.adapter.base.BaseDifferAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

import java.util.List;

/**
 * Create adapter
 */
public class DiffUtilAdapter extends BaseDifferAdapter<DiffUtilDemoEntity, QuickViewHolder> {
    public static final int ITEM_0_PAYLOAD = 901;

    public DiffUtilAdapter(List<DiffUtilDemoEntity> list) {
        super(new DiffDemoCallback(), list);
    }

    public DiffUtilAdapter() {
        super(new DiffDemoCallback());
    }


    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.layout_animation, parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, DiffUtilDemoEntity item) {
        holder.setText(R.id.tweetName, item.getTitle())
                .setText(R.id.tweetText, item.getContent())
                .setText(R.id.tweetDate, item.getDate());
    }

    /**
     * This method will only be executed when there is payload info
     *
     * 当有 payload info 时，只会执行此方法
     *
     * @param helper   A fully initialized helper.
     * @param item     The item that needs to be displayed.
     * @param payloads payload info.
     */
    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, DiffUtilDemoEntity item, @NonNull List<?> payloads) {
        for (Object p : payloads) {
            int payload = (int) p;
            if (payload == ITEM_0_PAYLOAD) {
                holder.setText(R.id.tweetName, item.getTitle())
                        .setText(R.id.tweetText, item.getContent());
            }
        }
    }



}
