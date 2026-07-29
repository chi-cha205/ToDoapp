package jp.ac.sus.t124042.todoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ProgressView extends View {

    private final Paint paint = new Paint();

    private int totalCount = 0;
    private int completedCount = 0;

    private boolean showPercent = true;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void setTaskCount(int total, int completed) {
        totalCount = total;
        completedCount = completed;
        invalidate();
    }

    //星の位置や達成率を描画する
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float percent = 0;

        if (totalCount > 0) {
            percent = (float) completedCount / totalCount;
        }

        float startX = 120;
        float endX = canvas.getWidth() - 120;
        float centerY = canvas.getHeight() / 2f;

        //道
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(10);

        canvas.drawLine(startX, centerY, endX, centerY, paint);

        //ゴール
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);

        canvas.drawText("🏁", endX + 40, centerY + 20, paint);

        //星
        float starX = startX + (endX - startX) * percent;

        paint.setColor(Color.rgb(255, 193, 7));
        paint.setTextSize(70);

        canvas.drawText("★", starX, centerY + 25, paint);

        //上の説明
        paint.setColor(Color.BLACK);
        paint.setTextSize(45);

        if (showPercent) {
            int p = Math.round(percent * 100);
            canvas.drawText(
                    "達成率 " + p + "%",
                    canvas.getWidth() / 2f,
                    centerY - 90,
                    paint
            );
        } else {
            canvas.drawText(
                    "完了 " + completedCount + " / " + totalCount,
                    canvas.getWidth() / 2f,
                    centerY - 90,
                    paint
            );
        }

        //下の説明
        paint.setTextSize(30);

        canvas.drawText(
                "画面をタップすると表示が切り替わります",
                canvas.getWidth() / 2f,
                centerY + 120,
                paint
        );
    }

    //タップすると表示内容を切り替える
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            showPercent = !showPercent;
            invalidate();
        }

        return true;
    }
}