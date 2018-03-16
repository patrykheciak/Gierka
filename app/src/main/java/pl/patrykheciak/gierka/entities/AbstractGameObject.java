package pl.patrykheciak.gierka.entities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public abstract class AbstractGameObject {

    private int screenWidth = 0;
    private int screenHeight = 0;



    public abstract void setScreenSize(int width, int height);
    public abstract void update();
    public abstract void render(Canvas canvas);
}
