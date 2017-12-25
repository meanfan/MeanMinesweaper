package com.mean.meanminesweeper;

import android.app.*;
import android.os.*;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private Button b[][];
    private EditText et_rowNum = null;
    private EditText et_colNum = null;
    private EditText et_mineNum = null;
    private Map map = null;
    private GridLayout layout_mineSpace = null;
    private LinearLayout layout_main = null;
    private LinearLayout layout_ctrl = null;
    private LinearLayout layout_status = null;
    private TextView tv_time = null;
    private TextView tv_score = null;
    private TextView tv_mineNum = null;
    private Button bt_start = null;
    private int rowNum = 9;
    private int colNum = 9;
    private int mineNum = 10;
    private int clickCount = 0;
    private Timer gameTime = null;
    private TimerTask task = null;
    private  int sec = 0;
    private  int score = 0;
    private boolean isRoundEnd = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout_main = findViewById(R.id.activity_main);
        layout_ctrl = findViewById(R.id.layout_ctrl);
        layout_status = findViewById(R.id.layout_status);

        et_rowNum = findViewById(R.id.et_rowNum);
        et_rowNum.setText(String.format(getString(R.string.str_int),rowNum));
        et_rowNum.setInputType(InputType.TYPE_CLASS_NUMBER);

        et_colNum = findViewById(R.id.et_colNum);
        et_colNum.setText(String.format(getString(R.string.str_int),colNum));
        et_colNum.setInputType(InputType.TYPE_CLASS_NUMBER);

        et_mineNum = findViewById(R.id.et_mineNum);
        et_mineNum.setText(String.format(getString(R.string.str_int),mineNum));
        et_mineNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        tv_time = findViewById(R.id.tv_time);
        tv_score = findViewById(R.id.tv_score);
        tv_mineNum = findViewById(R.id.tv_mineNum);
        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRoundEnd)
                {
                    endRound();
                    return;
                }
                int row = Integer.parseInt(et_rowNum.getText().toString());
                int col = Integer.parseInt(et_colNum.getText().toString());
                int num = Integer.parseInt(et_mineNum.getText().toString());
                if(row<9 ||col<9) {
                    Toast.makeText(MainActivity.this, getString(R.string.size_too_small), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(row>30 || col>24)
                {
                    Toast.makeText(MainActivity.this, getString(R.string.size_too_big), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num<10) {
                    Toast.makeText(MainActivity.this, getString(R.string.mineNum_too_less), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num>row*num){
                    Toast.makeText(MainActivity.this, getString(R.string.mineNum_too_much), Toast.LENGTH_SHORT).show();
                    return;
                }
                rowNum = row;
                colNum = col;
                mineNum = num;
                bt_start.setText(getString(R.string.bt_restart));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startRound();
                    }
                });
            }
        });
    }public void startTiming()
    {
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sec++;
                        int minute = sec/60;
                        int second = sec%60;
                        String s_minute;
                        String s_second;
                        if(minute/10==0)
                            s_minute = String.format(getString(R.string.str_0_int),minute);
                        else
                            s_minute = String.format(getString(R.string.str_int),minute);
                        if(second/10==0)
                            s_second = String.format(getString(R.string.str_0_int),second);
                        else
                            s_second = String.format(getString(R.string.str_int),second);
                        tv_time.setText(s_minute+getString((R.string.time_divider))+s_second);
                        if(isRoundEnd)
                            task.cancel();
                    }
                });
            }
        };
        gameTime = new Timer();
        gameTime.schedule(task,1000,1000);
    }
    public void startRound()
    {
        et_rowNum.setEnabled(false);
        et_colNum.setEnabled(false);
        et_mineNum.setEnabled(false);
        sec = 0;
        score = 0;
        clickCount = 0;
        isRoundEnd = false;
        tv_score.setText(String.valueOf(0));
        tv_mineNum.setText(String.valueOf(mineNum));
        ScrollView sv = findViewById(R.id.sv);
        if(layout_mineSpace != null)
            sv.removeView(layout_mineSpace);
        layout_mineSpace = new GridLayout(this);
        layout_mineSpace.setColumnCount(colNum);
        b = new Button[rowNum][colNum];
        map = new Map(rowNum, colNum,mineNum);
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
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
                        int i=0,j=0;
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
                        if(clickCount == 0)
                        {
                            map.generateMine(i,j);
                            startTiming();
                        }
                        if(map.getShowStatus(i,j))
                            return;
                        int m=map.get(i, j);
                        if (m != -1)
                        {
                            if(m == 0) {
                                autoExpand(i,j);
                            }else{
                                showMine(i,j);
                            }
                        }
                        else {
                            endRound();
                            Toast.makeText(MainActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                            //layout_main.removeAllViews();
                        }
                        //Log.d("c",String.format("%d,%d",clickCount,rowNum * colNum -mineNum));
                        if(clickCount == rowNum * colNum -mineNum) {
                            endRound();
                            Toast.makeText(MainActivity.this, "胜利！", Toast.LENGTH_SHORT).show();
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
                            if(((Button)v).getText() == "") {
                                ((Button) v).setText("🚩");
                                tv_mineNum.setText(String.format(getString(R.string.str_int),Integer.parseInt(tv_mineNum.getText().toString())-1));
                            }
                            else{
                                ((Button)v).setText("");
                                tv_mineNum.setText(String.format(getString(R.string.str_int),Integer.parseInt(tv_mineNum.getText().toString())+1));
                                }
                        return true;
                    }
                });
            }
        }
        sv.addView(layout_mineSpace);
    }
    private  void endRound()
    {
        isRoundEnd = true;
        bt_start.setText(getString(R.string.bt_start));
        et_rowNum.setEnabled(true);
        et_colNum.setEnabled(true);
        et_mineNum.setEnabled(true);
        if(clickCount!=0)
            showAllMine();
        //bt_start.setText("重开");
    }
    private void showMine(int x,int y)
    {
        clickCount++;
        score++;
        tv_score.setText(String.format(getString(R.string.str_int),score));
        b[x][y].setText(String.format(getString(R.string.str_int),map.get(x,y)));
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
    }
    private void showAllMine(){
        for(int i = 0; i< rowNum; i++)
            for(int j = 0; j< colNum; j++)
            {
                int v = map.get(i,j);
                if(v==-1){
                    if(b[i][j].getText().toString().matches(""))
                        b[i][j].setText("💣");
                }
                else if(v==0)
                    b[i][j].setText("0");
                else
                    b[i][j].setText(String.format(getString(R.string.str_int),v));
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
                    a[i][j] = 0;
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
            Log.d("1","generating mine");
            Random random = new Random();
            int i=0,m,n;
            while(i<mineNum)
            {
                m = random.nextInt(x-1)%x;
                n = random.nextInt(y-1)%y;
                if(m!=ex && n!=ey && a[m][n]!=-1)
                {
                    a[m][n]=-1;
                    calc(m,n);
                    i++;
                }
            }
            Log.d("1","generated mine");
        }
        void calc(int m,int n)
        {
            if(m-1>=0 && n-1>=0)
                if(a[m-1][n-1] != -1)
                    a[m-1][n-1]++;
            if(m-1>=0)
                if(a[m-1][n] != -1)
                    a[m-1][n]++;
            if(m-1>=0 && n+1<colNum)
                if(a[m-1][n+1] != -1)
                    a[m-1][n+1]++;
            if(n-1>=0)
                if(a[m][n-1] != -1)
                    a[m][n-1]++;
            if(n+1<colNum)
                if(a[m][n+1]!= -1)
                    a[m][n+1]++;
            if(m+1<rowNum && n-1>=0)
                if(a[m+1][n-1] != -1)
                    a[m+1][n-1]++;
            if(m+1<rowNum)
                if(a[m+1][n] != -1)
                    a[m+1][n]++;
            if(m+1<rowNum && n+1<colNum)
                if(a[m+1][n+1]!=-1)
                    a[m+1][n+1]++;
        }
        public int get(int col, int row) {
            return a[col][row];
        }
    }
}