package com.example.nikita.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Nikita on 13.06.2015.
 */
public class Explosion {
    private int x;
    private int y;
    private int widht;
    private int height;
    private int row;
    private  Animation animation = new Animation();
    private Bitmap spriteshit;

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames)
    {
        this.x = x;
        this.y = y;
        this.widht = w;
        this.height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spriteshit = res;

        for(int i = 0; i<image.length; i++)
        {
            if(i%5 == 0&&i>0) row++;
            image[i] = Bitmap.createBitmap(spriteshit, (i-(5*row))*widht, row*height, widht, height);
        }
        animation.setFrames(image);
        animation.setDelay(10);

    }

    public void draw(Canvas canvas)
    {
        if(!animation.playedOnce())
        {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }
    }
    public void update()
    {
        if(!animation.playedOnce())
        {
            animation.update();
        }
    }
    public int getHeight(){return height;}

}
