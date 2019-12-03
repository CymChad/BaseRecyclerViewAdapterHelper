package com.chad.baserecyclerviewadapterhelper.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author: limuyang
 * @date: 2019-11-29
 * @Description:
 */
public class Tips {

    /**
     * 显示 Toast
     * @param message 提示信息
     */
    public static void show(String message) {
        show(message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示 Toast
     * @param message 提示信息
     * @param duration 显示时间长短
     */
    public static void show(String message, int duration) {
        Toast toast = new Toast(Utils.getContext());
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(createTextToastView(message));
        toast.show();
    }

    /**
     * 创建自定义 Toast View
     *
     * @param message 文本消息
     * @return View
     */
    private static View createTextToastView(String message) {
        // 画圆角矩形背景
        float rc = dp2px(6);
        RoundRectShape shape = new RoundRectShape(new float[]{rc, rc, rc, rc, rc, rc, rc, rc}, null, null);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.getPaint().setColor(Color.argb(225, 240, 240, 240));
        drawable.getPaint().setStyle(Paint.Style.FILL);
        drawable.getPaint().setAntiAlias(true);
        drawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);

        // 创建View
        FrameLayout layout = new FrameLayout(Utils.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        layout.setPadding(dp2px(16), dp2px(12), dp2px(16), dp2px(12));
        layout.setBackground(drawable);

        TextView textView = new TextView(Utils.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(15);
        textView.setText(message);
        textView.setLineSpacing(dp2px(4), 1f);
        textView.setTextColor(Color.BLACK);

        layout.addView(textView);

        return layout;
    }

    public static int dp2px(float dpValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
