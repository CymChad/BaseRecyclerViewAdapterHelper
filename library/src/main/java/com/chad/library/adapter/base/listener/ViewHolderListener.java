package com.chad.library.adapter.base.listener;

import android.view.View;

import com.chad.library.adapter.base.XQuickAdapter;
import com.chad.library.adapter.base.vh.BaseViewHolder;

/**
 * Created by BlingBling on 2016/10/13.
 */

public class ViewHolderListener {

    private XQuickAdapter mAdapter;

    public ViewHolderListener(XQuickAdapter adapter) {
        mAdapter = adapter;
    }

    public void setOnClick(BaseViewHolder vh, View v, OnClickListener listener) {
        v.setOnClickListener(new MyClick(mAdapter, vh, listener));
    }

    public void setOnLongClick(BaseViewHolder vh, View v, OnLongClickListener listener) {
        v.setOnLongClickListener(new MyClick(mAdapter, vh, listener));
    }

    public interface OnClickListener {
        void onClick(XQuickAdapter adapter, View view, int position);
    }

    public interface OnLongClickListener {
        void onLongClick(XQuickAdapter adapter, View view, int position);
    }

    public static class MyClick implements View.OnClickListener, View.OnLongClickListener {

        private XQuickAdapter mAdapter;
        private BaseViewHolder mVH;
        private OnClickListener mOnClickListener;
        private OnLongClickListener mOnLongClickListener;

        public MyClick(XQuickAdapter adapter, BaseViewHolder vh, OnClickListener listener) {
            mAdapter = adapter;
            mVH = vh;
            mOnClickListener = listener;
        }

        public MyClick(XQuickAdapter adapter, BaseViewHolder vh, OnLongClickListener listener) {
            mAdapter = adapter;
            mVH = vh;
            mOnLongClickListener = listener;
        }

        @Override public void onClick(View v) {
            if (mOnClickListener == null) {
                OnItemClickListener listener = mAdapter.getOnItemClickListener();
                if (listener != null) {
                    if (mVH.itemView == v) {
                        listener.onItemClick(mAdapter, v, mVH.getAdapterPosition() - mAdapter.getHeaderViewCount());
                    } else {
                        listener.onItemChildClick(mAdapter, v, mVH.getAdapterPosition() - mAdapter.getHeaderViewCount());
                    }
                }
            } else {
                mOnClickListener.onClick(mAdapter, v, mVH.getAdapterPosition() - mAdapter.getHeaderViewCount());
            }
        }

        @Override public boolean onLongClick(View v) {
            if (mOnLongClickListener == null) {
                OnItemClickListener listener = mAdapter.getOnItemClickListener();
                if (listener != null) {
                    if (mVH.itemView == v) {
                        listener.onItemLongClick(mAdapter, v, mVH.getAdapterPosition() - mAdapter.getHeaderViewCount());
                    } else {
                        listener.onItemChildLongClick(mAdapter, v, mVH.getAdapterPosition() - mAdapter.getHeaderViewCount());
                    }
                }
            } else {
                mOnLongClickListener.onLongClick(mAdapter, v, mVH.getAdapterPosition() - mAdapter.getHeaderViewCount());
            }
            return true;
        }
    }
}
