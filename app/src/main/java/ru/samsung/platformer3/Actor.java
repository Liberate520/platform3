package ru.samsung.platformer3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Actor {

    float x, y, velX, velY;
    int width, height;
    Bitmap bitmap;
    Paint paint = new Paint();

    public Actor(float x, float y, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    public boolean onBound(Actor actor){
        Rect myRect = new Rect((int)x, (int)y, (int)x+width, (int)y+height);
        Rect actorRect = new Rect((int)actor.x, (int)actor.y,
                (int)actor.x+actor.width, (int)actor.y+actor.height);
        return myRect.intersect(actorRect);
    }
    public abstract void onDraw(Canvas canvas);
    public abstract void update(int ms);

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }
}
