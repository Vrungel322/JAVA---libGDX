package com.example.nikita.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nikita on 09.05.2015.
 */


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final int MOVESPEED = -5;
    private long smokeStartTimer;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<TopBorder> topborders;
    private ArrayList<BotBorder> botborders;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;
    //increase to slow down difficulty progression , decrease to speed up difficulty progressing
    private int progressDenom = 20;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;


    public GamePanel(Context context)
    {
        super(context);

        //events
        getHolder().addCallback(this);



        // gamepanel
        setFocusable(true);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int widht,int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try
            {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch(InterruptedException e) {e.printStackTrace();}

        }
    }

    @Override
    public  void surfaceCreated(SurfaceHolder holder){
        bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 195, 70, 3);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        topborders = new ArrayList<TopBorder>();
        botborders = new ArrayList<BotBorder>();
        smokeStartTimer = System.nanoTime();
        missileStartTime = System.nanoTime();

        thread = new MainThread(getHolder(),this);

        //start
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(!player.getPlaying() && newGameCreated && reset)
            {
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying())
            {
              if(!started) started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);

    }

    public void update()
    {
        if(player.getPlaying())
        {
            if(botborders.isEmpty())
            {
                player.setPlaying(false);
                return;
            }
            if(topborders.isEmpty())
            {
                player.setPlaying(false);
                return;
            }

            bg.update();
            player.update();

            //calculate  height based on the score
            //max
            //min
            maxBorderHeight = 30 +player.getScore()/progressDenom;

            if(maxBorderHeight > HEIGHT/4) maxBorderHeight = HEIGHT/4;

            minBorderHeight = 5 + player.getScore()/progressDenom;

            //check bot border collisison
            for(int i = 0; i<botborders.size(); i++)
            {
                if(collision(botborders.get(i),player))
                {
                    player.setPlaying(false);
                }
            }

            //check top border collisison
            for(int i = 0; i<topborders.size(); i++)
            {
                if(collision(topborders.get(i),player))
                {
                    player.setPlaying(false);
                }
            }


            //update top border
            this.updateTopBorder();

            //update bot border
            this.updateBottomBorder();

            //add missile
            long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;
            if(missileElapsed > (2000 - player.getScore()/4))
            {
               //first missile
                if(missiles.size() == 0)
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.
                            missile), WIDTH+20, HEIGHT/2, 120, 45, player.getScore(), 13 ));
                }
                else
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH+20, (int) (rand.nextDouble()*(HEIGHT/2-(maxBorderHeight * 2)+maxBorderHeight)), 120, 45, player.getScore(), 13) );
                }

                //reset timer
                missileStartTime = System.nanoTime();
            }
            //loop thorougth every missile and check collision and remove
            for(int i = 0; i<missiles.size();i++)
            {
                //update missile
                missiles.get(i).update();
                if(collision(missiles.get(i),player))
                {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove missile if  it is way of the screen
                if(missiles.get(i).getX()< -100)
                {
                    missiles.remove(i);
                    break;
                }
            }

            //add smoke
            long elapsed = (System.nanoTime() - smokeStartTimer)/1000000;
            if(elapsed > 150)
            {
                smoke.add(new Smokepuff(player.getX(), player.getY()+10) );
                smokeStartTimer = System.nanoTime();
            }

            for(int i = 0; i<smoke.size(); i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }
            }
        }
        else
        {
            player.resetDYA();
            if (!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), player.getX(),
                        player.getY()-100, 300, 300, 25);
            }
            explosion.update();
            long resetElapsed = (System.nanoTime()- startReset)/1000000;

            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();
            }



        }
    }
    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return  false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);
        if(canvas!=null)
        {
            final int saveState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if(!dissapear) {
                player.draw(canvas);
            }

            for(Smokepuff sp: smoke)
            {
                sp.draw(canvas);
            }
            for(Missile m:missiles)
            {
                m.draw(canvas);
            }


            //draw top border
            for(TopBorder tb :topborders)
            {
                tb.draw(canvas);
            }
            //draw bot border
            for(BotBorder bb :botborders)
            {
                bb.draw(canvas);
            }
            //draw explosion
            if(started)
            {
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(saveState);
        }


    }

    public void updateTopBorder()
    {
        //every 50 points insert ramdomly placed top break
        if(player.getScore()%50 == 0)
        {
            topborders.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
            ),topborders.get(topborders.size()-1).getX()+15, 0, (int) (rand.nextDouble()*(maxBorderHeight
            ))+1));
        }
        for(int i = 0; i<topborders.size(); i++)
        {
            topborders.get(i).update();
            if(topborders.get(i).getX()<-20)
            {
                topborders.remove(i);
                //remove el of array list

                //
                if(topborders.get(topborders.size()-1).getHeight()>-maxBorderHeight)
                {
                    topDown = false;
                }

                if(topborders.get(topborders.size()-1).getHeight()<minBorderHeight)
                {
                    topDown = true;
                }

                if(topDown)
                {
                    topborders.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick),topborders.get(topborders.size()-1).getX()+15,
                    0,topborders.get(topborders.size()-1).getHeight()+1));
                }
                else
                {
                    topborders.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick),topborders.get(topborders.size()-1).getX()+15,
                            0,topborders.get(topborders.size()-1).getHeight()-1));
                }
            }
        }
    }
    public  void updateBottomBorder()
    {
        //every 40 point the same
        if(player.getScore()%40 == 0)
        {
            botborders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                    botborders.get(botborders.size()-1).getX()+0,(int)((rand.nextDouble()
                    *maxBorderHeight)+(HEIGHT - maxBorderHeight))));
        }
        //update bot border
        for(int i = 0; i<botborders.size(); i++)
        {
            botborders.get(i).update();

            //if off scree- remove
            if(botborders.get(i).getX()<-20) {
                botborders.remove(i);

                //
                if (botborders.get(botborders.size() - 1).getY() <= HEIGHT - maxBorderHeight) {
                    botDown = true;
                }

                if (botborders.get(botborders.size() - 1).getY() >= HEIGHT - minBorderHeight) {
                    botDown = false;
                }

                if (botDown) {
                    botborders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborders.get(botborders.size() - 1).getX() + 20, botborders.get(botborders.size() - 1
                    ).getY() + 1));
                } else {
                    botborders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborders.get(botborders.size() - 1).getX() + 20, botborders.get(botborders.size() - 1
                    ).getY() - 1));
                }
            }

        }

    }
    public void newGame()
    {
        dissapear = false;
        botborders.clear();
        topborders.clear();
        missiles.clear();
        smoke.clear();

        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.reseScore();
        player.setY(HEIGHT/2);
        player.resetDYA();

        if(player.getScore() > best)
        {
            best = player.getScore();
        }

        //create top borders
        for (int i = 0; i*20<WIDTH+40; i++)
        {
            //first top border
            if(i==0)
            {
                topborders.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                ),i*20, 0, 10));
            }
            else
            {
                topborders.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                ),i*20, 0, topborders.get(i-1).getHeight()+1));
            }
        }
        //create bot border
        for(int i = 0; i*20<WIDTH+40; i++)
        {
            //first bot border
            if(i==0)
            {
                botborders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick)
                ,i*20, HEIGHT-minBorderHeight));
            }
            else
            {
                botborders.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i*20, botborders.get(i-1).getY()-1));
            }
        }
        newGameCreated = true;
    }

    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + (player.getScore() * 3), 10, HEIGHT - 10, paint);
        canvas.drawText("Best: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if(!player.getPlaying() && newGameCreated && reset)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(80);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH / 2 - 50, HEIGHT / 2, paint1);

            paint1.setTextSize(60);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH / 2 - 50, HEIGHT / 2 + 50, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2 -50, HEIGHT/2 +100,paint1);
        }
    }


}
