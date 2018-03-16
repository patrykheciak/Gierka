package pl.patrykheciak.gierka.entities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import pl.patrykheciak.gierka.R;
import pl.patrykheciak.gierka.UserInput;
import pl.patrykheciak.gierka.entities.AbstractGameObject;
import pl.patrykheciak.gierka.entities.ObstacleCar;

public class PlayersCar extends AbstractGameObject {

    private static Bitmap bitmap;

    private int screenWidth;
    private int screenHeight;

    private int logicalX;
    private int logicalY;
    private UserInput input;

    private int posX;
    private int posY;

    private Paint paint;
    private boolean vulnerable = true;

    public PlayersCar(Resources resources) {
        if (bitmap == null)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.car);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setAlpha(255);
    }

    @Override
    public void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        logicalX = width / 8;
        logicalY = (int) (((double) bitmap.getHeight() / bitmap.getWidth()) * logicalX);

        posX = screenWidth / 2;
        posY = (int) ((4.0 / 5) * screenHeight);
    }

    @Override
    public void update() {

        double leftBorderRate = 168.0 / 1080;
        int leftBorder = (int) (leftBorderRate * screenWidth);

        double rightBorderRate = 1 - leftBorderRate;
        int rightBorder = (int) (rightBorderRate * screenWidth) - logicalX;


        if (input == UserInput.LEFT) {
            posX -= 8;
            if (posX < leftBorder)
                posX = leftBorder;
        } else if (input == UserInput.RIGHT) {
            posX += 8;
            if (posX > rightBorder)
                posX = rightBorder;
        }
    }

    @Override
    public void render(Canvas canvas) {
        Rect rect = new Rect();
        rect.set(0, 0, logicalX, logicalY);
        rect.offset(posX, posY);
        canvas.drawBitmap(bitmap, null, rect, paint);
    }

    public void setInput(UserInput input) {
        this.input = input;
    }

    public boolean isColliding(Reward r) {
        Rect carRect = new Rect(posX, posY, posX + logicalX, posY + logicalY);
        Rect obstacleRect = r.getRect();
        if (carRect.intersect(obstacleRect)){
            r.initRandom();
            return true;
        } else {
            return false;
        }
    }

    public boolean isColliding(ObstacleCar c) {

        if (vulnerable) {

            Rect carRect = new Rect(posX, posY, posX + logicalX, posY + logicalY);
            Rect obstacleRect = c.getRect();
            if (carRect.intersect(obstacleRect)) {
                // handle collision
                vulnerable = false;
                paint.setAlpha(64);
                waitAndMakeVulnerableAgain();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private void waitAndMakeVulnerableAgain() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                vulnerable = true;
                paint.setAlpha(255);
            }
        }).start();
    }
}
