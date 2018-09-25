package com.mean.meanminesweeper.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.mean.meanminesweeper.R;
import com.mean.meanminesweeper.Util.PrefManager;

public class SettingsActivity extends AppCompatActivity {
    private int size_x = 9;
    private int size_y = 9;
    private int mine_num = 10;
    private SharedPreferences sps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        android.support.v7.widget.Toolbar tb =findViewById(R.id.tb_setting);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(PrefManager.CUSTOM_MODE_SIZE_X,size_x);
                bundle.putInt(PrefManager.CUSTOM_MODE_SIZE_Y,size_y);
                bundle.putInt(PrefManager.CUSTOM_MODE_MINE_NUM,mine_num);
//                SharedPreferences sps = getSharedPreferences("game_setting", Context.MODE_PRIVATE);
//                PrefManager.setCustomModePref(sps,bundle);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK,intent);
                SettingsActivity.this.finish();

            }
        });

        findViewById(R.id.cb_setting_auto_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckBox)v).setChecked(false);
                Toast.makeText(SettingsActivity.this,R.string.str_function_closed,Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.cb_setting_save_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckBox)v).setChecked(false);
                Toast.makeText(SettingsActivity.this,R.string.str_function_closed,Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.btn_setting_custom_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implement OnConfirmClickListener
                CustomSizeDialog.OnConfirmClickListener listener1 = new CustomSizeDialog.OnConfirmClickListener() {
                    @Override
                    public void getData(int x, int y) {
                        size_x = x;
                        size_y = y;
                        saveCustomSetting();
                        updateSizeDisp(x,y);
                    }
                };
                new CustomSizeDialog(SettingsActivity.this,listener1,size_x,size_y).show();
            }
        });

        //Temporary use CustomSizeDialog as "CustomMineDialog"
        findViewById(R.id.btn_setting_custom_mine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomSizeDialog.OnConfirmClickListener listener2 = new CustomSizeDialog.OnConfirmClickListener(){
                    @Override
                    public void getData(int mine, int useless) {
                        mine_num = mine;
                        saveCustomSetting();
                        updateMineDisp(mine);
                    }
                };
                new CustomSizeDialog(SettingsActivity.this,listener2,mine_num,mine_num,
                        getResources().getString(R.string.dialog_custom_mine_title).toString(),1).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sps = getSharedPreferences("game_setting", Context.MODE_PRIVATE);
        Bundle bundle = PrefManager.getCustomModePref(sps);
        size_x = bundle.getInt(PrefManager.CUSTOM_MODE_SIZE_X);
        size_y = bundle.getInt(PrefManager.CUSTOM_MODE_SIZE_Y);
        mine_num = bundle.getInt(PrefManager.CUSTOM_MODE_MINE_NUM);
        updateSizeDisp(size_x,size_y);
        updateMineDisp(mine_num);
    }
    private void updateSizeDisp(int x, int y){
        String sizeString = getResources().getText(R.string.setting_game_custom_size).toString()+
                x + " x " +y;
        ((Button)findViewById(R.id.btn_setting_custom_size)).setText(sizeString);
    }
    private void updateMineDisp(int m){
        String mineString = getResources().getText(R.string.setting_game_custom_mines).toString()+
                m;
        ((Button)findViewById(R.id.btn_setting_custom_mine)).setText(mineString);
    }
    private void saveCustomSetting(){
        Bundle bundle = new Bundle();
        bundle.putInt(PrefManager.CUSTOM_MODE_SIZE_X,size_x);
        bundle.putInt(PrefManager.CUSTOM_MODE_SIZE_Y,size_y);
        bundle.putInt(PrefManager.CUSTOM_MODE_MINE_NUM,mine_num);
        PrefManager.setCustomModePref(sps,bundle);
    }
}
