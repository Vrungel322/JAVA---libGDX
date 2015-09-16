package com.ndgroup.flappy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ndgroup.flappy.FlappyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = FlappyGame.WIDTH;
		config.height = FlappyGame.HEIGHT;

		new LwjglApplication(new FlappyGame(), config);
	}
}
