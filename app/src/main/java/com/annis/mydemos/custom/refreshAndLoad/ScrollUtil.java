package com.annis.mydemos.custom.refreshAndLoad;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author:Lee
 * date:2021/7/20
 * Describe:
 */
public class ScrollUtil {

    /**
     * 获取ViewGroup中可滑动的子视图
     *
     * @param parent 要查找的ViewGroup
     * @return 可滑动的子视图, 如果没有则返回null
     */
    public static View getScrollableChild(@NonNull ViewGroup parent) {
        int childCount = parent.getChildCount();
        if (childCount < 2) {
            return null;
        }
        View scroller = parent.getChildAt(1);
        if (scroller instanceof RecyclerView || scroller instanceof AdapterView) {
            return scroller;
        }
        if (scroller instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) scroller).getChildCount(); i++) {
                View child = ((ViewGroup) scroller).getChildAt(i);
                if (child instanceof RecyclerView || child instanceof AdapterView) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * 判断这个可滑动的子视图是否有滚动
     *
     * @param child 一个 RecycleView or AdapterView
     * @return true 子视图有滑动,false 没有
     */
    public static boolean hadScrolled(@NonNull View child) {
        if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            View view = recyclerView.getChildAt(0);
            int firstPosition = recyclerView.getChildAdapterPosition(view);
            return firstPosition != 0 || view.getTop() != 0;
        } else if (child instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) child;
            View childAt1 = adapterView.getChildAt(0);
            if (adapterView.getFirstVisiblePosition() != 0
                    || adapterView.getFirstVisiblePosition() == 0 && childAt1 != null && childAt1.getTop() < 0) {
                return true;
            }
        } else if (child.getScrollY() > 0) {
            return true;
        }
        return false;
    }
}
