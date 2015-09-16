package com.ndgroup.flappy;

import com.badlogic.gdx.math.MathUtils;import com.ndgroup.flappy.FlappyGame;

/**
 * Created by Nikita on 03.08.2015.
 */
public class Utils {

    public static float getRandomYOpening()
    {
        return MathUtils.random(FlappyGame.HEIGHT * .15f, FlappyGame.HEIGHT * .85f);
    }
}
