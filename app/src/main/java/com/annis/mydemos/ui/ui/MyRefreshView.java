package com.annis.mydemos.ui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

import com.annis.mydemos.R;
import com.annis.mydemos.custom.refreshAndLoad.ARefreshView;

/**
 * author:Lee
 * date:2021/7/19
 * Describe:
 */
public class MyRefreshView extends ARefreshView {

    public MyRefreshView(Context context) {
        super(context);
    }

    public MyRefreshView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.refresh_view, this, true);
    }


    @Override
    public void refresh() {

    }

    @Override
    public void refreshComplete() {

    }

}
