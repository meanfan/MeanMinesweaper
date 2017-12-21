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
    private int rowNum = 16;
    private int colNum = 16;
    private int mineNum = 30;
    private int i;
    private int j;
    private int clickCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        final LinearLayout layout_main = new LinearLayout(this);
        layout_main.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout_ctrl = new LinearLayout(this);
        layout_ctrl.setOrientation(LinearLayout.HORIZONTAL);
        layout_ctrl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1));

        TextView tv_size = new TextView(this);
        tv_size.setText("尺寸： ");
        layout_ctrl.addView(tv_size);

        EditText et_rowNum = new EditText(this);
        et_rowNum.setText(""+rowNum);
        et_rowNum.setWidth(100);
        layout_ctrl.addView(et_rowNum);

        TextView tv_x = new TextView(this);
        tv_x.setText(" X ");
        layout_ctrl.addView(tv_x);

        EditText et_colNum = new EditText(this);
        et_colNum.setText(""+colNum);
        et_colNum.setWidth(100);
        layout_ctrl.addView(et_colNum);

        TextView tv_mineNum = new TextView(this);
        tv_mineNum.setText("雷数： ");
        layout_ctrl.addView(tv_mineNum);

        EditText et_mineNum = new EditText(this);
        et_mineNum.setText(""+mineNum);
        et_mineNum.setWidth(100);
        layout_ctrl.addView(et_mineNum);

        Button bt_start = new Button(this);
        bt_start.setText("开始");
        layout_ctrl.addView(bt_start);

        //TODO 自定义数据
        et_rowNum.setEnabled(false);
        et_colNum.setEnabled(false);
        et_mineNum.setEnabled(false);
        bt_start.setEnabled(false);

        layout_main.addView(layout_ctrl);
        GridLayout layout_mineSpace = new GridLayout(this);
        layout_mineSpace.setColumnCount(colNum);
        b = new Button[rowNum][colNum];
        map = new Map(rowNum, colNum,mineNum);
        for (i = 0; i < rowNum; i++) {
            for (j = 0; j < colNum; j++) {
                b[i][j] = new Button(this);
                //b[i][j].setText("(" + i + "," + j + ")");
                GridLayout.Spec rowSpec = GridLayout.spec(i);
                GridLayout.Spec colSpec = GridLayout.spec(j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,colSpec);
                params.width = 100;
                params.height = 100;
                params.setGravity(Gravity.FILL);
                layout_mineSpace.addView(b[i][j],params);
                b[i][j].setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        int i,j=0;
                        for(i=0; i< rowNum; i++)
                        {
                            boolean symbol = false;
                            for(j=0; j< colNum; j++)
                            {
                                if(v == b[i][j])
                                {
                                    symbol = true;
                                    break;
                                }
                            }
                            if(symbol)
                                break;
                        }
                        if(map.getShowStatus(i,j))
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
                            if(m == 0)
                            {
                                if(i-1>=0)
                                    autoExpand(i-1,j);
                                if(j-1>=0)
                                    autoExpand(i,j-1);
                                if(j+1< colNum)
                                    autoExpand(i,j+1);
                                if(i+1< rowNum)
                                    autoExpand(i+1,j);
                            }
                        }
                        else {
                            //((Button)v).setText("*");
                            showAll();
                            //layout_main.removeAllViews();
                            t1 = new TextView(layout_main.getContext());
                            t1.setText("You are a loser!");
                            t1.setTextSize(100);
                            layout_main.addView(t1);
                        }
                        if(clickCount == rowNum * colNum -mineNum) {
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
                        for(i=0; i< rowNum; i++)
                        {
                            boolean symbol = false;
                            for(j=0; j< colNum; j++)
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
                        if(!map.getShowStatus(i,j))
                            if(((Button)v).getText() == "")
                                ((Button)v).setText("🚩");
                            else
                                ((Button)v).setText("");
                        return true;
                    }
                });
            }
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(layout_mineSpace);
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
    private void autoExpandSub(int m,int n)
    {
        if(m>=0 && m< rowNum && n>=0 && n< colNum)
        {
            if(!map.getShowStatus(m,n))
            {
                if(map.get(m,n)!=-1)
                    showMine(m,n);
                if(map.get(m,n) == 0)
                    autoExpand(m,n);
            }
        }
    }
    private void autoExpand(int i,int j)
    {
        if(map.get(i,j) == 0) {
            int m, n;
            m = i - 1;
            n = j - 1;
            autoExpandSub(m, n);
            n = j;
            autoExpandSub(m, n);
            n = j + 1;
            autoExpandSub(m, n);

            m = i;
            n = j - 1;
            autoExpandSub(m, n);
            n = j + 1;
            autoExpandSub(m, n);

            m = i + 1;
            n = j - 1;
            autoExpandSub(m, n);
            n = j;
            autoExpandSub(m, n);
            n = j + 1;
            autoExpandSub(m, n);
        }
        if(map.get(i,j)!=-1)
            showMine(i, j);
    }
    private void showAll(){
        for(int i = 0; i< rowNum; i++)
            for(int j = 0; j< colNum; j++)
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