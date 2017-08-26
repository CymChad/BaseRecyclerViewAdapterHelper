/**
 * Copyright 2013 Joan Zapata
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chad.library.adapter.base;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseViewHolder extends RecyclerView.ViewHolder {

  /**
   * Views indexed with their IDs
   */
  private final SparseArray<View> views;

  public Set<Integer> getNestViews() {
    return nestViews;
  }

  private final HashSet<Integer> nestViews;

  private final LinkedHashSet<Integer> childClickViewIds;

  private final LinkedHashSet<Integer> itemChildLongClickViewIds;
  private BaseQuickAdapter adapter;
  /**
   * use itemView instead
   */
  @Deprecated
  public View convertView;

  /**
   * Package private field to retain the associated user object and detect a change
   */
  Object associatedObject;

  public BaseViewHolder(final View view) {
    super(view);
    this.views = new SparseArray<>();
    this.childClickViewIds = new LinkedHashSet<>();
    this.itemChildLongClickViewIds = new LinkedHashSet<>();
    this.nestViews = new HashSet<>();
    convertView = view;

  }

  private int getClickPosition() {
    return getLayoutPosition() - adapter.getHeaderLayoutCount();
  }

  public HashSet<Integer> getItemChildLongClickViewIds() {
    return itemChildLongClickViewIds;
  }

  public HashSet<Integer> getChildClickViewIds() {
    return childClickViewIds;
  }

  /**
   * use itemView instead
   *
   * @return the ViewHolder root view
   */
  @Deprecated
  public View getConvertView() {

    return convertView;
  }

  /**
   * Will set the text of a TextView.
   *
   * @param viewId The view id.
   * @param value The text to put in the text view.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setText(@IdRes int viewId, CharSequence value) {
    TextView view = getView(viewId);
    view.setText(value);
    return this;
  }

  public BaseViewHolder setText(@IdRes int viewId, @StringRes int strId) {
    TextView view = getView(viewId);
    view.setText(strId);
    return this;
  }

  /**
   * Will set the image of an ImageView from a resource id.
   *
   * @param viewId The view id.
   * @param imageResId The image resource id.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
    ImageView view = getView(viewId);
    view.setImageResource(imageResId);
    return this;
  }

  /**
   * Will set background color of a view.
   *
   * @param viewId The view id.
   * @param color A color, not a resource id.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
    View view = getView(viewId);
    view.setBackgroundColor(color);
    return this;
  }

  /**
   * Will set background of a view.
   *
   * @param viewId The view id.
   * @param backgroundRes A resource to use as a background.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
    View view = getView(viewId);
    view.setBackgroundResource(backgroundRes);
    return this;
  }

  /**
   * Will set text color of a TextView.
   *
   * @param viewId The view id.
   * @param textColor The text color (not a resource id).
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
    TextView view = getView(viewId);
    view.setTextColor(textColor);
    return this;
  }

  /**
   * Will set the image of an ImageView from a drawable.
   *
   * @param viewId The view id.
   * @param drawable The image drawable.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
    ImageView view = getView(viewId);
    view.setImageDrawable(drawable);
    return this;
  }

  /**
   * Add an action to set the image of an image view. Can be called multiple times.
   */
  public BaseViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
    ImageView view = getView(viewId);
    view.setImageBitmap(bitmap);
    return this;
  }

  /**
   * Add an action to set the alpha of a view. Can be called multiple times.
   * Alpha between 0-1.
   */
  public BaseViewHolder setAlpha(@IdRes int viewId, float value) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      getView(viewId).setAlpha(value);
    } else {
      // Pre-honeycomb hack to set Alpha value
      AlphaAnimation alpha = new AlphaAnimation(value, value);
      alpha.setDuration(0);
      alpha.setFillAfter(true);
      getView(viewId).startAnimation(alpha);
    }
    return this;
  }

  /**
   * Set a view visibility to VISIBLE (true) or GONE (false).
   *
   * @param viewId The view id.
   * @param visible True for VISIBLE, false for GONE.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setGone(@IdRes int viewId, boolean visible) {
    View view = getView(viewId);
    view.setVisibility(visible ? View.VISIBLE : View.GONE);
    return this;
  }

  /**
   * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
   *
   * @param viewId The view id.
   * @param visible True for VISIBLE, false for INVISIBLE.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setVisible(@IdRes int viewId, boolean visible) {
    View view = getView(viewId);
    view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    return this;
  }

  /**
   * Add links into a TextView.
   *
   * @param viewId The id of the TextView to linkify.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder linkify(@IdRes int viewId) {
    TextView view = getView(viewId);
    Linkify.addLinks(view, Linkify.ALL);
    return this;
  }

  /**
   * Apply the typeface to the given viewId, and enable subpixel rendering.
   */
  public BaseViewHolder setTypeface(@IdRes int viewId, Typeface typeface) {
    TextView view = getView(viewId);
    view.setTypeface(typeface);
    view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    return this;
  }

  /**
   * Apply the typeface to all the given viewIds, and enable subpixel rendering.
   */
  public BaseViewHolder setTypeface(Typeface typeface, int... viewIds) {
    for (int viewId : viewIds) {
      TextView view = getView(viewId);
      view.setTypeface(typeface);
      view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
    return this;
  }

  /**
   * Sets the progress of a ProgressBar.
   *
   * @param viewId The view id.
   * @param progress The progress.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setProgress(@IdRes int viewId, int progress) {
    ProgressBar view = getView(viewId);
    view.setProgress(progress);
    return this;
  }

  /**
   * Sets the progress and max of a ProgressBar.
   *
   * @param viewId The view id.
   * @param progress The progress.
   * @param max The max value of a ProgressBar.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setProgress(@IdRes int viewId, int progress, int max) {
    ProgressBar view = getView(viewId);
    view.setMax(max);
    view.setProgress(progress);
    return this;
  }

  /**
   * Sets the range of a ProgressBar to 0...max.
   *
   * @param viewId The view id.
   * @param max The max value of a ProgressBar.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setMax(@IdRes int viewId, int max) {
    ProgressBar view = getView(viewId);
    view.setMax(max);
    return this;
  }

  /**
   * Sets the rating (the number of stars filled) of a RatingBar.
   *
   * @param viewId The view id.
   * @param rating The rating.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setRating(@IdRes int viewId, float rating) {
    RatingBar view = getView(viewId);
    view.setRating(rating);
    return this;
  }

  /**
   * Sets the rating (the number of stars filled) and max of a RatingBar.
   *
   * @param viewId The view id.
   * @param rating The rating.
   * @param max The range of the RatingBar to 0...max.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setRating(@IdRes int viewId, float rating, int max) {
    RatingBar view = getView(viewId);
    view.setMax(max);
    view.setRating(rating);
    return this;
  }

  /**
   * Sets the on click listener of the view.
   *
   * @param viewId The view id.
   * @param listener The on click listener;
   * @return The BaseViewHolder for chaining.
   */
  @Deprecated
  public BaseViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
    View view = getView(viewId);
    view.setOnClickListener(listener);
    return this;
  }

  /**
   * add childView id
   *
   * @param viewId add the child view id   can support childview click
   * @return if you use adapter bind listener
   * @link {(adapter.setOnItemChildClickListener(listener))}
   * <p>
   * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
   */
  @SuppressWarnings("unchecked")
  public BaseViewHolder addOnClickListener(@IdRes final int viewId) {
    childClickViewIds.add(viewId);
    final View view = getView(viewId);
    if (view != null) {
      if (!view.isClickable()) {
        view.setClickable(true);
      }
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (adapter.getOnItemChildClickListener() != null) {
            adapter.getOnItemChildClickListener().onItemChildClick(adapter, v, getClickPosition());
          }
        }
      });
    }

    return this;
  }

  /**
   * set nestview id
   *
   * @param viewId add the child view id   can support childview click
   * @return
   */
  public BaseViewHolder setNestView(@IdRes int viewId) {
    addOnClickListener(viewId);
    addOnLongClickListener(viewId);
    nestViews.add(viewId);
    return this;
  }

  /**
   * add long click view id
   *
   * @param viewId
   * @return if you use adapter bind listener
   * @link {(adapter.setOnItemChildLongClickListener(listener))}
   * <p>
   * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
   */
  @SuppressWarnings("unchecked")
  public BaseViewHolder addOnLongClickListener(@IdRes final int viewId) {
    itemChildLongClickViewIds.add(viewId);
    final View view = getView(viewId);
    if (view != null) {
      if (!view.isLongClickable()) {
        view.setLongClickable(true);
      }
      view.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          return adapter.getOnItemChildLongClickListener() != null &&
                 adapter.getOnItemChildLongClickListener().onItemChildLongClick(adapter, v, getClickPosition());
        }
      });
    }
    return this;
  }

  /**
   * Sets the on touch listener of the view.
   *
   * @param viewId The view id.
   * @param listener The on touch listener;
   * @return The BaseViewHolder for chaining.
   */
  @Deprecated
  public BaseViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
    View view = getView(viewId);
    view.setOnTouchListener(listener);
    return this;
  }

  /**
   * Sets the on long click listener of the view.
   *
   * @param viewId The view id.
   * @param listener The on long click listener;
   * @return The BaseViewHolder for chaining. Please use {@link #addOnLongClickListener(int)} (adapter.setOnItemChildLongClickListener(listener))}
   */
  @Deprecated
  public BaseViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
    View view = getView(viewId);
    view.setOnLongClickListener(listener);
    return this;
  }

  /**
   * Sets the listview or gridview's item click listener of the view
   *
   * @param viewId The view id.
   * @param listener The item on click listener;
   * @return The BaseViewHolder for chaining. Please use {@link #addOnClickListener(int)} (int)} (adapter.setOnItemChildClickListener(listener))}
   */
  @Deprecated
  public BaseViewHolder setOnItemClickListener(@IdRes int viewId, AdapterView.OnItemClickListener listener) {
    AdapterView view = getView(viewId);
    view.setOnItemClickListener(listener);
    return this;
  }

  /**
   * Sets the listview or gridview's item long click listener of the view
   *
   * @param viewId The view id.
   * @param listener The item long click listener;
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setOnItemLongClickListener(@IdRes int viewId, AdapterView.OnItemLongClickListener listener) {
    AdapterView view = getView(viewId);
    view.setOnItemLongClickListener(listener);
    return this;
  }

  /**
   * Sets the listview or gridview's item selected click listener of the view
   *
   * @param viewId The view id.
   * @param listener The item selected click listener;
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setOnItemSelectedClickListener(@IdRes int viewId, AdapterView.OnItemSelectedListener listener) {
    AdapterView view = getView(viewId);
    view.setOnItemSelectedListener(listener);
    return this;
  }

  /**
   * Sets the on checked change listener of the view.
   *
   * @param viewId The view id.
   * @param listener The checked change listener of compound button.
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
    CompoundButton view = getView(viewId);
    view.setOnCheckedChangeListener(listener);
    return this;
  }

  /**
   * Sets the tag of the view.
   *
   * @param viewId The view id.
   * @param tag The tag;
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setTag(@IdRes int viewId, Object tag) {
    View view = getView(viewId);
    view.setTag(tag);
    return this;
  }

  /**
   * Sets the tag of the view.
   *
   * @param viewId The view id.
   * @param key The key of tag;
   * @param tag The tag;
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setTag(@IdRes int viewId, int key, Object tag) {
    View view = getView(viewId);
    view.setTag(key, tag);
    return this;
  }

  /**
   * Sets the checked status of a checkable.
   *
   * @param viewId The view id.
   * @param checked The checked status;
   * @return The BaseViewHolder for chaining.
   */
  public BaseViewHolder setChecked(@IdRes int viewId, boolean checked) {
    View view = getView(viewId);
    // View unable cast to Checkable
    if (view instanceof Checkable) {
      ((Checkable) view).setChecked(checked);
    }
    return this;
  }

  /**
   * Sets the adapter of a adapter view.
   *
   * @param viewId The view id.
   * @param adapter The adapter;
   * @return The BaseViewHolder for chaining.
   */
  @SuppressWarnings("unchecked")
  public BaseViewHolder setAdapter(@IdRes int viewId, Adapter adapter) {
    AdapterView view = getView(viewId);
    view.setAdapter(adapter);
    return this;
  }

  /**
   * Sets the adapter of a adapter view.
   *
   * @param adapter The adapter;
   * @return The BaseViewHolder for chaining.
   */
  protected BaseViewHolder setAdapter(BaseQuickAdapter adapter) {
    this.adapter = adapter;
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T extends View> T getView(@IdRes int viewId) {
    View view = views.get(viewId);
    if (view == null) {
      view = itemView.findViewById(viewId);
      views.put(viewId, view);
    }
    return (T) view;
  }

  /**
   * Retrieves the last converted object on this view.
   */
  public Object getAssociatedObject() {
    return associatedObject;
  }

  /**
   * Should be called during convert
   */
  public void setAssociatedObject(Object associatedObject) {
    this.associatedObject = associatedObject;
  }
}
