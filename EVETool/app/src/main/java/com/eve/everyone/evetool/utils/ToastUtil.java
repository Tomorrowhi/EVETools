package com.eve.everyone.evetool.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast mToast;

    public static void showMessage(Context act, CharSequence charSequence) {
        if (mToast == null) {
            mToast = Toast.makeText(act, charSequence, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(charSequence);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showMessage(Context act, String charSequence) {
        if (mToast == null) {
            mToast = Toast.makeText(act, charSequence, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(charSequence);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showMessageLong(Context act, CharSequence charSequence) {
        if (mToast == null) {
            mToast = Toast.makeText(act, charSequence, Toast.LENGTH_LONG);
        } else {
            mToast.setText(charSequence);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void showMessageLong(Context act, String charSequence) {
        if (mToast == null) {
            mToast = Toast.makeText(act, charSequence, Toast.LENGTH_LONG);
        } else {
            mToast.setText(charSequence);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void hide() {
        mToast.cancel();
    }
}  
