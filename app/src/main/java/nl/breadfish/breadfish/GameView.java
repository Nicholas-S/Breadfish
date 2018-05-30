package nl.breadfish.breadfish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;

    Random r = new Random();

    Bitmap bmp;
    Bitmap bmp2;

    public static int gapHeight = 750;
    public static int velocity = 10;
    private static int max = 400;
    private static int min = 100;
    //initiates player and character
    private CharacterSprite characterSprite;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    //initiates baguetteList
    ArrayList<BaguetteSprite> baguetteList = new ArrayList();

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

    @SuppressLint("ClickableViewAccessibility")
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
        if(canvas!=null)
        {
            canvas.drawRGB(0, 100, 205);
            characterSprite.draw(canvas);
            for(int i = 0; i < baguetteList.size(); i++)
            {
                baguetteList.get(i).draw(canvas);
            }
        }
    }

    public void update()
    {
        logic();
        characterSprite.update();
        for(int i = 0; i < baguetteList.size(); i++)
        {
            baguetteList.get(i).update();
        }
    }

    //places baguetteList into the world
    public void makeLevel()
    {
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.baguette);
        bmp2 = BitmapFactory.decodeResource(getResources(),R.drawable.baguette);

        for(int i = 0; i < 100; i++)
        {
            baguetteList.add(new BaguetteSprite(bmp, bmp2, 500 + (500 * i), r.nextInt((max - min) + 1) + min));
        }
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

    public void logic() {

        for (int i = 0; i < baguetteList.size(); i++) {
            //Detect if the character is touching one of the baguetteList
            if (characterSprite.y < baguetteList.get(i).yY + (screenHeight / 2)
                    - (gapHeight / 2) && characterSprite.x + 300 > baguetteList.get(i).xX
                    && characterSprite.x < baguetteList.get(i).xX + 500) {
                resetLevel();
            } else if (characterSprite.y + 240 > (screenHeight / 2) +
                    (gapHeight / 2) + baguetteList.get(i).yY
                    && characterSprite.x + 300 > baguetteList.get(i).xX
                    && characterSprite.x < baguetteList.get(i).xX + 500) {
                resetLevel();
            }

            //Detect if the pipe has gone off the left of the
            //screen and regenerate further ahead
            if (baguetteList.get(i).xX + 500 < 0) {
                int value1 = r.nextInt(500);
                int value2 = r.nextInt(500);
                baguetteList.get(i).xX = screenWidth + value1 + 1000;
                baguetteList.get(i).yY = value2 - 250;
            }
        }

        //Detect if the character has gone off the
        //bottom or top of the screen
        if (characterSprite.y + 240 < 0) {
            resetLevel(); }
        if (characterSprite.y > screenHeight) {
            resetLevel(); }
    }

    public void resetLevel()
    {
        characterSprite.y = 1000;
        baguetteList.clear();
        for(int i = 0; i < 100; i++)
        {
            baguetteList.add(new BaguetteSprite(bmp, bmp2, 500 + (500 * i), r.nextInt((max - min) + 1) + min));
        }
    }
}