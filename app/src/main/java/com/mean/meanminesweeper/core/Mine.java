package com.mean.meanminesweeper.core;

 public class Mine{
    private int xPos;
    private int yPos;
    private int mineAround;
    private boolean isMine;
    private boolean isShown;
    private boolean isMarked;
    Mine(int x,int y){
        this.xPos = x;
        this.yPos = y;
        mineAround = 0;
        isMine = false;
        isShown = false;
        isMarked = false;
    }

     public int getxPos() {
         return xPos;
     }

     public void setxPos(int xPos) {
         this.xPos = xPos;
     }

     public int getyPos() {
         return yPos;
     }

     public void setyPos(int yPos) {
         this.yPos = yPos;
     }

     public int getMineAround() {
         return mineAround;
     }

     public void setMineAround(int mineAround) {
         this.mineAround = mineAround;
     }
     public void increaseMineAround() {
         this.mineAround++;
     }


     public boolean isMine() {
         return isMine;
     }

     public void setMine(boolean mine) {
         isMine = mine;
     }

     public boolean isShown() {
         return isShown;
     }

     public void setShown(boolean shown) {
         isShown = shown;
     }

     public boolean isMarked() {
         return isMarked;
     }

     public void setMarked(boolean marked) {
         isMarked = marked;
     }
 }
