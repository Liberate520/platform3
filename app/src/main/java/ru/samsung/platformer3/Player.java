package ru.samsung.platformer3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor{

    private List<Rect> frames = new ArrayList<>();
    private int currentFrame = 0;
    private int timeForCurrentFrame = 0;
    private int frameTime = 80;
    private boolean stay = false;
    private int maxSpeed = 10;
    private boolean stop = false;

    public Player(float x, float y, Bitmap bitmap, int countFrame) {
        super(x, y, bitmap);
        width = bitmap.getWidth() / countFrame;
        for (int i = 0; i < countFrame; i++){
            frames.add(new Rect(i*width, 0, i*width+width, height));
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect position = new Rect((int)x,(int)y, (int)x+width, (int)y+height);
        canvas.drawBitmap(bitmap, frames.get(currentFrame), position, paint);
    }

    public void jump(){
        if (stay){
            stay = false;
            velY = -15;
        }
    }

    @Override
    public void update(int ms) {
        timeForCurrentFrame += ms;
        if (timeForCurrentFrame >= frameTime){
            currentFrame = (currentFrame + 1) % frames.size();
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }

        velY = velY + Game.getGravity();
        y = y + velY;

        for (Ground ground: Game.grounds){
            if (onBound(ground)){
                if (y < ground.y) {
                    y = ground.y - height;
                    setStay(true);
                } else {
                    y = ground.y + ground.height;
                }
                velY = 0;
            }
        }

        if (stop){
            if (velX > 1){
                velX -= 1;
            } else if (velX < -1){
                velX += 1;
            } else {
                velX = 0;
                stop = false;
            }
        }
        x = x + velX;

        for (Ground ground: Game.grounds){
            if (onBound(ground)){
                if (x < ground.x) {
                    x = ground.x - width;
                } else {
                    x = ground.x + ground.width;
                }
                velX = 0;
            }
        }

        if (y + height > Game.getScreenHeight()){
            y = Game.getScreenHeight() - height;
            velY = 0;
            stay = true;
        }
    }

    public void stop(){
        stop = true;
    }

    public void setVelX(float velX) {
        if (Math.abs(velX) > maxSpeed){
            if (velX > 0) {
                velX = maxSpeed;
            } else {
                velX = -maxSpeed;
            }
        } else {
            this.velX = velX;
        }
    }

    public void setVelY(float velY) {
        if (Math.abs(velY) > maxSpeed){
            if (velY > 0) {
                velY = maxSpeed;
            } else {
                velY = -maxSpeed;
            }
        }
        this.velY = velY;
    }

    public boolean isStay() {
        return stay;
    }

    public void setStay(boolean stay) {
        this.stay = stay;
    }

}
