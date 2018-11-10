package com.ryeslim.darbuosna.controller;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ryeslim.darbuosna.MainActivity;

/**
 * Adapted from StackOverflow
 * Created by Dr Thomas Fankhauser Apr 21 '11 at 10:24
 * Modified by Marek Sebera Jan 10 '12 at 16:11
 */
public class ActivitySwipeDetector implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private MainActivity activity;
    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;

    public ActivitySwipeDetector(MainActivity activity) {
        this.activity = activity;
    }

    public void onRightToLeftSwipe(View v) {
        //Log.i(logTag, "RightToLeftSwipe!");
        activity.goForward();
    }

    public void onLeftToRightSwipe(View v) {
        //Log.i(logTag, "LeftToRightSwipe!");
        activity.goBackwards();
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe(v);
                        return true;
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe(v);
                        return true;
                    }
                } else {
                    //Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {

                    }
                    if (deltaY > 0) {

                    }
                } else {
                    //Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    v.performClick();
                }
            }
        }
        return false;
    }


}
