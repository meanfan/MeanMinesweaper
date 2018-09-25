package com.mean.meanminesweeper.Util;

import android.content.SharedPreferences;
import android.os.Bundle;


public class PrefManager {
    public static final String CUSTOM_MODE_SIZE_X = "custom_mode_size_x";
    public static final int CUSTOM_MODE_SIZE_X_DEFAULT = 9;
    public static final String CUSTOM_MODE_SIZE_Y = "custom_mode_size_y";
    public static final int CUSTOM_MODE_SIZE_Y_DEFAULT = 9;
    public static final String CUSTOM_MODE_MINE_NUM = "custom_mode_mine_num";
    public static final int CUSTOM_MODE_MINE_NUM_DEFAULT = 10;

    public static Bundle getCustomModePref(SharedPreferences sps){
        Bundle bundle = new Bundle();
        bundle.putInt(CUSTOM_MODE_SIZE_X, sps.getInt(CUSTOM_MODE_SIZE_X,CUSTOM_MODE_SIZE_X_DEFAULT));
        bundle.putInt(CUSTOM_MODE_SIZE_Y, sps.getInt(CUSTOM_MODE_SIZE_Y,CUSTOM_MODE_SIZE_Y_DEFAULT));
        bundle.putInt(CUSTOM_MODE_MINE_NUM, sps.getInt(CUSTOM_MODE_MINE_NUM,CUSTOM_MODE_MINE_NUM_DEFAULT));
        return bundle;
    }
    public static void setCustomModePref(SharedPreferences sps,Bundle bundle){
        SharedPreferences.Editor editor = sps.edit();
        editor.putInt(CUSTOM_MODE_SIZE_X, bundle.getInt(CUSTOM_MODE_SIZE_X,CUSTOM_MODE_SIZE_X_DEFAULT));
        editor.putInt(CUSTOM_MODE_SIZE_Y, bundle.getInt(CUSTOM_MODE_SIZE_Y,CUSTOM_MODE_SIZE_Y_DEFAULT));
        editor.putInt(CUSTOM_MODE_MINE_NUM, bundle.getInt(CUSTOM_MODE_MINE_NUM,CUSTOM_MODE_MINE_NUM_DEFAULT));
        editor.commit();
    }

}
