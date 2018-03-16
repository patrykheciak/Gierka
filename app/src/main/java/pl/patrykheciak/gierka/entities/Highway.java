package pl.patrykheciak.gierka.entities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import pl.patrykheciak.gierka.R;
import pl.patrykheciak.gierka.entities.AbstractGameObject;

public class Highway extends AbstractGameObject {

    private static final int highwaySpeed = 24;
    private static Bitmap bitmap;
    private int screenWidth;
    private int screenHeight;
    private int logicalY;
    private int logicalX;
    int posY = 0;
    int[] posYSegments;


    public Highway(Resources resources) {
        if (bitmap == null)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.highway_segment);
    }

    @Override
    public void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        logicalX = width;
        logicalY = (int) (((double) bitmap.getHeight() / bitmap.getWidth()) * width);

        int imagesToFill = height / logicalY + 2;

        posYSegments = new int[imagesToFill];
        for (int i = 0; i < posYSegments.length; i++) {
            posYSegments[i] = (i - 1) * logicalY;
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < posYSegments.length; i++) {
            posYSegments[i] += highwaySpeed;
        }
        for (int i = 0; i < posYSegments.length; i++) {
            if (posYSegments[i] > screenHeight)
                posYSegments[i] = min(posYSegments) - logicalY;
        }
    }

    private int min(int[] posYSegments) {
        int min = Integer.MAX_VALUE;
        for (int i : posYSegments) {
            if (i < min)
                min = i;
        }
        return min;
    }

    @Override
    public void render(Canvas canvas) {
        for (int i = 0; i < posYSegments.length; i++) {

            Rect rect = new Rect();
            rect.set(0, 0, logicalX, logicalY);
            rect.offset(0, posYSegments[i]);
            canvas.drawBitmap(bitmap, null, rect, null);
        }
    }

}

