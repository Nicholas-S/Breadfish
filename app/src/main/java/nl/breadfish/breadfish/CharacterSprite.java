package nl.breadfish.breadfish;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite
{
    private Bitmap image;

    public int x, y;
    public int xVelocity = 10;
    public int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public CharacterSprite(Bitmap bmp)
    {
        image = bmp;
        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, 100, 100, null);
    }

    public void update()
    {
        y += yVelocity;
    }
}
