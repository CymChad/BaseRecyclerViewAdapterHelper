package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.Status;

import java.util.List;
/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class DefAdpater extends RecyclerView.Adapter<DefAdpater.ViewHolder> {
    private final List<Status> sampleData = DataServer.getSampleData(100);
    private Context mContext;
    public DefAdpater(Context context) {
        mContext = context;
    } 
   @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet, parent, false);
        return new ViewHolder(item);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status status = sampleData.get(position);
        holder.name.setText(status.getUserName());
        holder.text.setText(status.getText());
        holder.date.setText(status.getCreatedAt());
        Glide.with(mContext).load(status.getUserAvatar()).into(holder.avatar);
        holder.rt.setVisibility(status.isRetweet() ? View.VISIBLE : View.GONE);
    }
    @Override
    public int getItemCount() {
        return sampleData.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private ImageView rt;
        private TextView name;
        private TextView date;
        private TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tweetText);
            name = (TextView) itemView.findViewById(R.id.tweetName);
            date = (TextView) itemView.findViewById(R.id.tweetDate);
            avatar = (ImageView) itemView.findViewById(R.id.tweetAvatar);
            rt = (ImageView) itemView.findViewById(R.id.tweetRT);
        }
    }
}