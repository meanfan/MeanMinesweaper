package com.mean.meanminesweeper.view;

import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.mean.meanminesweeper.R;
import com.mean.meanminesweeper.controller.GameCore;
import com.mean.meanminesweeper.dao.Mine;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {
    GameCore gameCore = null;
    private Button mineButton[][];
    private EditText et_rowNum = null;
    private EditText et_colNum = null;
    private EditText et_mineNum = null;
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
    private boolean isRoundEnd = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
//        Toolbar tb =findViewById(R.id.tb_main);
//        tb.setTitle(getString(R.string.app_name));
//        tb.setTitleTextColor(Color.WHITE);
//        tb.setNavigationOnClickListener(new NavListener());
//        setSupportActionBar(tb);
//        tb.setNavigationIcon(R.drawable.ic_menu_white_24dp);
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

        gameCore = GameCore.getInstance();
        gameCore.setGameActivity(this);
        bt_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String gameStatus = gameCore.getGameStatus();
                if(gameStatus.equals("new")||gameStatus.contains("end")){
                    int row = Integer.parseInt(et_rowNum.getText().toString());
                    int col = Integer.parseInt(et_colNum.getText().toString());
                    int mine = Integer.parseInt(et_mineNum.getText().toString());
                    if(row<9 ||col<9) {
                        Toast.makeText(GameActivity.this, getString(R.string.size_too_small), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(row>30 || col>24)
                    {
                        Toast.makeText(GameActivity.this, getString(R.string.size_too_big), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mine<10) {
                        Toast.makeText(GameActivity.this, getString(R.string.mineNum_too_less), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mine>=row*col/2){
                        Toast.makeText(GameActivity.this, getString(R.string.mineNum_too_much), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    rowNum = row;
                    colNum = col;
                    mineNum = mine;
                    gameCore.initGame(rowNum,colNum,mineNum);
                    startRound();
                    bt_start.setText("结束");
                }else if(gameStatus.equals("gaming")){
                    //TODO end game
                    bt_start.setText("开始");
                }
            }
        });
    }
    public void startTiming()
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
                        if(gameCore.getGameStatus().contains("end"))
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
        tv_time.setText(getString(R.string.time_zero));
        tv_score.setText(String.valueOf(0));
        tv_mineNum.setText(String.valueOf(mineNum));
        ScrollView sv = findViewById(R.id.sv);
        if(layout_mineSpace != null)
            sv.removeView(layout_mineSpace);
        layout_mineSpace = new GridLayout(this);
        layout_mineSpace.setColumnCount(colNum);
        mineButton = new Button[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                mineButton[i][j] = new Button(this);
                //mineButton[i][j].setText("(" + i + "," + j + ")");
                GridLayout.Spec rowSpec = GridLayout.spec(i);
                GridLayout.Spec colSpec = GridLayout.spec(j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,colSpec);
                params.width = 100;
                params.height = 100;
                params.setGravity(Gravity.FILL);
                layout_mineSpace.addView(mineButton[i][j],params);
                mineButton[i][j].setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        GameCore gameCore = GameCore.getInstance();
                        int i=0,j=0;
                        for(i=0; i< rowNum; i++)
                        {
                            boolean symbol = false;
                            for(j=0; j< colNum; j++)
                            {
                                if(v == mineButton[i][j])
                                {
                                    symbol = true;
                                    break;
                                }
                            }
                            if(symbol)
                                break;
                        }
                        if(gameCore.getGameStatus().equals("init done"))
                        {
                            gameCore.mineOnClick(i,j);
                            startTiming();
                        }else if(gameCore.getGameStatus().equals("gaming")){
                            gameCore.mineOnClick(i,j);
                        }
                    }
                });
                mineButton[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int i,j=0;
                        for(i=0; i< rowNum; i++)
                        {
                            boolean symbol = false;
                            for(j=0; j< colNum; j++)
                            {
                                if(v == mineButton[i][j])
                                {
                                    symbol = true;
                                    break;
                                }
                            }
                            if(symbol)
                                break;
                        }
                        if(((Button) v).getText().toString().equals("")||((Button) v).getText().toString().equals("🚩"))
                            if(((Button)v).getText().equals("")) {
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
        bt_start.setText(getString(R.string.bt_start));
        et_rowNum.setEnabled(true);
        et_colNum.setEnabled(true);
        et_mineNum.setEnabled(true);
        Toast.makeText(GameActivity.this,"Game Over!",Toast.LENGTH_LONG).show();
    }
    public void showMine(ArrayList<Mine> mines)
    {
        for(int i=0;i<mines.size();i++){
            int x = mines.get(i).getxPos();
            int y = mines.get(i).getyPos();
            clickCount++;
            score++;
            tv_score.setText(String.format(getString(R.string.str_int),score));
            Log.d("d", "showMine: "+x+","+y);
            if(mines.get(i).isMine()){
                mineButton[x][y].setText("💣");
                if(!isRoundEnd){
                    isRoundEnd = true;
                    endRound();
                }
            }else{
                mineButton[x][y].setText(String.format(getString(R.string.str_int),mines.get(i).getMineAround()));
            }
        }
    }
}