package com.lakue.linememolist.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lakue.linememolist.R;

public class RoundImageView extends ImageView {

    // 라운드처리 강도 값을 크게하면 라운드 범위가 커짐
    private  float radius = 7.0f;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs);
    }

    private void initView(AttributeSet attrs){
        TypedArray type = getContext().obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);
        radius = type.getFloat(R.styleable.RoundImageView_radius,7.0f);
    }

    public void setRectRadius(Float radius){
        this.radius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}