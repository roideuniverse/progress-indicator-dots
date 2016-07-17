package com.roide.progressdotslib;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by roide on 7/17/16.
 */
public class ProgressDotWidget extends LinearLayout {
    private static final int MIN_DOTS_REQUIRED = 2;
    private static final int ANIMATION_DURATION = 300;
    private int mDotCount;
    private int mPreviousPosition;
    private ArrayList<WeakReference<View>> mDotList = new ArrayList<>();
    private ArrayList<WeakReference<View>> mBridgeList = new ArrayList<>();

    public ProgressDotWidget(Context context) {
        super(context);
        init();
    }

    public ProgressDotWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressDotWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    public void setDotCount(int dotCount) {
        mDotCount = dotCount;
        addDots(mDotCount);
    }

    private void addDots(int dotCount) {
        /**
         * If less than 2 dots then return
         */
        if (dotCount < MIN_DOTS_REQUIRED) {
            return;
        }
        View firstDot = getDotView(true);
        mDotList.add(0, new WeakReference<View>(firstDot));
        mBridgeList.add(0, new WeakReference<View>(null));
        addView(firstDot);

        for (int i = 1; i < dotCount; i++) {
            View bridge = getBridgeView();
            View dot = getDotView(false);

            addView(bridge);
            addView(dot);

            mBridgeList.add(i, new WeakReference<View>(bridge));
            mDotList.add(i, new WeakReference<View>(dot));
        }
    }

    private FrameLayout getDotView(boolean enabled) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (enabled) {
            frameLayout.setBackgroundResource(R.drawable.widget_progress_dot);
        } else {
            frameLayout.setBackgroundResource(R.drawable.widget_progress_dot_disabled);
        }
        return frameLayout;
    }

    private View getBridgeView() {
        Resources res = getResources();
        int width = res.getDimensionPixelSize(R.dimen.one_half_standard_margin);
        int dotSize = res.getDimensionPixelSize(R.dimen.progress_widget_dot_size);
        int leftMargin = -dotSize;
        int rightMargin = leftMargin;

        LayoutParams params = new LayoutParams(width, dotSize);
        params.setMargins(leftMargin, 0, rightMargin, 0);
        View view = new View(getContext());
        view.setLayoutParams(params);
        view.setBackgroundResource(R.drawable.widget_progress_dot_bridge);
        view.setVisibility(INVISIBLE);

        return view;
    }

    public void setActivePosition(int position) {
        int diff = position - mPreviousPosition;
        if (diff > 0) {
            //moving to right
            View fromDot = mDotList.get(position - 1).get();
            View toDot = mDotList.get(position).get();
            View bridge = mBridgeList.get(position).get();
            animateDotMovement(fromDot, toDot, bridge);
        } else if (diff < 0) {
            //moving to left
            View fromDot = mDotList.get(position + 1).get();
            View toDot = mDotList.get(position).get();
            View bridge = mBridgeList.get(position + 1).get();
            animateDotMovement(fromDot, toDot, bridge);
        }
        mPreviousPosition = position;
    }

    private void animateDotMovement(final View fromDot, final View toDot, final View bridge) {
        int fromDotLoc[] = new int[2];
        int toDotLoc[] = new int[2];

        fromDot.getLocationOnScreen(fromDotLoc);
        toDot.getLocationOnScreen(toDotLoc);

        int translationOffset = toDotLoc[0] - fromDotLoc[0];

        fromDot.animate()
            .translationX(translationOffset)
            .setDuration(ANIMATION_DURATION)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    bridge.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    bridge.setVisibility(View.INVISIBLE);
                    fromDot.setTranslationX(0);
                    fromDot.setBackgroundResource(R.drawable.widget_progress_dot_disabled);
                    toDot.setBackgroundResource(R.drawable.widget_progress_dot);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    bridge.setVisibility(View.INVISIBLE);
                    fromDot.setTranslationX(0);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    //Do nothing
                }
            });
    }
}
