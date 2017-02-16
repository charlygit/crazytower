package software.cm.crazytower.componentes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {
    private String text;
    private Paint textPaint;

    public TextProgressBar(Context context) {
        super(context);
        this.text = "0";
        this.textPaint = new Paint();
        this.textPaint.setColor(Color.BLACK);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.text = "0";
        this.textPaint = new Paint();
        this.textPaint.setColor(Color.BLACK);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.text = "0";
        this.textPaint = new Paint();
        this.textPaint.setColor(Color.BLACK);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bounds = new Rect();
        this.textPaint.getTextBounds(text, 0, text.length(), bounds);
        this.textPaint.setTextSize(60);

        Typeface currentTypeFace = this.textPaint.getTypeface();
        Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
        this.textPaint.setTypeface(bold);

        this.textPaint.setColor(Color.BLUE);

        int x = getWidth() / 2 - bounds.centerX();
        int y = getHeight() / 2 - bounds.centerY();
        canvas.drawText(text, x, y, this.textPaint);
    }

    public synchronized void setText(String text) {
        this.text = text;
        drawableStateChanged();
    }

    public void setTextColor(int color) {
        this.textPaint.setColor(color);
        drawableStateChanged();
    }
}
