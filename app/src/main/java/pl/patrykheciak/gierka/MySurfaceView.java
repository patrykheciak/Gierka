package pl.patrykheciak.gierka;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import pl.patrykheciak.gierka.entities.World;

public class MySurfaceView extends SurfaceView implements World.OnGameStatusListener {

    private static final String TAG = "MySurfaceView";
    private Context context;
    private World world;
    private UserInput userInput;
    private FPSCounter fpsCounter;
    private Paint paint;
    private boolean firstFrameRendered = false;
    private int lives = 3;
    private int score = 0;
    private OnDialogClosedListener callback;

    public MySurfaceView(Context context) {
        super(context);
        init(context);
        Log.d(TAG, "MySurfaceView(Context context)");
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        Log.d(TAG, "MySurfaceView(Context context, atrs)");
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        Log.d(TAG, "MySurfaceView(Context context, atrs, defStyleAttr)");
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        Log.d(TAG, "MySurfaceView(Context context, atrs, defStyleAttr, defStyleRes)");
    }

    private void init(Context context) {
        userInput = UserInput.NONE;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);

        fpsCounter = new FPSCounter();
        world = new World(getResources(), this);
        this.context = context;

        callback = (OnDialogClosedListener) context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, event.getAction() + " " + event.getX());
        if (event.getX() < getHolder().getSurfaceFrame().width() / 2) {
            userInput = UserInput.LEFT;
        } else {
            userInput = UserInput.RIGHT;
        }

        if (event.getAction() == 1) {
            userInput = UserInput.NONE;
        }

        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!firstFrameRendered) {
            world.setScreenSize(
                    getHolder().getSurfaceFrame().width(),
                    getHolder().getSurfaceFrame().height());
            firstFrameRendered = true;
        } else {
            world.update(userInput);
            world.render(canvas);
        }

        canvas.drawText("FPS: " + (int) fpsCounter.newFrame(), 16, 64, paint);
        canvas.drawText("Lives: " + lives, 16, 128, paint);
        canvas.drawText("Score: " + score, 450, 64, paint);

        invalidate();
    }

    @Override
    public void onLivesChanged(int lives) {
        this.lives = lives;
        if (lives == 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            callback.onSaveToCalendar(score);
                            world.reset();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            world.reset();
                            break;
                        default:
                            if (callback != null)
                                callback.onDialogClosed();
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Zdobyłeś " + ++score + " punktów. Chcesz zapisać wynik w kalendarzu?")
                    .setPositiveButton("Tak", dialogClickListener)
                    .setNegativeButton("Nie", dialogClickListener)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            world.reset();
                            if (callback != null)
                                callback.onDialogClosed();
                        }
                    }).show();
        }
    }



    @Override
    public void onScoreChanged(int score) {
        this.score = score;
    }

    public interface OnDialogClosedListener {
        void onDialogClosed();
        void onSaveToCalendar(int score);
    }
}
