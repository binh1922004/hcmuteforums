package com.example.hcmuteforums.decoration;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint;
    private final int height;

    public DividerItemDecoration(Context context, int color, int height) {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        this.height = height;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = height; // Tạo khoảng trống cho divider
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent,
                       @NonNull RecyclerView.State state) {
        int left = 0;
        int right = parent.getWidth();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + height;
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}
