package ru.samsung.platformer3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Player player;
    private Bitmap bitmapPlayer, bitmapGround5;
    private final int timeInterval = 20;
    private Timer timer;
    private static int screenWidth, screenHeight;
    private List<Ground> grounds = new ArrayList<>();

    private static final int gravity = 1;

    public Game(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        bitmapPlayer = BitmapFactory.decodeResource(getResources(), R.drawable.player_run);
        bitmapGround5 = BitmapFactory.decodeResource(getResources(), R.drawable.surface5);
        bitmapGround5 = Bitmap.createScaledBitmap(bitmapGround5, 100, 100, true);

        grounds.add(new Ground(0, 300, bitmapGround5));
        player = new Player(10, 10, bitmapPlayer, 6);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        timer = new Timer();
        timer.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        timer.cancel();
    }

    private void update(){
        player.update(timeInterval);
        for (Ground ground: grounds){
            if (player.onBound(ground)){
                player.y = ground.y - player.height;
                player.setStay(true);
                player.velY = 0;
            }
        }
    }

    private void draw(){
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.BLUE);
        for (Ground ground: grounds){
            ground.onDraw(canvas);
        }
        player.onDraw(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                player.jump();
                break;
        }
        return true;
    }

    class Timer extends CountDownTimer{
        public Timer() {
            super(Integer.MAX_VALUE, timeInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            update();
            draw();
        }

        @Override
        public void onFinish() {

        }
    }

    public static int getGravity() {
        return gravity;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}
