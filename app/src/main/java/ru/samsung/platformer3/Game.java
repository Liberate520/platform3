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
    static List<Ground> grounds = new ArrayList<>();
    private Joystick joystick;
    private int indexPointerJoystick;

    private static final int gravity = 1;

    public Game(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        bitmapPlayer = BitmapFactory.decodeResource(getResources(), R.drawable.player_run);
        bitmapGround5 = BitmapFactory.decodeResource(getResources(), R.drawable.surface5);
        bitmapGround5 = Bitmap.createScaledBitmap(bitmapGround5, 100, 100, true);

        grounds.add(new Ground(0, 300, bitmapGround5));
        grounds.add(new Ground(100, 500, bitmapGround5));
        player = new Player(10, 10, bitmapPlayer, 6);

        joystick = new Joystick(0, 0, 80, 40);
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
        joystick.setPosition(100, screenHeight - 100);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        timer.cancel();
    }

    private void update(){
        joystick.update();
        player.setVelX(player.getVelX() + (float) joystick.getActuatorX());
        player.update(timeInterval);
    }

    private void draw(){
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.BLUE);
        for (Ground ground: grounds){
            ground.onDraw(canvas);
        }
        player.onDraw(canvas);
        joystick.draw(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                if (joystick.isPressed(event.getX(), event.getY())){
                    joystick.setIsPressed(true);
                    indexPointerJoystick = event.getActionIndex();
                } else {
                    player.jump();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.isPressed(event.getX(1), event.getY(1))){
                    joystick.setIsPressed(true);
                    indexPointerJoystick = event.getActionIndex();
                } else {
                    player.jump();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()){
                    joystick.setActuator(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (joystick.getIsPressed()){
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    player.stop();
                }
            case MotionEvent.ACTION_POINTER_UP:
                if (joystick.getIsPressed() && indexPointerJoystick == event.getActionIndex()){
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    player.stop();
                }
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
