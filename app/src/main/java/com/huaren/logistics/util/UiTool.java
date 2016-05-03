/**
 * @author xiaohuan
 * 2015年6月13日
 */
package com.huaren.logistics.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaohuan
 * @date 2015年6月13日
 */
public class UiTool {
    /**
     * dp 转 px
     *
     * @param context 上下文
     * @param dp      dp值
     * @return px值
     */
    public static int dpToPx(Context context, float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取屏幕的参数
     */
    public static void getScreenConfig(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // Config.SrceenWidth = metric.widthPixels; // 屏幕宽度（像素）
        // Config.SrceenHeight = metric.heightPixels; // 屏幕高度（像素）
        // Config.SrceenDensity = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    public static void showToast(Context context, String text) {
        if (StringTool.isNotNull(text)) Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除webview缓存
     */
    public static void deleteWebviewCache(Context context) {
        try {
            CookieSyncManager.createInstance(context);
            CookieManager.getInstance().removeAllCookie();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0); // 强制隐藏键盘
        }
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec =
                View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    public static void setDialog(Activity context, Dialog dialog, int position, int animId,
                                 double scale) {
        Window window = dialog.getWindow();
        window.setGravity(position); // 此处可以设置dialog显示的位置
        if (animId != -1) {
            window.setWindowAnimations(animId); // 添加动画
        }
        dialog.show();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * scale); // 设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 处理表情字符串
     */
    public static SpannableStringBuilder dealImageString(String content, Context context) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        String rexgString = "(\\{img:[^{}]*\\})";
        Pattern pattern = Pattern.compile(rexgString);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String name = content.substring(matcher.start() + 5, matcher.end() - 1);
            int resID = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(resID);
            drawable.setBounds(0, 0, 40, 40);//这里设置图片的大小
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            builder.setSpan(imageSpan, matcher.start(), matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    /**
     * 隐藏系统键盘
     *
     * @param activity
     * @param ed
     */
    //用这个方法关闭系统键盘就不会出现光标消失的问题了
    public static void hideSoftInputMethod(Activity activity, EditText ed) {
        String hideKeyBoardFlag = CommonTool.getSharePreference(activity, "hideKeyBoard");
        if ("1".equals(hideKeyBoardFlag)) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            String methodName = null;
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            if (currentVersion >= 16) {
                // 4.2
                methodName = "setShowSoftInputOnFocus";  //
            } else if (currentVersion >= 14) {
                // 4.0
                methodName = "setSoftInputShownOnFocus";
            }
            if (methodName == null) {
                //最低级最不济的方式，这个方式会把光标给屏蔽
                ed.setInputType(InputType.TYPE_NULL);
            } else {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                try {
                    setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                    setShowSoftInputOnFocus.setAccessible(true);
                    setShowSoftInputOnFocus.invoke(ed, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
