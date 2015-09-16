package com.ndgroup.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Nikita on 30.08.2015.
 */
public class MainMenuScreen extends ScreenAdapter {

    private FlappyGame game;
    private Stage stage;

    private Image title;
    private Image bacground;
    private Button playButton;

    public MainMenuScreen(final FlappyGame game) {
        this.game = game;
        stage = new Stage(new StretchViewport(FlappyGame.WIDTH, FlappyGame.HEIGHT));


        bacground = new Image(Assets.background);
        bacground.setPosition(0,0);

        title = new Image(Assets.title);
        title.setPosition(FlappyGame.WIDTH / 2, FlappyGame.HEIGHT - 50f, Align.top);
        title.setOrigin(Align.center);
        title.addAction(Actions.forever(Actions.sequence(Actions.scaleTo(1.2f, 1.2f, 1f, Interpolation.fade), Actions.scaleTo(1, 1, 1, Interpolation.elastic))));


        initPlayButton();

        stage.addActor(bacground);
        stage.addActor(playButton);
        stage.addActor(title);

        Gdx.input.setInputProcessor(stage);

    }

    private void initPlayButton() {
        playButton = new ImageButton(new TextureRegionDrawable(Assets.playUp), new TextureRegionDrawable(Assets.playDown));

        playButton.setPosition(FlappyGame.WIDTH / 2, FlappyGame.HEIGHT * .2f, Align.center);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();

    }

    @Override
    public void show() {
        playButton.clearActions();
        playButton.addAction(Actions.moveBy(0, -100f));
        playButton.addAction(Actions.moveBy(0, 100f, 2f, Interpolation.swing));
    }
}
