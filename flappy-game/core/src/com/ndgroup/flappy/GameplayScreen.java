package com.ndgroup.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;


/**
 * Created by Nikita on 29.07.2015.
 */
public class GameplayScreen extends ScreenAdapter {

    public static final float PIPE_SPACING = 200f;
    public static final float PIPE_SETS = 3;

    protected FlappyGame game;
    protected OrthographicCamera camera;

    private Stage gameplayStage;
    private Stage uiStage;

    private Label scoreLabel;
    private Label tapToRetry;
    private int score;

    private Bird bird;
    private Image background;
    private Image ground;

    private enum State {PLAYING, DYING, DEAD}
    private State state = State.PLAYING;

    private Array<PipePair> pipePairs;

    public GameplayScreen(FlappyGame game) {
        this.game = game;

        pipePairs = new Array<PipePair>();

        camera = new OrthographicCamera(FlappyGame.WIDTH, FlappyGame.HEIGHT);

        gameplayStage = new Stage(new StretchViewport(FlappyGame.WIDTH, FlappyGame.HEIGHT, camera));
        uiStage = new Stage(new StretchViewport(FlappyGame.WIDTH, FlappyGame.HEIGHT));

        bird = new Bird();
        bird.setPosition(FlappyGame.HEIGHT * .25f, FlappyGame.HEIGHT / 2, Align.center);

        scoreLabel = new Label("0", new Label.LabelStyle(Assets.fontMedium, Color.WHITE));
        scoreLabel.setPosition(FlappyGame.WIDTH / 2, FlappyGame.HEIGHT * .9f, Align.center);
        uiStage.addActor(scoreLabel);

        tapToRetry = new Label("Tap To Retry!", new Label.LabelStyle(Assets.fontMedium, Color.WHITE));
        tapToRetry.setPosition(FlappyGame.WIDTH / 2, FlappyGame.HEIGHT * .2f, Align.center);
        uiStage.addActor(tapToRetry);

        initFirstPairOfPipes();
        initSecondPairOfPipes();
        initThirdPairOfPipes();


        background = new Image(Assets.background);
        ground = new Image(Assets.ground);

        gameplayStage.addActor(background);
        gameplayStage.addActor(ground);
        addPairsToStage(gameplayStage);
        gameplayStage.addActor(bird);


        initInputProcessor();
        

    }

    private void initFirstPairOfPipes()
    {
        Pipe topPipe = new Pipe();
        Pipe bottomPipe = new Pipe();
        topPipe.setRotation(180f);
        PipePair pair = new PipePair(topPipe, bottomPipe);
        pair.initFirst();
        pipePairs.add(pair);
    }

    private void initSecondPairOfPipes()
    {
        Pipe topPipe = new Pipe();
        Pipe bottomPipe = new Pipe();
        topPipe.setRotation(180f);
        PipePair pair = new PipePair(topPipe, bottomPipe);
        pair.initSecond();
        pipePairs.add(pair);
    }

    private void initThirdPairOfPipes()
    {
        Pipe topPipe = new Pipe();
        Pipe bottomPipe = new Pipe();
        topPipe.setRotation(180f);
        PipePair pair = new PipePair(topPipe, bottomPipe);
        pair.initThird();
        pipePairs.add(pair);
    }

    private void addPairsToStage(Stage gameplayStage)
    {
        for (int i = 0; i < pipePairs.size; i++)
        {
            PipePair pair = pipePairs.get(i);
            gameplayStage.addActor(pair.getBottomPipe());
            gameplayStage.addActor(pair.getTopPipe());
            gameplayStage.addActor(pair.getCoin());
        }
    }

    private void initInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                if(state == State.DYING)
                {

                    System.out.println("DYING");
                    //game.setScreen(new GameplayScreen(game));
                    return true;
                }

                if (state == State.DEAD)
                {
                    game.setScreen(new GameplayScreen(game));
                    System.out.println("444");
                    return true;
                }

                bird.jump();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        switch (state)
        {
            case PLAYING:

                updatePipePairs();
                gameplayStage.act();
                uiStage.act();
                checkCollisions();
                if (bird.getState() == Bird.State.dying)
                {
                    stopTheWorld();
                    tapToRetry.addAction(Actions.moveBy(0, 200f, .5f));
                    state = State.DYING;
                }
                gameplayStage.draw();
                uiStage.draw();
                break;

            case DEAD:
            case DYING:
                if (bird.getState() == Bird.State.dead)
                {
                    state = State.DEAD;
                }
                gameplayStage.act();
                gameplayStage.draw();
                uiStage.act();
                uiStage.draw();

                break;
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < pipePairs.size; i++)
        {
            PipePair pair = pipePairs.get(i);
            if (pair.getBottomPipe().getBoundes().overlaps(bird.getBoundes()))
            {
                stopTheWorld();
            }
            if (pair.getTopPipe().getBoundes().overlaps(bird.getBoundes()))
            {
               stopTheWorld();
            }
            if (pair.getCoin().getBoundes().overlaps(bird.getBoundes())) {
               score++;
                updateScoreLabel();
                pair.moveCoinOffscreen();
            }
        }
    }

    private void updateScoreLabel() {
        scoreLabel.setText(String.valueOf(score));
        //ToDo center label acording to width
    }

    private void stopTheWorld() {
        bird.die();
        killPipePairs();
        state = State.DYING;
        System.out.println("in stopTheWorld");
    }

    private void killPipePairs() {
        for (PipePair pair : pipePairs)
        {
            pair.getBottomPipe().setState(Pipe.State.dead);
            pair.getTopPipe().setState(Pipe.State.dead);
            pair.getCoin().getVel().setZero();
        }
    }

    private void updatePipePairs() {
        for (int i = 0; i < pipePairs.size; i++)
            pipePairs.get(i).update();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        Assets.batch.setProjectionMatrix(camera.combined);
        gameplayStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        tapToRetry.addAction(Actions.moveBy(0, -200f));
    }

    @Override
    public void dispose() {
        gameplayStage.dispose();
        uiStage.dispose();
    }
}
