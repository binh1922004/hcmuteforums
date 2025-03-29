package com.example.hcmuteforums.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBorderDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;

    public RecyclerViewBorderDecoration() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4); // Độ dày viền
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getLeft();
        int right = parent.getRight();
        int top = parent.getTop();
        int bottom = parent.getBottom();
        canvas.drawRect(left, top, right, bottom, paint);
    }
}
