package com.simplexorg.searchfragment.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

public class SearchEditText extends AppCompatEditText {
    public interface OnKeyImeChangeListener {
        boolean onKeyImeChange(int keyCode, KeyEvent event);
    }

    private OnKeyImeChangeListener mOnKeyImeChangeListener;

    public SearchEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SearchEditText(Context context) {
        super(context);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setOnKeyImeChangeListener(OnKeyImeChangeListener listener) {
        mOnKeyImeChangeListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return mOnKeyImeChangeListener == null ||
                mOnKeyImeChangeListener.onKeyImeChange(keyCode, event);
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        // Inorder to prevent vertical scrolling of the edit text we override the default movement method.
        final MovementMethod defaultMethod = super.getDefaultMovementMethod();
        return new MovementMethod() {
            @Override
            public void initialize(TextView textView, Spannable spannable) {
                defaultMethod.initialize(textView, spannable);
            }

            @Override
            public boolean onKeyDown(TextView textView, Spannable spannable, int i, KeyEvent keyEvent) {
                return defaultMethod.onKeyDown(textView, spannable, i, keyEvent);
            }

            @Override
            public boolean onKeyUp(TextView textView, Spannable spannable, int i, KeyEvent keyEvent) {
                return defaultMethod.onKeyUp(textView, spannable, i, keyEvent);
            }

            @Override
            public boolean onKeyOther(TextView textView, Spannable spannable, KeyEvent keyEvent) {
                return defaultMethod.onKeyOther(textView, spannable, keyEvent);
            }

            @Override
            public void onTakeFocus(TextView textView, Spannable spannable, int i) {
                defaultMethod.onTakeFocus(textView, spannable, i);
            }

            @Override
            public boolean onTrackballEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
                return defaultMethod.onTrackballEvent(textView, spannable, motionEvent);
            }

            @Override
            public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onGenericMotionEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean canSelectArbitrarily() {
                return defaultMethod.canSelectArbitrarily();
            }
        };
    }
}
