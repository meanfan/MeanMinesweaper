package com.mean.meanminesweaper;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import java.util.Random;


public class MainActivity extends Activity {
    private Button b[][];
    private TextView t1 = null;
    private Map map = null;
    private int HMineNum = 6;
    private int VMineNum = 16;
    private int mineNum = 12;
    private int i;
    private int j;
    private int clickCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        final LinearLayout layout_main = new LinearLayout(this);
        layout_main.setOrientation(LinearLayout.VERTICAL);
        GridLayout layout = new GridLayout(this);
        layout.setColumnCount(HMineNum);
        b = new Button[VMineNum][HMineNum];
        map = new Map(VMineNum,HMineNum,mineNum);
        for (i = 0; i < VMineNum; i++) {
            for (j = 0; j < HMineNum; j++) {
                b[i][j] = new Button(this);
                //b[i][j].setText("(" + i + "," + j + ")");
                b[i][j].setHeight(10);
                b[i][j].setWidth(b[i][j].getHeight());
                b[i][j].setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        int i,j=0;
                        for(i=0;i<VMineNum;i++)
                        {
                            boolean symbol = false;
                            for(j=0;j<HMineNum;j++)
                            {
                                if(v == b[i][j])
                                {
                                    symbol = true;
                                    break;
                                }
                            }
                            if(symbol == true)
                                break;
                        }
                        if(map.getShowStatus(i,j) == true)
                            return;
                        if(clickCount == 0)
                        {
                            map.generateMine(i,j);
                            map.calc();
                        }
                        int m=map.get(i, j);
                        if (m != -1)
                        {
                            showMine(i,j);
                            showNearbyZero(i,j,true);
                        }
                        else {
                            //((Button)v).setText("*");
                            showAll();
                            layout_main.removeAllViews();
                            t1 = new TextView(layout_main.getContext());
                            t1.setText("You are a loser!");
                            t1.setTextSize(100);
                            layout_main.addView(t1);
                        }
                        if(clickCount == VMineNum * HMineNum -mineNum) {
                            showAll();
                            layout_main.removeAllViews();
                            t1 = new TextView(layout_main.getContext());
                            t1.setText("You win!");
                            t1.setTextSize(100);
                            layout_main.addView(t1);
                        }
                    }
                });
                b[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int i,j=0;
                        for(i=0;i<VMineNum;i++)
                        {
                            boolean symbol = false;
                            for(j=0;j<HMineNum;j++)
                            {
                                if(v == b[i][j])
                                {
                                    symbol = true;
                                    break;
                                }
                            }
                            if(symbol == true)
                                break;
                        }
                        if(map.getShowStatus(i,j) == false)
                            if(((Button)v).getText() == "")
                                ((Button)v).setText("🚩");
                            else
                                ((Button)v).setText("");
                        return true;
                    }
                });
                layout.addView(b[i][j]);
            }
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(layout);
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.addView(sv);
        layout_main.addView(hsv);
        setContentView(layout_main);
    }
    private void showMine(int x,int y)
    {
        clickCount++;
        b[x][y].setText(""+map.get(x,y));
        map.setShowStatus(x,y);
    }
    private void showNearbyZero(int x,int y,boolean isOrigin)
    {
        if(x-1>=0)
            if (map.getShowStatus(x - 1,y) == false) {
                if (map.get(x - 1, y) == 0) {
                    showMine(x - 1, y);
                    showNearbyZero(x - 1, y,false);
                }else if(isOrigin == false && map.get(x - 1, y) != -1)
                    showMine(x - 1, y);
            }
        if(y-1>=0)
            if (map.getShowStatus(x,y-1) == false) {
                if (map.get(x, y-1) == 0) {
                    showMine(x, y-1);
                    showNearbyZero(x, y-1,false);
                }else if(isOrigin == false && map.get(x, y-1) != -1)
                    showMine(x, y-1);
            }
        if(y+1<HMineNum)
            if (map.getShowStatus(x,y+1) == false) {
                if (map.get(x, y+1) == 0) {
                    showMine(x, y+1);
                    showNearbyZero(x, y+1,false);
                }else if(isOrigin == false && map.get(x, y+1) != -1)
                    showMine(x, y+1);
            }
        if(x+1<VMineNum)
            if (map.getShowStatus(x + 1,y) == false) {
                if (map.get(x + 1, y) == 0) {
                    showMine(x + 1, y);
                    showNearbyZero(x + 1, y,false);
                }else if(isOrigin == false && map.get(x + 1, y) != -1)
                    showMine(x + 1, y);
            }
    }
    private void showAll(){
        for(int i=0;i<VMineNum;i++)
            for(int j=0;j<HMineNum;j++)
            {
                int v = map.get(i,j);
                if(v==-1)
                    b[i][j].setText("💣");
                else if(v==0)
                    b[i][j].setText("0");
                else
                {
                    b[i][j].setText(""+v);
                }
            }
    }
    private class Map {
        int a[][];
        private boolean isShown[][];
        private int x,y,mineNum;
        Map(int x,int y,int mineNum) {
            a = new int[x][y];
            isShown = new boolean[x][y];
            for(int i=0;i<x;i++)
                for(int j=0;j<y;j++) {
                    a[i][j] = -2;
                    isShown[i][j] = false;
                }
            this.x=x;
            this.y=y;
            this.mineNum = mineNum;
        }
        void setShowStatus(int x,int y)
        {
            isShown[x][y] = true;
        }
        boolean getShowStatus(int x,int y)
        {
            return isShown[x][y];
        }
        void generateMine(int ex,int ey){
            int i=0;
            int m,n;
            while(i<mineNum)
            {
                Random random = new Random();
                m = random.nextInt(x-1)%x;
                n = random.nextInt(y-1)%y;
                if(m!=ex && n!=ey && a[m][n]==-2)
                {
                    a[m][n]=-1;
                    i++;
                }
            }
        }
        void calc()
        {
            for(int i=0;i<x;i++)
                for(int j=0;j<y;j++)
                {
                    if(a[i][j]==-2)
                    {
                        a[i][j] = 0;
                        if(i-1>=0 && j-1>=0)
                            if(a[i-1][j-1] == -1)
                                a[i][j]++;
                        if(i-1>=0)
                            if(a[i-1][j] == -1)
                                a[i][j]++;
                        if(i-1>=0 && j+1<y)
                            if(a[i-1][j+1] == -1)
                                a[i][j]++;
                        if(j-1>=0)
                            if(a[i][j-1] == -1)
                                a[i][j]++;
                        if(j+1<y)
                            if(a[i][j+1]== -1)
                                a[i][j]++;
                        if(i+1<x && j-1>=0)
                            if(a[i+1][j-1] == -1)
                                a[i][j]++;
                        if(i+1<x)
                            if(a[i+1][j] == -1)
                                a[i][j]++;
                        if(i+1<x && j+1<y)
                            if(a[i+1][j+1]==-1)
                                a[i][j]++;
                    }
                }
        }
        public int get(int col, int row) {
            return a[col][row];
        }
    }
}