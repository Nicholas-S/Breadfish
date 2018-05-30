package nl.breadfish.breadfish;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;

    public static int gapHeight = 470;
    public static int velocity = 10;
    //initiates player and character
    private CharacterSprite characterSprite;
    //initiates pipes
    private BaguetteSprite baguette1;
    private BaguetteSprite baguette2;
    private BaguetteSprite baguette3;

    public GameView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        characterSprite.y = characterSprite.y - (characterSprite.yVelocity * 20);
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.fish));
        makeLevel();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawRGB(0, 100, 205);
        characterSprite.draw(canvas);
        baguette1.draw(canvas);
        baguette2.draw(canvas);
        baguette3.draw(canvas);
    }

    public void update()
    {
        characterSprite.update();
        baguette1.update();
        baguette2.update();
        baguette3.update();
    }

    //places pipes into the world
    public void makeLevel()
    {
        Bitmap bmp;
        Bitmap bmp2;
        int y;
        int x;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.baguette), 500,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.baguette), 500,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        baguette1 = new BaguetteSprite(bmp, bmp2, 0, 2000);
        baguette2 = new BaguetteSprite(bmp, bmp2, -250, 3200);
        baguette3 = new BaguetteSprite(bmp, bmp2, 250, 4500);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
}