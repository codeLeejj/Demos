package com.annis.mydemos.custom.refreshAndLoad;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author:Lee
 * date:2021/7/17
 * Describe:
 */
public class RefreshAndLoader extends FrameLayout {
    ARefreshView mRefreshView;
    RefreshListener refreshListener;
    AutoScroller autoScroller;

    public RefreshAndLoader(@NonNull Context context) {
        super(context);
        init();
    }

    public RefreshAndLoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        autoScroller = new AutoScroller();
    }

    public void setRefreshView(@NonNull ARefreshView refreshView) {
        if (mRefreshView != null) {
            removeView(this.mRefreshView);
        }
        this.mRefreshView = refreshView;

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mRefreshView, 0, params);
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mRefreshView != null && getChildCount() >= 2) {
            View head = getChildAt(0);
            int dY = 0;
            if (head instanceof ARefreshView) {
                dY = -head.getMeasuredHeight();
                head.layout(0, dY, right, +dY + head.getMeasuredHeight());
            }
            View rv = getChildAt(1);
            //相对于头部View的高度
            int relativelyHead = dY + head.getMeasuredHeight();
            rv.layout(0, relativelyHead, right, relativelyHead + rv.getMeasuredHeight());
//            super.onLayout(changed, left, top, right, bottom);
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //没有刷新header
        if (mRefreshView == null) {
            return super.dispatchTouchEvent(ev);
        }
        View header = getChildAt(0);
        //判断是否是抬起动作
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL
                || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            int bottom = header.getBottom();
            if (bottom > 0) {
                if (mRefreshView.mState != ARefreshView.State.STATE_REFRESHING) {
                    recover(bottom);
                    return true;
                }
            }
        }

        boolean consumed = detector.onTouchEvent(ev);

        if (consumed) {
            return true;
        } else {
//            ev.setAction(MotionEvent.ACTION_CANCEL);//让父类接受不到真实的事件
            return super.dispatchTouchEvent(ev);
        }
    }

    GestureDetector detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mRefreshView.mState == ARefreshView.State.STATE_INIT
                    //横向滑动 | 向上推
                    && (Math.abs(distanceX) > Math.abs(distanceY) || distanceY > 0)) {
                return false;
            }
            //正在刷新 拦截事件   (防止触发第二次刷新和子视图滚动)
            if (mRefreshView.mState == ARefreshView.State.STATE_REFRESHING || mRefreshView.mState == ARefreshView.State.STATE_PACK_UP) {
                return true;
            }
            View scrollableChild = ScrollUtil.getScrollableChild(RefreshAndLoader.this);
            if (scrollableChild == null) {
                return false;
            }

            if (ScrollUtil.hadScrolled(scrollableChild)) {
                return false;
            }
            return moveHeader(distanceY, true);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });

    /**
     * 恢复滑动滑动距离
     *
     * @param bottom
     */
    private void recover(int bottom) {
        mRefreshView.mState = ARefreshView.State.STATE_PACK_UP;
        if (refreshListener != null && bottom > mRefreshView.refreshHeight) {
            autoScroller.scroll(bottom - mRefreshView.refreshHeight);
        } else {
            autoScroller.scroll(bottom);
        }
    }

    /**
     * 事件完成后的重置
     */
    private void check2Recover() {
        View header = getChildAt(0);
        View scroller = getChildAt(1);
        if (header.getBottom() == mRefreshView.refreshHeight && mRefreshView.mState == ARefreshView.State.STATE_REFRESHING) {
            return;
        }
        header.offsetTopAndBottom(-header.getBottom());
        scroller.offsetTopAndBottom(-scroller.getTop());
        mRefreshView.mState = ARefreshView.State.STATE_INIT;
    }

    private void offsetWithHeader(int distanceY, boolean userControl) {
        View header = getChildAt(0);
        View scroller = getChildAt(1);
        int targetY = header.getBottom() + distanceY;
        if (targetY < 0) {//视图已经恢复到初始状态
            header.offsetTopAndBottom(distanceY - targetY);
            scroller.offsetTopAndBottom(distanceY - targetY);
        } else {
            mRefreshView.mState = userControl ? ARefreshView.State.STATE_DRAGGING : ARefreshView.State.STATE_PACK_UP;
            header.offsetTopAndBottom(distanceY);
            scroller.offsetTopAndBottom(distanceY);
        }

    }

    /**
     * 显示头部
     *
     * @param distanceY
     * @param userControl true 由用户操作,false代码触发
     */
    private boolean moveHeader(float distanceY, boolean userControl) {
        //视图在默认位置,并下拉  -> 滑出顶部

        //视图在默认位置,并上推  -> 滑动列表
        if (mRefreshView.mState == ARefreshView.State.STATE_INIT && distanceY > 0) {
            return false;
        }

        View header = getChildAt(0);
        View scroller = getChildAt(1);

        int headerBottom = header.getBottom();
        Log.w("RefreshAndLoader", "distanceY:" + distanceY + "   -   headerBottom:" + headerBottom);
        //推到了顶顶就恢复初始位置
        if (headerBottom < 0 && distanceY > 0) {
            mRefreshView.mState = ARefreshView.State.STATE_INIT;
            check2Recover();
            return false;
        } else if (mRefreshView.mState == ARefreshView.State.STATE_REFRESHING) {
            return false;
        } else {//头部视图已经滑出
            //释放手势后缩回
            if (distanceY > 0 && !userControl) {
                offsetWithHeader(-(int) distanceY, false);
            } else {//用户操作的
                //已经置顶,释放手势给子视图处理
                if (mRefreshView.mState == ARefreshView.State.STATE_INIT && distanceY > 0) {
                    return false;
                }
                if (headerBottom > mRefreshView.refreshHeight) {
                    distanceY /= mRefreshView.damp;
                }
                offsetWithHeader(-(int) distanceY, userControl);
            }
        }
        //根据位置设置状态
        headerBottom = header.getBottom();
        if (!userControl) {
            if (headerBottom == mRefreshView.refreshHeight) {
                mRefreshView.refresh();
                mRefreshView.mState = ARefreshView.State.STATE_REFRESHING;
                refreshListener.refresh();
            } else if (headerBottom == 0) {
                mRefreshView.mState = ARefreshView.State.STATE_INIT;
            } else {
                mRefreshView.mState = ARefreshView.State.STATE_PACK_UP;
            }
        } else {
            if (headerBottom == 0 && distanceY > 0) {
                mRefreshView.mState = ARefreshView.State.STATE_INIT;
            } else {
                mRefreshView.mState = ARefreshView.State.STATE_DRAGGING;
            }
        }
        Log.w("RefreshAndLoader", "mState:" + mRefreshView.mState
                + "     header.bottom:" + header.getBottom() + "   -   scroller.top:" + scroller.getTop());
        return true;
    }

    /**
     * 刷新完毕
     */
    public void refreshComplete() {
        mRefreshView.refreshComplete();
        recover(mRefreshView.refreshHeight);
    }

    class AutoScroller implements Runnable {
        Scroller mScroller;
        int lastY;

        int count = 0;

        public AutoScroller() {
            mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                int xY = mScroller.getCurrY() - lastY;
                lastY = mScroller.getCurrY();
                Log.w("RefreshAndLoader", "AutoScroller:" + count++ + "      xY:" + xY + "     lastY:" + lastY);
                moveHeader(xY, false);
                post(this);
//                if (xY == 0) {
//                    removeCallbacks(this);
//                }
            } else {
                Log.w("RefreshAndLoader", "AutoScroller:  完成:" + lastY);

                removeCallbacks(this);
                check2Recover();
            }
        }

        /**
         * @param dis 滑动距离
         */
        public void scroll(int dis) {
            if (dis <= 0) {
                return;
            }
            Log.w("RefreshAndLoader", "AutoScroller:" + "      dis:" + dis);

            removeCallbacks(this);
            lastY = 0;
            count = 0;
            mScroller.startScroll(0, 0, 0, dis, 500);
            post(this);
        }
    }
}
