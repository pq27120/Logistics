/**
 * @author xiaohuan
 * 2015年9月7日
 */
package com.huaren.logistics.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.huaren.logistics.common.Constant;
import com.huaren.logistics.common.OrderStatusEnum;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 通用工具类
 *
 * @author xiaohuan
 * @date 2015年9月7日
 */
public class CommonTool {

    // 用于格式化日期,作为日志文件名的一部分
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 打印日志
     */
    public static void showLog(String text) {
        if (Constant.LOG_FLAG && StringTool.isNotNull(text)) {
            Log.e("test", text);
        }
    }

    /**
     * 存储数据到SharePreference
     *
     * @param key   键
     * @param value 值
     */
    public static void setSharePreference(Context context, String key, String value) {
        SharedPreferences sharePreferences =
                context.getSharedPreferences(Constant.SHARE_PREFERENCES_NAME, 0);
        SharedPreferences.Editor localEditor = sharePreferences.edit();
        localEditor.putString(key, value);
        localEditor.commit();
    }

    /**
     * 存储数据到SharePreference
     */
    public static void setSharePreference(Context context, HashMap<String, String> param) {
        SharedPreferences sharePreferences =
                context.getSharedPreferences(Constant.SHARE_PREFERENCES_NAME, 0);
        SharedPreferences.Editor localEditor = sharePreferences.edit();
        Iterator<String> it = param.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            localEditor.putString(key, param.get(key));
        }
        localEditor.commit();
    }

    /**
     * 从SharePreference取数据
     *
     * @param key 键
     */
    public static String getSharePreference(Context context, String key) {
        SharedPreferences sharePreferences =
                context.getSharedPreferences(Constant.SHARE_PREFERENCES_NAME, 0);
        return sharePreferences.getString(key, null);
    }

    /**
     * 通过name获取字符资源
     */
    public static String getStringByName(Context context, String name) {
        if (!StringTool.isNotNull(name)) return "无";
        String str = "";
        try {

            str = context.getString(
                    context.getResources().getIdentifier(name, "string", context.getPackageName()));
        } catch (Exception e) {
            return "";
        }
        return str;
    }

    /**
     * view快照
     */
    public static Bitmap convertViewToBitmap(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        v.draw(c);
        return screenshot;
    }

    /**
     * 保存图片中
     */
    public static String saveBitmap2file(Bitmap bmp) {
        String picPath = null;
        String sdcardPath = null;
        boolean sdCardExist =
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            File sdDir = Environment.getExternalStorageDirectory(); // 获取跟目录
            sdcardPath = sdDir.toString();
        }
        if (sdcardPath == null) {
            return picPath;
        }
        OutputStream stream = null;
        try {
            File directory = new File(sdcardPath + "/acperror");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
            int quality = 100;
            picPath = sdcardPath + "/acperror/acp_temp.png";
            stream = new FileOutputStream(picPath);
            bmp.compress(format, quality, stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e2) {
            }
        }
        return picPath;
    }

    /**
     * 删除图片
     */
    public static void delPic(String picPath) {
        if (picPath != null) {
            File f = new File(picPath);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    public static void saveToLogFile(String str) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path + "/LogTest.txt");//
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static int getVersion(Context context) {
        // 获得一个系统包管理器
        PackageManager pm = context.getPackageManager();
        // 获得包管理器
        try {
            // 获得功能清单文件
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            // 不可能发生的异常
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        // 获得一个系统包管理器
        PackageManager pm = context.getPackageManager();
        // 获得包管理器
        try {
            // 获得功能清单文件
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            // 不可能发生的异常
            return "";
        }
    }

    /**
     * 检测网络是否连接
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                    && networkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据状态获取订单描述
     *
     * @param status
     * @return
     */
    public static String getDescByStatus(String status) {
        String desc = "";
        if (OrderStatusEnum.READEY_CARGO.getStatus().equals(status)) {
            desc = OrderStatusEnum.READEY_CARGO.getDesc();
        } else if (OrderStatusEnum.CARGO.getStatus().equals(status)) {
            desc = OrderStatusEnum.CARGO.getDesc();
        } else if (OrderStatusEnum.UNCARGO.getStatus().equals(status)) {
            desc = OrderStatusEnum.UNCARGO.getDesc();
        } else if (OrderStatusEnum.EVALUATION.getStatus().equals(status)) {
            desc = OrderStatusEnum.EVALUATION.getDesc();
        }
        return desc;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public static String saveCrashInfo2File(Context context, Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            } else {
                FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                outStream.write(sb.toString().getBytes());
                outStream.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(CommonTool.class.getSimpleName(), "an error occured while writing file...", e);
        }

        return null;
    }

    public static String sbCrashInfo2Str(Context context, Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }
}
