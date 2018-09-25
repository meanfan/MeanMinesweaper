package com.mean.meanminesweeper.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mean.meanminesweeper.R;
import com.mean.meanminesweeper.Util.PrefManager;

public class CustomSizeDialog extends Dialog {
    private NumberPicker numberPickerX;
    private NumberPicker numberPickerY;
    private OnConfirmClickListener mListener;

    public CustomSizeDialog(@NonNull Context context, final OnConfirmClickListener mListener, int sizeX, int sizeY) {
        super(context);
        this.mListener = mListener;
        setContentView(R.layout.dialog_custom_size);

        numberPickerX = findViewById(R.id.numPicker_x);
        numberPickerX.setValue(sizeX);
        numberPickerX.setMaxValue(30);
        numberPickerX.setMinValue(9);
        numberPickerX.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPickerY = findViewById(R.id.numPicker_y);
        numberPickerY.setValue(sizeY);
        numberPickerY.setMaxValue(30);
        numberPickerY.setMinValue(9);
        numberPickerY.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        findViewById(R.id.btn_dialog_custom_size_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.getData(numberPickerX.getValue(),numberPickerY.getValue());
                dismiss();
            }
        });
        findViewById(R.id.btn_dialog_custom_size_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    //Temporary Constructor for "CustomMineDialog"
    public CustomSizeDialog(@NonNull Context context, final OnConfirmClickListener mListener, int data1, int data2,String title,int pickerCount) {
        this(context,mListener,data1,data2);
        ((TextView)findViewById(R.id.tv_dialog_custom_title)).setText(title);
        if(pickerCount == 1){
            numberPickerY.setVisibility(View.INVISIBLE);
        }
    }


    public void  setTitle(String title){

    }

    public interface  OnConfirmClickListener {
        void getData(int sizeX,int sizeY); //get Data Interface for Activity
    }
}
