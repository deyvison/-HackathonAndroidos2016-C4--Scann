package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

public class CenturyGotic extends TextView {

    public CenturyGotic(Context context) {
        super(context);
        setTypefaceRoboto(context);
    }

    public CenturyGotic(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypefaceRoboto(context);
    }

    public CenturyGotic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypefaceRoboto(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CenturyGotic(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypefaceRoboto(context);
    }

    private void setTypefaceRoboto(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/century_gothic.ttf");
        setTypeface(typeface);
    }
}
