package com.suiyi.jnidemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class demoView extends View {
    public demoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        refreshDrawableState();

        invalidate();


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.clipRect(new Rect());
    }
}
