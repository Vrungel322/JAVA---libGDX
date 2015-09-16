package com.ndgroup.flappy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Nikita on 29.07.2015.
 */
public class Bird extends Actor {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    public static final float GRAVITY = 780f;
    private static final float JUMP_VELOCITY = 250f;


    private Vector2 vel;
    private Vector2 accel;

    private TextureRegion region;
    private float time;

    private Rectangle boundes;

    private State state;

    public enum State{ alive, dying, dead}

    public Bird()
    {
        region = new TextureRegion(Assets.bird);
        setWidth(WIDTH);
        setHeight(HEIGHT);
        state = State.alive;

        vel = new Vector2(0 , 0);
        accel = new Vector2(0, -GRAVITY);

        boundes = new Rectangle(0, 0, WIDTH, HEIGHT);

        //difines center for rotatin
        setOrigin(Align.center);
    }

    public void jump()
    {
        vel.y = JUMP_VELOCITY;

        clearActions();

        RotateToAction a1 =  Actions.rotateTo(40, .1f);
        DelayAction da = Actions.delay(.3f);
        RotateToAction a2 =  Actions.rotateTo(-90, .5f);

        SequenceAction sequenceAction = Actions.sequence(a1, da, a2);

        addAction(sequenceAction);
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        time += delta;

        switch (state)
        {
            case alive:
                region = Assets.birdAnimation.getKeyFrame(time);
                actAlive(delta);
                break;

            case dead:
            case dying:
                vel.x = 0;
                accel.y = -GRAVITY;
                applyAccel(delta);
                updatePosition(delta);

                if(isBelowGround())
                {
                    setY(FlappyGame.GROUND_LEVEL);
                    setState(State.dead);
                }
                break;
        }

        updateBounds();
    }

    public Rectangle getBoundes() {
        return boundes;
    }

    public void setBoundes(Rectangle boundes) {
        this.boundes = boundes;
    }

    private void updateBounds() {
        boundes.x = getX();
        boundes.y = getY();
    }

    private void actAlive(float delta) {

        applyAccel(delta);
        updatePosition(delta);

        //setRotation(MathUtils.clamp(vel.y / JUMP_VELOCITY * 45f, -90f, 45f));

        if(isBelowGround())
        {
            setY(FlappyGame.GROUND_LEVEL);
            clearActions();
            die();
        }
        if(isABOVECeiling()) {
            setPosition(getX(), FlappyGame.HEIGHT, Align.topLeft);
            die();
        }
    }


    private boolean isBelowGround()
    {
        return (getY(Align.bottom) <= FlappyGame.GROUND_LEVEL);
    }

    private boolean isABOVECeiling()
    {
        return (getY(Align.top) >= FlappyGame.HEIGHT);
    }


    private void updatePosition(float delta) {
        setX(getX() + vel.x * delta);
        setY(getY() + vel.y * delta);
    }

    private void applyAccel(float delta) {
        vel.add(accel.x * delta, accel.y * delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

    }

    public void die()
    {
        state = State.dying;
        vel.y = 0;
        clearActions();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
