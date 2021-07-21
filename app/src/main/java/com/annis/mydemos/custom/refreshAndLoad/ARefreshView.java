package com.annis.mydemos.custom.refreshAndLoad;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * author:Lee
 * date:2021/7/18
 * Describe:
 */
public abstract class ARefreshView extends FrameLayout {
    public enum State {
        /**
         * 初始状态
         */
        STATE_INIT,
        /**
         * 被拖拽过程中
         */
        STATE_DRAGGING,
        /**
         * 正在刷新
         */
        STATE_REFRESHING,
        /**
         * 正在收起 (有两个过程:1.收起至刷新  2.收起回到初始状态      ???在考虑是否需要这个状态???是否需要拆分)
         */
        STATE_PACK_UP;
    }

    /**
     * 记录当前的状态
     */
    State mState;

    /**
     * 下拉到这个距离释放就刷新
     */
    public int refreshHeight;

    public final float damp = 2.2f;

    public ARefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        preInit();
    }

    public ARefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public ARefreshView(Context context) {
        super(context);
        preInit();
    }

    /**
     * 开始刷新
     */
    public abstract void refresh();

    /**
     * 刷新完成
     */
    public abstract void refreshComplete();

    private void preInit() {
        refreshHeight = dp2px(66, getResources());
        init();
    }

    public abstract void init();

    private static int dp2px(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
