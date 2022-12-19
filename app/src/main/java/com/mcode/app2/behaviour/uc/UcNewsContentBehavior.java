package com.mcode.app2.behaviour.uc;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.mcode.app2.DemoApplication;
import com.mcode.app2.BuildConfig;
import com.mcode.app2.R;
import com.mcode.app2.behaviour.helper.HeaderScrollingViewBehavior;

import java.util.List;

public class UcNewsContentBehavior extends HeaderScrollingViewBehavior {
    private static final String TAG = "UcNewsContentBehavior";
    private float lastX, lastY;

    public UcNewsContentBehavior() {
    }

    public UcNewsContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDependentViewChanged");
        }
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY((int) (-dependency.getTranslationY() / (getHeaderOffsetRange() * 1.0f) * getScrollRange(dependency)));

    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            lastX = ev.getX();
            lastY = ev.getY();
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
            float currX = ev.getX();
            float currY = ev.getY();
            boolean horizontalScroll = Math.abs(currX - lastX) / Math.abs(currY - lastY) > 1;
            if (horizontalScroll && child.getTranslationY() == 0)
                return true;
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    protected View findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (isDependOn(view))
                return view;
        }
        return null;
    }

    @Override
    protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            return Math.max(0, v.getMeasuredHeight() - getFinalHeight());
        } else {
            return super.getScrollRange(v);
        }
    }

    private int getHeaderOffsetRange() {
        return DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.uc_news_header_pager_offset);
//        return DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.zero_dp);
    }

    /**
     * final hight should be like height + width
     * if we add 45 dip +45 dip then tab will show but margin of top 45 dp
     * @return
     */
    private int getFinalHeight() {
//        return DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.uc_news_tabs_height) + DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.uc_news_header_title_height);
        return DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.zero_dp)+DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.uc_news_header_title_height);
    }


    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.id_uc_news_header_pager;
    }
}