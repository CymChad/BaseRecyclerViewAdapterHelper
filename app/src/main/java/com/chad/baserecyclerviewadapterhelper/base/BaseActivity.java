package com.chad.baserecyclerviewadapterhelper.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.util.ToastUtils;

/**
 * 文 件 名: MyApplication
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class BaseActivity extends AppCompatActivity {
    protected View customView;
    private ImageView mBack;
    private TextView mActionBarTitle;
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        // 设置android:fitsSystemWindows="true"属性
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 19) {
            parentView.setFitsSystemWindows(true);
        }
    }

    protected void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.layout_title_bar, null);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

            actionBar.setCustomView(v, layout);
            /**
             * 解决 actionbar 两端空白问题
             */
            Toolbar parent = (Toolbar) v.getParent();
            parent.setContentInsetsAbsolute(0, 0);
            customView = actionBar.getCustomView();
            mActionBarTitle = (TextView) customView.findViewById(R.id.title);
            mBack = (ImageView) customView.findViewById(R.id.img_back);
            /**
             * 解决 actionBar 阴影问题
             */
            getSupportActionBar().setElevation(0);
            customView = getSupportActionBar().getCustomView();

        }
    }

    /**
     * actionbar回退键
     */
    protected void setBackBtn() {
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setBackBtn(View.OnClickListener listener) {
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(listener);
    }

    /**
     * actionbar标题
     *
     * @param title
     */
    protected void setTitle(@NonNull String title) {
        if (null != mActionBarTitle) {
            mActionBarTitle.setText(title);
        }
    }


    protected void showToast(CharSequence message) {
        ToastUtils.showShortToast(message);
    }
}
