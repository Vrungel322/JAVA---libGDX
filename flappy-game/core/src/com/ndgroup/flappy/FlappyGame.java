package com.ndgroup.flappy;

import com.badlogic.gdx.Game;

public class FlappyGame extends Game {

	public static final int WIDTH = 300;
	public static final int HEIGHT = 480;
	public static final int GROUND_LEVEL = 20;

	
	@Override
	public void create ()
	{
		Assets.load();
		//setScreen(new GameplayScreen(this));
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		Assets.dispose();

	}
}
