package com.mean.meanminesweeper.controller;

import android.util.Log;

import com.mean.meanminesweeper.dao.Mine;
import com.mean.meanminesweeper.dao.MineMap;
import com.mean.meanminesweeper.view.GameActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//NotThreadSafe
public class GameCore {
    private static final String TAG = "GameCore";
    private static GameCore instance = null;
    private GameActivity gameActivity = null;
    private int row;
    private int col;
    private int mine;
    private int sec;
    private int score;
    private int shownMineCounter = 0;
    private String gameStatus;
    private MineMap mineMap;
    private TimerTask task;
    private Timer gameTime;
    private GameCore(){
        gameStatus = "new";
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public String getGameStatus(){
        return gameStatus;
    }
    public static GameCore getInstance(){
        if(instance == null){
            instance = new GameCore();
        }
        return instance;
    }
    public void initGame(int row, int col,int mine){
        gameStatus = "init started";
        this.row = row;
        this.col = col;
        this.mine = mine;
        shownMineCounter = 0;
        sec = 0;
        score = 0;
        mineMap = new MineMap(row, col, mine);
        gameStatus = "init done";
    }
    public void startTiming()
    {
        task = new TimerTask() {
            @Override
            public void run() {
                sec++;
                if(gameStatus.matches("finish"))
                    task.cancel();
            }
        };
        gameTime = new Timer();
        gameTime.schedule(task,1000,1000);
    }
    public void mineOnClick(int x, int y) {
        ArrayList<Mine> list = new ArrayList<>();
        Log.d(TAG, "mineOnClick: gameStatus = "+gameStatus);
        if(gameStatus.equals("init done")){
            mineMap.initMines(x,y);
            mineMap.setShown(x,y,true);
            shownMineCounter = 0;
            gameStatus = "gaming";
            list.add(mineMap.getMine(x,y));
            autoExpand(x,y,list);
            gameActivity.showMine(list);
            shownMineCounter+=list.size();
            startTiming();
            Log.d(TAG, "mineOnClick: shownMineCounter = "+shownMineCounter);
            return;
        }
        if(mineMap.getMine(x,y).isShown()){
            return;
        }
        if(gameStatus.equals("gaming")){

            if(mineMap.isMine(x,y)){
                gameStatus = "lose_end";
                gameActivity.showMine(mineMap.getMines());
                endRound();
            }else {
                mineMap.setShown(x,y,true);
                list.add(mineMap.getMine(x,y));
                autoExpand(x,y,list);
                gameActivity.showMine(list);
                shownMineCounter+=list.size();
                Log.d(TAG, "mineOnClick: shownMineCounter = "+shownMineCounter);
                if(shownMineCounter+mine == row*col){
                    gameStatus = "win_end";
                    gameActivity.showMine(mineMap.getMines());
                }
            }
        }
    }
    private void endRound() {
        gameTime.cancel();
        mineMap=null;
    }

    private void expand(int m, int n, ArrayList<Mine> list)
    {
        if(m>=0 && m< row && n>=0 && n< col) {
            if(!mineMap.isShown(m,n)) {
                if(!mineMap.isMine(m,n)){
                    mineMap.setShown(m,n,true);
                    list.add(mineMap.getMine(m,n));
                }
                if(mineMap.getMineAround(m,n) == 0)
                    autoExpand(m,n,list);
            }
        }
    }
    private void autoExpand(int x,int y,ArrayList<Mine> list)
    {
        if(mineMap.getMineAround(x,y)==0) {
            int m, n;
            m = x - 1;
            n = y - 1;
            expand(m, n,list);
            n = y;
            expand(m, n,list);
            n = y + 1;
            expand(m, n,list);

            m = x;
            n = y - 1;
            expand(m, n,list);
            n = y + 1;
            expand(m, n,list);

            m = x + 1;
            n = y - 1;
            expand(m, n,list);
            n = y;
            expand(m, n,list);
            n = y + 1;
            expand(m, n,list);
        }
    }
    public void endGame(){
        GameCore.instance = null;
    }
}

