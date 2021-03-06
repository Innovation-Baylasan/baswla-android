/*
 * Copyright 2015 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.baylasan.sudanmap.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class RecycleEmptyErrorView extends RecyclerView {

  private View emptyView;

  private View errorView;

  private boolean isError;

  private int mVisibility;

  final private AdapterDataObserver mObserver = new AdapterDataObserver() {
    @Override
    public void onChanged() {
      updateEmptyView();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      updateEmptyView();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      updateEmptyView();
    }
  };

  public RecycleEmptyErrorView(Context context) {
    super(context);
    mVisibility = getVisibility();
  }

  public RecycleEmptyErrorView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mVisibility = getVisibility();
  }

  public RecycleEmptyErrorView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mVisibility = getVisibility();
  }

  @Override
  public void setAdapter(Adapter adapter) {
    Adapter oldAdapter = getAdapter();
    if (oldAdapter != null) {
      oldAdapter.unregisterAdapterDataObserver(mObserver);
    }
    super.setAdapter(adapter);
    if (adapter != null) {
      adapter.registerAdapterDataObserver(mObserver);
    }
    updateEmptyView();
  }

  @Override
  public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    mVisibility = visibility;
    updateErrorView();
    updateEmptyView();
  }

  private void updateEmptyView() {
    if (emptyView != null && getAdapter() != null) {
      boolean isShowEmptyView = getAdapter().getItemCount() == 0;
      emptyView.setVisibility(isShowEmptyView && !shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
      super.setVisibility(!isShowEmptyView && !shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
    }
  }

  private void updateErrorView() {
    if (errorView != null) {
      errorView.setVisibility(shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
    }
  }

  private boolean shouldShowErrorView() {
    if (errorView != null && isError) {
      return true;
    }
    return false;
  }

  public void setEmptyView(View emptyView) {
    this.emptyView = emptyView;
    updateEmptyView();
  }

  public void setErrorView(View errorView) {
    this.errorView = errorView;
    updateErrorView();
    updateEmptyView();
  }

  public void showErrorView() {
    isError = true;
    updateErrorView();
    updateEmptyView();
  }

  public void hideErrorView() {
    isError = false;
    updateErrorView();
    updateEmptyView();
  }

}