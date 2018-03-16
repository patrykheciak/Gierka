package pl.patrykheciak.gierka.entities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

import pl.patrykheciak.gierka.R;
import pl.patrykheciak.gierka.entities.AbstractGameObject;

public class ObstacleCar extends AbstractGameObject {

    private static Bitmap bitmap;
    private int lane;

    private int screenWidth;
    private int screenHeight;

    private int logicalX;
    private int logicalY;

    private int posX;
    private int posY;


    public ObstacleCar(Resources resources, int i) {

        if (bitmap == null)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.car);
        lane = i;
    }

    @Override
    public void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        initRandom();
    }

    public void initRandom() {
        logicalX = screenWidth / 8;
        logicalY = (int) (((double) bitmap.getHeight() / bitmap.getWidth()) * logicalX);

        posY = -(logicalY + new Random().nextInt(2 * screenHeight));

        double leftBorderRate = 228.0 / 1080;
        double rightBorderRate = 1 - leftBorderRate;
        int leftBorder = (int) (leftBorderRate * screenWidth);
        int rightBorder = (int) (rightBorderRate * screenWidth) - logicalX;

        int laneWidth = (rightBorder - leftBorder) / 3;

        posX = leftBorder + laneWidth * lane;
    }

    @Override
    public void update() {
        posY += 10;
        if (posY > screenHeight) {
            initRandom();
        }
    }

    @Override
    public void render(Canvas canvas) {
        Rect rect = new Rect();
        rect.set(0, 0, logicalX, logicalY);
        rect.offset(posX, posY);
        canvas.drawBitmap(bitmap, null, rect, null);
    }

    public Rect getRect() {
        return new Rect(posX, posY, posX + logicalX, posY + logicalY);
    }
}
