package com.chad.baserecyclerviewadapterhelper.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding;
import com.chad.baserecyclerviewadapterhelper.utils.ExtKt;

/**
 * 文 件 名: BaseActivity
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public abstract class BaseActivity extends AppCompatActivity {

    public BaseActivity() {
        this(0);
    }

    public BaseActivity(@LayoutRes int layoutId) {
        super(layoutId);
    }

    protected ViewBinding binding;

    /**
     * 日志输出标志getSupportActionBar().
     **/
    private TextView title;
    private ImageView back;
    protected final String TAG = this.getClass().getSimpleName();
    private LinearLayout rootLayout;

    protected void setTitle(String msg) {
        if (title != null) {
            title.setText(msg);
        }
    }

    /**
     * sometime you want to define back event
     */
    protected void setBackBtn() {
        if (back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
//            Logger.t(TAG).e("back is null ,please check out");
        }

    }

    protected void setBackClickListener(View.OnClickListener l) {
        if (back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(l);
        } else {
//            Logger.t(TAG).e("back is null ,please check out");
        }

    }

    @Nullable
    public ViewBinding viewBinding() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewBinding binding = viewBinding();
        if (binding != null) {
            setContentView(binding.getRoot());
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.spinner_bg));
            ExtKt.setStatusBarLightMode(getWindow(), false);
        }

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        back = findViewById(R.id.img_back);
        title = findViewById(R.id.title);
    }


//    @Override
//    public void setContentView(int layoutId) {
//        setContentView(View.inflate(this, layoutId, null));
//    }
//
//    @Override
//    public void setContentView(View view) {
//        rootLayout = findViewById(R.id.root_layout);
//        if (rootLayout == null) {
//            return;
//        }
//        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        initToolbar();
//    }
}
