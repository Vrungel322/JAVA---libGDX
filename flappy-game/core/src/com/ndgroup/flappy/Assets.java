package com.ndgroup.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Nikita on 29.07.2015.
 */
public class Assets {

    //disposables
    public static TextureAtlas atlas;
    public static SpriteBatch batch;
    public static BitmapFont fontMedium;

    //Non disposebels
    public static TextureRegion bird;
    public static TextureRegion bird1;
    public static TextureRegion bird2;
    public static TextureRegion bird3;
    public static TextureRegion bird4;
    public static TextureRegion bird5;
    public static TextureRegion bird6;
    public static TextureRegion bird7;
    public static TextureRegion bird8;
    public static TextureRegion bird9;
    public static TextureRegion bird10;
    public static TextureRegion background;
    public static TextureRegion ground;
    public static TextureRegion pipe;
    public static TextureRegion title;
    public static TextureRegion playDown;
    public static TextureRegion playUp;

    //Animation
    public static Animation birdAnimation;

    public static void load()
    {
        atlas = new TextureAtlas("pack.atlas");
        batch = new SpriteBatch();

        fontMedium = new BitmapFont(Gdx.files.internal("font/font.fnt"), Gdx.files.internal("font/font_0.png"), false);


//        bird = atlas.findRegion("bird-16x16");
//        bird2 = atlas.findRegion("bird2-16x16");
//        bird3 = atlas.findRegion("bird3-16x16");
        bird = atlas.findRegion("bird1");
        bird1 = atlas.findRegion("bird2");
        bird3 = atlas.findRegion("bird4");
        bird4 = atlas.findRegion("bird5");
        bird5 = atlas.findRegion("bird6");
        bird6 = atlas.findRegion("bird7");
        bird7 = atlas.findRegion("bird8");
        bird8 = atlas.findRegion("bird9");

        //background = atlas.findRegion("background-300x480");
        background = atlas.findRegion("bacground1");
        //ground = atlas.findRegion("ground-321x24");
        ground = atlas.findRegion("ground1");
        pipe = atlas.findRegion("pipe-64x400");

        title = atlas.findRegion("title");
        playDown = atlas.findRegion("play_down");
        playUp = atlas.findRegion("play_up");



        birdAnimation = new Animation(.1f, bird, bird1, bird3, bird4, bird5, bird6, bird7, bird8);
        birdAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    public static void dispose()
    {
        if (atlas != null){
            atlas.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        fontMedium.dispose();
    }

}
