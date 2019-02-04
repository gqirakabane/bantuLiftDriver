package com.example.saurabh.driver.customtext;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by iws-035 on 28/3/18.
 */

public class ButtonBold extends AppCompatButton {

    public ButtonBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Bold.ttf");
        setTypeface(tf ,1);
    }
}