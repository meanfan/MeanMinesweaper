package com.mean.meanminesweeper;

import android.app.*;
import android.os.*;
import android.text.InputType;
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
    private int rowNum = 16;
    private int colNum = 16;
    private int mineNum = 30;
    private int i;
    private int j;
    private int clickCount = 0;
    private Timer gameTime = null;
    private TimerTask task = null;
    private  int sec = 0;
    private  int score = 0;
    private boolean isRoundEnd;
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
                int row = Integer.parseInt(et_rowNum.getText().toString());
                int col = Integer.parseInt(et_colNum.getText().toString());
                int num = Integer.parseInt(et_mineNum.getText().toString());
                if(row<3 ||col<3) {
                    Toast.makeText(MainActivity.this, getString(R.string.size_wrong_msg), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num<1) {
                    Toast.makeText(MainActivity.this, getString(R.string.mineNum_to_less), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num>row*num){
                    Toast.makeText(MainActivity.this, getString(R.string.mineNum_to_much), Toast.LENGTH_SHORT).show();
                    return;
                }
                rowNum = row;
                colNum = col;
                mineNum = num;
                startRound();
            }
        });
        layout_ctrl.setVisibility(View.VISIBLE);
        layout_status.setVisibility(View.INVISIBLE);
    }
    public void startRound()
    {
        sec=0;
        score = 0;
        clickCount = 0;
        isRoundEnd = false;
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
        tv_score.setText(String.valueOf(0));
        tv_mineNum.setText(String.valueOf(mineNum));
        layout_status.setVisibility(View.VISIBLE);
        ScrollView sv = findViewById(R.id.sv);
        if(layout_mineSpace != null)
            sv.removeView(layout_mineSpace);
        layout_mineSpace = new GridLayout(this);
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
                        if(isRoundEnd)
                            return;
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
                            endRound();
                            Toast.makeText(MainActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                            //layout_main.removeAllViews();
                        }
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
                            if(((Button)v).getText() == "")
                                ((Button)v).setText("🚩");
                            else
                                ((Button)v).setText("");
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
        showAllMine();
        layout_ctrl.setVisibility(View.VISIBLE);
        bt_start.setText("重开");
        layout_status.setVisibility(View.INVISIBLE);
    }
    private void showMine(int x,int y)
    {
        clickCount++;
        score++;
        tv_score.setText(String.format(getString(R.string.str_int),score));
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
    private void showAllMine(){
        for(int i = 0; i< rowNum; i++)
            for(int j = 0; j< colNum; j++)
            {
                int v = map.get(i,j);
                if(v==-1)
                    b[i][j].setText("💣");
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