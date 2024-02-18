package com.android.networkmanager.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

    public static void show(Context context, String text) {
        Toast makeText = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();
    }
}
