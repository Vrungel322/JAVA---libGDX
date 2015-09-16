package com.example.nikita.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Nikita on 24.05.2015.
 */
public class Smokepuff  extends GameObject{
    public int r;
    public Smokepuff(int x, int y)
    {
        r = 12;
        super.x = x;
        super.y = y;
    }
    public void update()
    {
        x-=10;
    }
    public void draw (Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x - r, y - r + 10, r, paint);
        canvas.drawCircle(x - r+2, y-r-2 + 10, r, paint);
        canvas.drawCircle(x - r+4, y-r+1 + 10, r, paint);

    }

}
