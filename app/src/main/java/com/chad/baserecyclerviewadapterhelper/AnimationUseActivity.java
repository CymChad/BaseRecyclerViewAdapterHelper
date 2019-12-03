package com.chad.baserecyclerviewadapterhelper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.AnimationAdapter;
import com.chad.baserecyclerviewadapterhelper.entity.Status;
import com.chad.baserecyclerviewadapterhelper.utils.Tips;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kyleduo.switchbutton.SwitchButton;

import org.jetbrains.annotations.NotNull;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * <p>
 * modify by AllenCoder
 */
public class AnimationUseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AnimationAdapter mAnimationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_use);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        initMenu();
        initView();
    }

    private void initView() {
        ImageView mImgBtn = findViewById(R.id.img_back);
        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });
    }

    private void initAdapter() {
        mAnimationAdapter = new AnimationAdapter();
        mAnimationAdapter.setAnimationEnable(true);
        int mFirstPageItemCount = 3;
//        mAnimationAdapter.setNotDoAnimationCount(mFirstPageItemCount);
        mAnimationAdapter.addItemChildClickViewIds(R.id.img, R.id.tweetName, R.id.tweetText);
        mAnimationAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String content = null;
                Status status = (Status) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.img:
                        content = "img:" + status.getUserAvatar();
                        break;
                    case R.id.tweetName:
                        content = "name:" + status.getUserName();
                        break;
                    case R.id.tweetText:
                        content = "tweetText:" + status.getUserName();
                        break;
                    default:
                        break;
                }
                Tips.show(content);
            }
        });
        mRecyclerView.setAdapter(mAnimationAdapter);
    }

    private void initMenu() {
        MaterialSpinner spinner = findViewById(R.id.spinner);
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom1", "Custom2");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        mAnimationAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
                        break;
                    case 1:
                        mAnimationAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn);
                        break;
                    case 2:
                        mAnimationAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
                        break;
                    case 3:
                        mAnimationAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);
                        break;
                    case 4:
                        mAnimationAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInRight);
                        break;
                    case 5:
                        mAnimationAdapter.setAdapterAnimation(new CustomAnimation1());
                        break;
                    case 6:
                        mAnimationAdapter.setAdapterAnimation(new CustomAnimation2());
                        break;
                    default:
                        break;
                }
                mRecyclerView.setAdapter(mAnimationAdapter);
            }
        });
        //init firstOnly state
        mAnimationAdapter.setAnimationFirstOnly(false);
        SwitchButton switchButton = findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    mAnimationAdapter.setAnimationFirstOnly(true);
                } else {
                    mAnimationAdapter.setAnimationFirstOnly(false);
                }
                mAnimationAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 自定义动画1
     */
    public class CustomAnimation1 implements BaseAnimation {
        @NotNull
        @Override
        public Animator[] animators(@NotNull View view) {
            Animator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1);
            Animator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1);
            Animator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);

            scaleY.setDuration(350);
            scaleX.setDuration(350);
            alpha.setDuration(350);

            scaleY.setInterpolator(new DecelerateInterpolator());
            scaleX.setInterpolator(new DecelerateInterpolator());

            return new Animator[]{scaleY, scaleX, alpha};
        }
    }

    /**
     * 自定义动画2
     */
    public class CustomAnimation2 implements BaseAnimation {
        @NotNull
        @Override
        public Animator[] animators(@NotNull View view) {
            Animator translationX =
                    ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0f);

            translationX.setDuration(800);
            translationX.setInterpolator(new MyInterpolator2());

            return new Animator[]{translationX};
        }

        class MyInterpolator2 implements Interpolator {
            @Override
            public float getInterpolation(float input) {
                float factor = 0.7f;
                return (float) (pow(2.0, -10.0 * input) * sin((input - factor / 4) * (2 * PI) / factor) + 1);
            }
        }
    }
}
