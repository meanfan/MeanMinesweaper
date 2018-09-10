package com.mean.meanminesweeper.dao;

import java.util.ArrayList;
import java.util.Random;

public class MineMap {
        private ArrayList<Mine> mines = new ArrayList<>();
        private int row,col, mineNum;
        public MineMap(int row,int col,int mine) {
            this.row=row;
            this.col=col;
            this.mineNum = mine;
        }

    public ArrayList<Mine> getMines() {
        return mines;
    }

    public void initMines(int ex, int ey){
            for(int i=0;i<row;i++){
                for(int j=0;j<col;j++){
                    mines.add(new Mine(i,j));
                }
            }
            generateMine(ex,ey);
        }
        public int getMineAround(int x, int y) {
            return mines.get(x*col+y).getMineAround();
        }
        public void setShown(int x,int y, boolean status) {
            mines.get(x*col+y).setShown(status);
        }
        public boolean isShown(int x,int y)
        {
            return mines.get(x*col+y).isShown();
        }
        public boolean isMine(int x,int y){
            return mines.get(x*col+y).isMine();
        }
        public boolean isMarked(int x,int y){
            return mines.get(x*col+y).isMarked();
        }
        public void setMarked(int x,int y,boolean val){
            mines.get(x*col+y).setMarked(val);
        }
        public Mine getMine(int x,int y){
            return mines.get(x*col+y);
        }
        private void generateMine(int ex,int ey){
            //Log.d("MineMap","generating mine");
            Random random = new Random();
            int i=0,m,n;
            while(i< mineNum)
            {
                m = random.nextInt(row-1)%row;
                n = random.nextInt(col-1)%col;
                if(!(m==ex && n==ey) && !mines.get(m*col+n).isMine())
                {
                    mines.get(m*col+n).setMine(true);
                    newMineUpdates(m,n);
                    i++;
                }
            }
            //Log.d("1","generated mine");
        }
        private void newMineUpdates(int m, int n) {
            if(m-1>=0 && n-1>=0)
                if(!mines.get((m-1)*col+(n-1)).isMine())
                    mines.get((m-1)*col+(n-1)).increaseMineAround();
            if(m-1>=0)
                if(!mines.get((m-1)*col+( n )).isMine())
                    mines.get((m-1)*col+( n )).increaseMineAround();
            if(m-1>=0 && n+1<col)
                if(!mines.get((m-1)*col+(n+1)).isMine())
                    mines.get((m-1)*col+(n+1)).increaseMineAround();
            if(n-1>=0)
                if(!mines.get(( m )*col+(n-1)).isMine())
                    mines.get(( m )*col+(n-1)).increaseMineAround();
            if(n+1<col)
                if(!mines.get(( m )*col+(n+1)).isMine())
                    mines.get(( m )*col+(n+1)).increaseMineAround();
            if(m+1<row && n-1>=0)
                if(!mines.get((m+1)*col+(n-1)).isMine())
                    mines.get((m+1)*col+(n-1)).increaseMineAround();
            if(m+1<row)
                if(!mines.get((m+1)*col+( n )).isMine())
                    mines.get((m+1)*col+( n )).increaseMineAround();
            if(m+1<row && n+1<col)
                if(!mines.get((m+1)*col+(n+1)).isMine())
                    mines.get((m+1)*col+(n+1)).increaseMineAround();
        }
}

