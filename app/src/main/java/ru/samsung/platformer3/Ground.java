package ru.samsung.platformer3;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Ground extends Actor{

    public Ground(float x, float y, Bitmap bitmap) {
        super(x, y, bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    @Override
    public void update(int ms) {

    }
}
