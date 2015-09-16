package com.ndgroup.flappy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Nikita on 25.08.2015.
 */
public class Coin extends Actor {
    public static final int WIDTH = 2;
    public static final int HEIGHT = 128;
    public static final float MOVE_VELOCITY = 120f;

    private Vector2 vel;
    private TextureRegion region;

    public Rectangle getBoundes() {
        return boundes;
    }

    public void setBoundes(Rectangle boundes) {
        this.boundes = boundes;
    }

    private Rectangle boundes;

    public Vector2 getVel() {
        return vel;
    }

    public Coin()
    {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        vel = new Vector2();
        vel.x =  - Pipe.MOVE_VELOCITY;
        region = new TextureRegion(Assets.bird);
        boundes = new Rectangle();
        boundes.set(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        setX(getX() + vel.x * delta);
        boundes.setPosition(getX(), getY());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
//                getScaleX(), getScaleY(), getRotation());


    }
}
