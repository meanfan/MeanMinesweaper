package com.mean.meanminesweeper.core;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameCore {
    private static GameCore instance = null;
    private int row;
    private int col;
    private int mine;
    private int sec;
    private int score;
    private String gameStatus;
    private MineMap mineMap;
    private TimerTask task;
    private Timer gameTime;
    private GameCore(){

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
        sec = 0;
        score = 0;
        mineMap = new MineMap(row, col, mine);
        gameStatus = "init done";
    }
    public void start(int startX,int startY){
        mineMap.initMines(startX,startY);
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
    public ArrayList<Mine> mineOnClick(int x, int y) {
        ArrayList<Mine> list = new ArrayList<>();
        if(gameStatus.equals("init done")){
            start(x,y);
            mineMap.setShown(x,y,true);
            gameStatus = "gaming";
            list.add(mineMap.getMine(x,y));
            autoExpand(x,y,list);
            return list;
        }
        if(gameStatus.equals("gaming")){
            if(mineMap.isMine(x,y)){
                gameStatus = "lose";
                return list;
            }else {
                mineMap.setShown(x,y,true);
                list.add(mineMap.getMine(x,y));
                autoExpand(x,y,list);
                return list;
            }
        }
        return  list;
    }
    public boolean mineOnLongclick(int x,int y){
        boolean mark = mineMap.isMarked(x,y);
        mineMap.setMarked(x,y,!mark);
        return mark;
    }
    private  void endRound()
    {
        if(gameStatus.equals("gaming"))
            showAllMine();
        gameStatus = "end";
        gameTime.cancel();
    }

    private void autoExpandProc(int m, int n,ArrayList<Mine> list)
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
            autoExpandProc(m, n,list);
            n = y;
            autoExpandProc(m, n,list);
            n = y + 1;
            autoExpandProc(m, n,list);

            m = x;
            n = y - 1;
            autoExpandProc(m, n,list);
            n = y + 1;
            autoExpandProc(m, n,list);

            m = x + 1;
            n = y - 1;
            autoExpandProc(m, n,list);
            n = y;
            autoExpandProc(m, n,list);
            n = y + 1;
            autoExpandProc(m, n,list);
        }
    }
    private void showAllMine(){
        //TODO
    }
}

