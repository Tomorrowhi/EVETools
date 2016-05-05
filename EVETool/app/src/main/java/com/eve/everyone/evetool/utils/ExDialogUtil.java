package com.eve.everyone.evetool.utils;


import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxfun.R;
import com.maxfun.interfaces.DialogCallback;
import com.maxfun.view.MaterialProgressDrawable;


public class ExDialogUtil {

    //    private static ProgressDialog progressDialog;
    private static Dialog progressDialog;
    private static TextView diaologMsg;
    private static ImageView diaologIV;
    private static MaterialProgressDrawable materialProgressDrawable;
    private static ValueAnimator valueAnimator;
    ;
    private static Dialog dialog;

    /**
     * 显示弹窗
     *
     * @param context      context
     * @param charSequence 显示内容
     */
    public static void show(Context context, CharSequence charSequence) {
        if (progressDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.loading_dialog, null);
            diaologMsg = (TextView) v.findViewById(R.id.loading_dialog_msg);
            diaologIV = (ImageView) v.findViewById(R.id.loading_dialog_iv);
            diaologIV.setImageDrawable(progressAnimator(context));
            diaologMsg.setText(charSequence.toString());
            progressDialog = new Dialog(context, R.style.loading_dialog);
            progressDialog.setContentView(v);

//            progressDialog = ProgressDialog.show(context, "", charSequence, true, false);
            // 不可以触摸控件外部隐藏
            progressDialog.setCanceledOnTouchOutside(false);
            // 不可以用"返回键"取消
            progressDialog.setCancelable(false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle("");
//            progressDialog.setMessage(charSequence);
            diaologMsg.setText(charSequence);
        }
        try {
            progressDialog.show();
            valueAnimator.start();
            materialProgressDrawable.start();

            //设置宽高
            int width = Math.min(DensityUtil.getScreenWidth(context), DensityUtil.getScreenHeight(context));
            progressDialog.getWindow().setLayout(width / 5 * 3, width / 6 * 2);
//            //设置字体大小
//            View decorView = progressDialog.getWindow().getDecorView();
//            float fontSize = 18;
//            setDialogFontSize(decorView, DensityUtil.px2sp(context, fontSize));
            LogUtils.d(charSequence.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置自定义旋转动画
     *
     * @param context context
     * @return 显示的动画主体
     */
    private static MaterialProgressDrawable progressAnimator(Context context) {
        //加入自定义旋转动画
        materialProgressDrawable = new MaterialProgressDrawable(context, diaologIV);
        materialProgressDrawable.setBackgroundColor(ContextCompat.getColor(context, R.color.white3));
        materialProgressDrawable.setColorSchemeColors(ContextCompat.getColor(context, R.color.blue7));
        materialProgressDrawable.updateSizes(MaterialProgressDrawable.LARGE);
        materialProgressDrawable.showArrow(false);
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            //动画时长
            valueAnimator.setDuration(600);
            //动画由快倒慢
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float n = (float) animation.getAnimatedValue();
                    //圈圈的旋转角度
                    materialProgressDrawable.setProgressRotation(n * 0.5f);
                    //圈圈周长，0f-1F
                    materialProgressDrawable.setStartEndTrim(0f, n * 0.8f);
                    //透明度，0-255,0为不透明
//                    materialProgressDrawable.setAlpha((int) (255 * n));
                    materialProgressDrawable.setAlpha(255);
                }
            });
        }

        return materialProgressDrawable;
    }

    /**
     * 关闭加载进度弹窗的显示
     */
    public static void progressDialoghide() {
        LogUtils.d("关闭了加载进度弹窗");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            valueAnimator.end();
            materialProgressDrawable.stop();
            progressDialog = null;
        }
    }

    /**
     * 更改dialog默认的字体大小
     *
     * @param view view
     * @param size 字体大小
     */
    private static void setDialogFontSize(View view, int size) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                setDialogFontSize(parent.getChildAt(i), size);
            }
        } else if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setTextSize(size);
        }
    }

    public static SpannableStringBuilder getFormatText(Context mContext, int res, String... value) {
        int orangeColor = ContextCompat.getColor(mContext, R.color.orange1);
        String tips = mContext.getString(res);
        int[] index = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            StringBuffer tag = new StringBuffer("%");
            tag.append(i + 1);
            tag.append("$s");
            index[i] = tips.indexOf(tag.toString());
            tips = tips.replace(tag.toString(), value[i]);
        }
        SpannableStringBuilder style = new SpannableStringBuilder(tips);
        for (int i = 0; i < value.length; i++) {
            if (index[i] >= 0) {
                style.setSpan(new ForegroundColorSpan(orangeColor), index[i], value[i].length() + index[i], Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
            }
        }
        return style;
    }

    /**
     * 隐藏所有弹出框
     */
    public static void hideDialog() {
        LogUtils.d("关闭了dialog弹窗");
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
    }

    /**
     * 判断dialog是否显示
     *
     * @return true, 显示;false,没有显示
     */
    public static boolean isShow() {
        LogUtils.d("dialog弹窗正在显示");
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    /**
     * 公共提醒弹出框
     *
     * @param context     Activity对象
     * @param title       标题
     * @param message     内容
     * @param positiveBtn 确认按钮文字,设为null按钮会隐藏
     * @param negativeBtn 取消按钮文字，设为null
     * @param dc          回调
     * @return dialog对象
     */
    public static Dialog commonDialog(Context context, CharSequence title,
                                      CharSequence message,
                                      CharSequence positiveBtn,
                                      CharSequence negativeBtn,
                                      final DialogCallback... dc) {

        dialog = new Dialog(context, R.style.default_dialog);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.default_dialog, null);
        ((TextView) view.findViewById(R.id.dialog_title_msg)).setText(title);
        ((TextView) view.findViewById(R.id.dialog_content_1)).setText(message);
        if (negativeBtn != null) {
            ((Button) view.findViewById(R.id.btn_cancel)).setText(negativeBtn);
        } else {
            view.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
            view.findViewById(R.id.id_dialog_left_padding).setVisibility(View.VISIBLE);
            view.findViewById(R.id.id_dialog_right_padding).setVisibility(View.VISIBLE);
        }
        if (positiveBtn != null) {
            ((Button) view.findViewById(R.id.btn_ok)).setText(positiveBtn);
        } else {
            view.findViewById(R.id.btn_ok).setVisibility(View.GONE);
            view.findViewById(R.id.id_dialog_left_padding).setVisibility(View.VISIBLE);
            view.findViewById(R.id.id_dialog_right_padding).setVisibility(View.VISIBLE);
        }
        dialog.setContentView(view);
        view.findViewById(R.id.dialog_cancel_button).setOnClickListener(new View.OnClickListener() {  //关闭
            @Override
            public void onClick(View v) {
                if (dc.length > 1)
                    dc[1].execute(dialog, v);
                if (dialog != null && dialog.isShowing())
                    dialog.cancel();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {  //取消
            @Override
            public void onClick(View v) {
                if (dc.length > 1)
                    dc[1].execute(dialog, v);
                if (dialog != null && dialog.isShowing())
                    dialog.cancel();
            }


        });
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {  //确定
            @Override
            public void onClick(View v) {
                if (dc.length > 0)
                    dc[0].execute(dialog, v);
            }
        });
        //设置宽高
        int width = Math.min(DensityUtil.getScreenWidth(context), DensityUtil.getScreenHeight(context));
        dialog.getWindow().setLayout(width / 5 * 4, width / 6 * 3);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();

        return dialog;
    }
//
//
//    /**
//     * 公共提醒弹出框
//     *
//     * @return
//     */
//    public static Dialog commonDialogDefine(final Context context, String positiveButtonName, String negativeButtonName, CharSequence message, final DialogCallback... dc) {
//
//        final Dialog dialog = new Dialog(context, R.style.default_dialog);
//        LayoutInflater mInflater = LayoutInflater.from(context);
//        View view = mInflater.inflate(R.layout.dialog, null);
//        ((TextView) view.findViewById(R.id.dialog_title)).setText(context.getResources().getString(R.string.dialog_title_tips));
//        ((TextView) view.findViewById(R.id.dialog_content)).setText(message);
//        Button okBtn = (Button) view.findViewById(R.id.btn_ok);
//        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
//        okBtn.setText(positiveButtonName);
//        cancelBtn.setText(negativeButtonName);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.setContentView(view);
//        dialog.show();
//        view.findViewById(R.id.dialog_button).setOnClickListener(new OnClickListener() {  //关闭
//            @Override
//            public void onClick(View v) {
//                if (dc.length > 1)
//                    dc[1].execute(dialog, v);
//                if (dialog != null && dialog.isShowing())
//                    dialog.cancel();
//            }
//        });
//        cancelBtn.setOnClickListener(new OnClickListener() {  //取消
//            @Override
//            public void onClick(View v) {
//                if (dc.length > 1)
//                    dc[1].execute(dialog, v);
//                if (dialog != null && dialog.isShowing())
//                    dialog.cancel();
//            }
//
//
//        });
//        okBtn.setOnClickListener(new OnClickListener() {  //确定
//            @Override
//            public void onClick(View v) {
//                HttpUtil.disableView(v);
//                if (dc.length > 0)
//                    dc[0].execute(dialog, v);
//            }
//        });
//        return dialog;
//    }
//
//    /**
//     * 公共提醒弹出框 一个按钮
//     *
//     * @return
//     */
//    public static Dialog commonDialogOne(final Context context, CharSequence message, final DialogCallback dc) {
//
//        final Dialog dialog = new Dialog(context, R.style.default_dialog);
//        LayoutInflater mInflater = LayoutInflater.from(context);
//        View view = mInflater.inflate(R.layout.dialog, null);
//        ((TextView) view.findViewById(R.id.dialog_title)).setText(message);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.setContentView(view);
//        dialog.show();
//        view.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
//        view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {  //确定
//            @Override
//            public void onClick(View v) {
//                HttpUtil.disableView(v);
//                dc.execute(dialog, v);
//            }
//        });
//        return dialog;
//    }
//
//
//    /**
//     * 公共提醒弹出框 一个按钮
//     *
//     * @return
//     */
//    public static Dialog commonDialogClose(final Context context, CharSequence message, String info) {
//        final Dialog dialog = new Dialog(context, R.style.default_dialog);
//        LayoutInflater mInflater = LayoutInflater.from(context);
//        View view = mInflater.inflate(R.layout.dialog_close, null);
//        ((TextView) view.findViewById(R.id.dialog_title)).setText(message);
//        ((TextView) view.findViewById(R.id.dialog_info)).setText(info);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        dialog.setContentView(view);
//        dialog.show();
//        view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {  //取消
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//        return dialog;
//    }
//

    /**
     * 公共提醒弹出框  带一个弹出框
     *
     * @return
     */
    public static Dialog commonDialogEdit(final Context context,
                                          CharSequence message,
                                          CharSequence positiveBtn,
                                          CharSequence negativeBtn, final DialogCallback... dc) {

        dialog = new Dialog(context, R.style.default_dialog);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.dialog_edit, null);
        ((TextView) view.findViewById(R.id.id_edit_dialog_title)).setText(message);
        final EditText editText = ((EditText) view.findViewById(R.id.id_edit_dialog_input));
        if (negativeBtn != null) {
            ((Button) view.findViewById(R.id.id_edit_dialog_btn_cancel)).setText(negativeBtn);
        } else {
            view.findViewById(R.id.id_edit_dialog_btn_cancel).setVisibility(View.GONE);
            view.findViewById(R.id.id_edit_dialog_left_padding).setVisibility(View.VISIBLE);
            view.findViewById(R.id.id_edit_dialog_right_padding).setVisibility(View.VISIBLE);
        }
        if (positiveBtn != null) {
            ((Button) view.findViewById(R.id.id_edit_dialog_btn_ok)).setText(positiveBtn);
        } else {
            view.findViewById(R.id.id_edit_dialog_btn_ok).setVisibility(View.GONE);
            view.findViewById(R.id.id_edit_dialog_left_padding).setVisibility(View.VISIBLE);
            view.findViewById(R.id.id_edit_dialog_right_padding).setVisibility(View.VISIBLE);
        }
        dialog.setContentView(view);
        view.findViewById(R.id.id_edit_dialog_cancel_button).setOnClickListener(new View.OnClickListener() {  //关闭
            @Override
            public void onClick(View v) {
                if (dc.length > 1)
                    dc[1].execute(dialog, editText);
                if (dialog != null && dialog.isShowing())
                    dialog.cancel();
            }
        });
        view.findViewById(R.id.id_edit_dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {  //取消
            @Override
            public void onClick(View v) {
                if (dc.length > 1)
                    dc[1].execute(dialog, editText);
                if (dialog != null && dialog.isShowing())
                    dialog.cancel();
            }


        });
        view.findViewById(R.id.id_edit_dialog_btn_ok).setOnClickListener(new View.OnClickListener() {  //确定
            @Override
            public void onClick(View v) {
                if (dc.length > 0)
                    dc[0].execute(dialog, editText);
            }
        });
        //设置宽高
        int width = Math.min(DensityUtil.getScreenWidth(context), DensityUtil.getScreenHeight(context));
        dialog.getWindow().setLayout(width / 5 * 4, width / 6 * 3);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();

        return dialog;
    }


//    /**
//     * 公共提醒弹出框  带一个弹出框
//     *
//     * @return
//     */
//    public static Dialog commonDialogEdit2(final Activity activity, CharSequence message, final DialogCallback... dc) {
//
//        final Dialog dialog = new Dialog(activity, R.style.default_dialog);
//        LayoutInflater mInflater = LayoutInflater.from(activity);
//        View view = mInflater.inflate(R.layout.dialog_edit2, null);
//        ((TextView) view.findViewById(R.id.dialog_title)).setText(message);
//        final EditText editText = ((EditText) view.findViewById(R.id.edit_input));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.setContentView(view);
//        dialog.show();
//        ((Button) view.findViewById(R.id.btn_cancel)).setText(activity.getString(R.string.txt_cut_name));
//        view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {  //取消
//            @Override
//            public void onClick(View v) {
//                if (dc.length > 1)
//                    dc[1].execute(dialog, editText);
//
//            }
//        });
//        ((Button) view.findViewById(R.id.btn_ok)).setText(activity.getString(R.string.txt_add_name));
//        view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {  //确定
//            @Override
//            public void onClick(View v) {
//                if (dc.length > 0)
//                    dc[0].execute(dialog, editText);
//            }
//        });
//
//        view.findViewById(R.id.dialog_close).setOnClickListener(new OnClickListener() {  //确定
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//        return dialog;
//    }
//
//
//    /**
//     * 公共提醒弹出框  带一个弹出框
//     *
//     * @return
//     */
//    public static Dialog commonDialogEdit3(final Activity activity, CharSequence message, final DialogCallback... dc) {
//
//        final Dialog dialog = new Dialog(activity, R.style.default_dialog);
//        LayoutInflater mInflater = LayoutInflater.from(activity);
//        View view = mInflater.inflate(R.layout.dialog_edit3, null);
//        ((TextView) view.findViewById(R.id.dialog_title)).setText(message);
//        final EditText editText = ((EditText) view.findViewById(R.id.edit_input));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.setContentView(view);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        dialog.show();
//
//        view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {  //取消
//            @Override
//            public void onClick(View v) {
//                if (dc.length > 1)
//                    dc[1].execute(dialog, editText);
//                if (dialog != null && dialog.isShowing())
//                    dialog.cancel();
//            }
//        });
//        view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {  //确定
//            @Override
//            public void onClick(View v) {
//                HttpUtil.disableView(v);
//                if (dc.length > 0)
//                    dc[0].execute(dialog, editText);
//            }
//        });
//
//        int[] ids = {R.id.num_10, R.id.num_20, R.id.num_30, R.id.num_40, R.id.num_100};
//        for (int i : ids) {
//            view.findViewById(i).setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    editText.setText(((Button) v).getText());
//                }
//            });
//        }
//        return dialog;
//    }
//
//    /**
//     * 公共提醒弹出框  带一个弹出框
//     *
//     * @return
//     */
//    public static Dialog commonDialogEdit4(final Activity activity, CharSequence message, final DialogCallback... dc) {
//
//        final Dialog dialog = new Dialog(activity, R.style.default_dialog);
//        LayoutInflater mInflater = LayoutInflater.from(activity);
//        View view = mInflater.inflate(R.layout.dialog_edit4, null);
//        ((TextView) view.findViewById(R.id.dialog_title)).setText(message);
//        final EditText editText = ((EditText) view.findViewById(R.id.edit_input));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.setContentView(view);
//        dialog.show();
//        view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {  //取消
//            @Override
//            public void onClick(View v) {
//                if (dc.length > 1)
//                    dc[1].execute(dialog, editText);
//                if (dialog != null && dialog.isShowing())
//                    dialog.cancel();
//            }
//        });
//        view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {  //确定
//            @Override
//            public void onClick(View v) {
//                HttpUtil.disableView(v);
//                if (dc.length > 0)
//                    dc[0].execute(dialog, editText);
//            }
//        });
//        return dialog;
//    }
}
